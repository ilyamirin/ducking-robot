package me.ilyamirin.little.hub.invasion;

import me.ilyamirin.little.hub.invasion.clients.CAFSClient;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.ilyamirin.little.hub.invasion.models.File;
import me.ilyamirin.little.hub.invasion.models.FilePartUpload;
import me.ilyamirin.little.hub.invasion.models.FileVersion;

/**
 *
 * @author ilamirin
 */
@Slf4j
@AllArgsConstructor
public class SmbProcessor {

    private static final int CHUNK_SIZE = 64000;
    private static final int CHUNK_BUCKET_SIZE = 5;
    private CAFSClient client;
    private SessionHolder sessionHolder;

    private void processFile(SmbFile smbFile, String pathTo, String targetId) throws SmbException, IOException {
        log.info("File {} has been found and he wanna {} bytes of disk space.",
                smbFile.getName(), smbFile.length());

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

        client.createNewFileVersion(fileVersion, sessionHolder.getSessionId());
    }

    public void process(SmbFile root, String pathTo, String targetId, boolean isRoot) throws SmbException, IOException {
        if (root.isDirectory()) {
            log.info("Directory {} has been found and she wanna {} bytes of a disk space.",
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
