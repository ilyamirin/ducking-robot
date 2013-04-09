package me.ilyamirin.little.hub.invasion;

import com.google.inject.Inject;
import me.ilyamirin.little.hub.invasion.clients.CAFSClient;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import lombok.extern.slf4j.Slf4j;
import me.ilyamirin.little.hub.invasion.interaction.cafs.File;
import me.ilyamirin.little.hub.invasion.interaction.cafs.FilePartUpload;
import me.ilyamirin.little.hub.invasion.interaction.cafs.FileVersion;

/**
 *
 * @author ilyamirin
 */
@Slf4j
public class SmbProcessor {

    private static final int CHUNK_SIZE = 64000;
    private static final int CHUNK_BUCKET_SIZE = 5;

    private CAFSClient client;
    private SessionHolder sessionHolder;

    @Inject
    public SmbProcessor(CAFSClient client, SessionHolder sessionHolder) {
        this.client = client;
        this.sessionHolder = sessionHolder;
    }

    private void processFile(SmbFile smbFile, String pathTo, String targetId) throws SmbException, IOException {
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
        //TODO: media type?
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
        }
    }

    public void process(SmbFile root, String pathTo, String targetId, boolean isRoot) throws SmbException, IOException {
        if (root.isDirectory()) {
            log.trace("Directory {} has been found and she wanna {} bytes of a disk space.",
                    root.getPath(), root.length());

            String pathToSmbFiles = isRoot ? pathTo : pathTo + root.getName();
            for (SmbFile smbFile : root.listFiles()) {
                try {
                    process(smbFile, pathToSmbFiles, targetId, false);
                } catch (Exception e) {
                    log.error("Oops!", e);
                }
            }
        } else {
            processFile(root, pathTo, targetId);
        }
    }//process
}
