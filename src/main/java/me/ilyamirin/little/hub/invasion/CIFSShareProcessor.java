package me.ilyamirin.little.hub.invasion;

import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
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
public class CIFSShareProcessor {

    private static final int CHUNK_SIZE = 64000;
    private static final int CHUNK_BUCKET_SIZE = 5;
    private CAFSClient client;
    private SessionHolder sessionHolder;

    //@SneakyThrows
    public void process(String shareRootUrl, String targetId) throws Exception {
        SmbFile root = new SmbFile(shareRootUrl);
        for (SmbFile smbFile : root.listFiles()) {
            log.info("File {} wanna {} bytes of disk space.", smbFile.getName(), smbFile.length());

            String fileVersionKey = String.valueOf(smbFile.hashCode());
            SmbFileInputStream in = (SmbFileInputStream) smbFile.getInputStream();
            Collection<FilePartUpload> filePartUploads = new HashSet<FilePartUpload>();
            byte[] b;
            int inBucket = 0;
            int chunksCount = 0;

            for (int tot = 0; tot < smbFile.length(); tot += CHUNK_SIZE) {
                inBucket++;
                chunksCount++;

                if (smbFile.length() - tot < CHUNK_SIZE) {
                    b = new byte[(int) (smbFile.length() - tot)];
                } else {
                    b = new byte[CHUNK_SIZE];
                }

                log.info("Chunk size {}", b.length);

                in.read(b);

                FilePartUpload partUpload = new FilePartUpload();
                partUpload.setIndex(chunksCount);
                partUpload.setFileVersionKey(fileVersionKey);
                partUpload.setContent(b);

                filePartUploads.add(partUpload);

                if (inBucket >= CHUNK_BUCKET_SIZE) {
                    client.uploadChunks(filePartUploads, sessionHolder.getSessionId());
                    filePartUploads.clear();
                }
            }

            if (!filePartUploads.isEmpty()) {
                client.uploadChunks(filePartUploads, sessionHolder.getSessionId());
            }

            in.close();

            File file = new File();
            file.setFolder(false);
            //TODO: media type?
            //file.setMediaType(smbFile.);
            file.setTargetId(targetId);
            file.setPath("/" + smbFile.getName());

            FileVersion fileVersion = new FileVersion();
            //TODO:: Last mod date WTF???
            fileVersion.setVersionId(System.currentTimeMillis());
            fileVersion.setFile(file);
            fileVersion.setKey(fileVersionKey);
            fileVersion.setLastModificationDate(smbFile.getLastModified());
            //WTF problem in cafs
            fileVersion.setSizeInBytes(smbFile.length());
            fileVersion.setChunksCount(chunksCount);
            //WTF why problem on CAFS?
            fileVersion.setHashes(null);

            client.createNewFileVersion(fileVersion, sessionHolder.getSessionId());
        }//for SMB files
    }//process
}
