package me.ilyamirin.little.hub.invasion;

import com.google.inject.Inject;
import jcifs.smb.SmbFile;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ilamirin
 */
@Slf4j
public class SmbCrawler implements Runnable {

    private SmbProcessor smbProcessor;
    private SmbFile root;
    private String pathTo;
    private String targetId;
    private boolean isRoot;

    @Inject
    public SmbCrawler(SmbProcessor smbProcessor, SmbFile root, String pathTo, String targetId, boolean isRoot) {
        this.smbProcessor = smbProcessor;
        this.root = root;
        this.pathTo = pathTo;
        this.targetId = targetId;
        this.isRoot = isRoot;
    }

    public void run() {
        try {
            smbProcessor.process(root, pathTo, targetId, isRoot);
        } catch (Exception ex) {
            log.error("Oops!", ex);
        }
    }

}
