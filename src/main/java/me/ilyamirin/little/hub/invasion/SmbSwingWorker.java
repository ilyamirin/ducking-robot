package me.ilyamirin.little.hub.invasion;

import com.google.inject.Inject;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.ilyamirin.little.hub.invasion.clients.CAFSClient;
import me.ilyamirin.little.hub.invasion.interaction.cafs.File;
import me.ilyamirin.little.hub.invasion.interaction.cafs.FilePartUpload;
import me.ilyamirin.little.hub.invasion.interaction.cafs.FileVersion;

/**
 *
 * @author ilamirin
 */
@Slf4j
public class SmbSwingWorker extends SwingWorker<Integer, SmbFile> {

    private static final int CHUNK_SIZE = 64000;
    private static final int CHUNK_BUCKET_SIZE = 5;

    private Properties p;
    private CAFSClient client;
    private SessionHolder sessionHolder;

    @Setter
    private SmbFile root;
    @Setter
    private JTextArea textArea;

    private Integer totalFilesBackuped = 0;

    private Properties mediaTypes;

    @Inject
    public SmbSwingWorker(Properties p, CAFSClient client, SessionHolder sessionHolder) {
        this.p = p;
        this.client = client;
        this.sessionHolder = sessionHolder;
    }

    private String getFileMediaType(SmbFile smbFile) {
        if (mediaTypes == null) {
            mediaTypes = new Properties();
            try {
                mediaTypes.load(new FileInputStream("media_types.properties"));
            } catch (IOException e) {
                log.error("Can not load media types", e);
            }
        }
        Pattern exp = Pattern.compile("[^\\.]+$");
        Matcher matcher = exp.matcher(smbFile.getName());
        if (matcher.find()) {
            return mediaTypes.getProperty(matcher.group(0), "application/octet-stream");
        } else {
            return null;
        }
    }

    private void sliceAndSendChunk(SmbFile smbFile, String pathTo, String targetId) throws SmbException, IOException {
        log.info("File {} has been found.", smbFile.getPath());

        String smbFileCacheKey = smbFile.getPath().concat(pathTo).concat(targetId);
        /*if (cache.contains(smbFileCacheKey)) {
            log.trace("File {} has been already processed at {}.",
                    smbFile, cache.get(smbFileCacheKey, Date.class));
            return;
        }*/

        //Upload chunks

        String fileVersionKey = String.valueOf(smbFile.getPath().concat(new Date().toString()).hashCode());
        SmbFileInputStream in = (SmbFileInputStream) smbFile.getInputStream();
        Collection<FilePartUpload> filePartUploads = new HashSet<FilePartUpload>();

        byte[] chunk;
        int totalChunksCount = 0;

        for (int cursor = 0; cursor < smbFile.length(); cursor += CHUNK_SIZE) {
            if (smbFile.length() - cursor < CHUNK_SIZE) {
                chunk = new byte[(int) (smbFile.length() - cursor)];
            } else {
                chunk = new byte[CHUNK_SIZE];
            }
            log.trace("I intend to read chunk with size {}", chunk.length);
            in.read(chunk);

            FilePartUpload partUpload = new FilePartUpload();
            partUpload.setIndex(totalChunksCount);
            partUpload.setFileVersionKey(fileVersionKey);
            partUpload.setContent(chunk);
            filePartUploads.add(partUpload);

            totalChunksCount++;

            if (filePartUploads.size() >= CHUNK_BUCKET_SIZE) {
                client.uploadChunks(filePartUploads, sessionHolder.getSessionId());
                filePartUploads.clear();
            }
        }

        if (!filePartUploads.isEmpty()) {
            client.uploadChunks(filePartUploads, sessionHolder.getSessionId());
        }

        in.close();

        //Creaet file on CAFS

        File file = new File();
        file.setFolder(false);
        file.setMediaType(getFileMediaType(smbFile));
        file.setTargetId(targetId);
        file.setPath(pathTo + smbFile.getName());

        FileVersion fileVersion = new FileVersion();
        fileVersion.setVersionId(System.currentTimeMillis());
        fileVersion.setFile(file);
        fileVersion.setKey(fileVersionKey);
        fileVersion.setLastModificationDate(smbFile.getLastModified());
        fileVersion.setSizeInBytes(smbFile.length());
        fileVersion.setChunksCount(totalChunksCount);
        fileVersion.setHashes(null);

        if (client.createNewFileVersion(fileVersion, sessionHolder.getSessionId())) {
            //cache.put(smbFileCacheKey, new Date());
            totalFilesBackuped++;
            publish(smbFile);
        }
    }

    public void crawlDir(SmbFile root, String pathTo, String targetId, boolean isRoot) throws SmbException, IOException {
        if (root.isDirectory()) {
            log.trace("Directory {} has been found and she wanna {} bytes of a disk space.",
                    root.getPath(), root.length());

            String pathToSmbFiles = isRoot ? pathTo : pathTo + root.getName();
            for (SmbFile smbFile : root.listFiles()) {
                try {
                    crawlDir(smbFile, pathToSmbFiles, targetId, false);
                } catch (Exception e) {
                    log.error("Oops!", e);
                }
            }
        } else {
            sliceAndSendChunk(root, pathTo, targetId);
        }
    }//process

    @Override
    protected Integer doInBackground() throws Exception {
        crawlDir(root, "/", p.getProperty("targetId"), true);
        return totalFilesBackuped;
    }

    @Override
    protected void process(List<SmbFile> files) {
        for (SmbFile file : files) {
            textArea.append("+".concat(file.getPath().concat("\n")));
        }
    }
}
