package com.nscaled.nanopod;

import com.google.inject.Guice;
import com.google.inject.Injector;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Properties;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import lombok.extern.slf4j.Slf4j;
import com.nscaled.nanopod.clients.CAFSClient;
import com.nscaled.nanopod.clients.ConsoleClient;
import com.nscaled.nanopod.interaction.cafs.File;
import com.nscaled.nanopod.interaction.cafs.FileVersion;
import com.nscaled.nanopod.interaction.cafs.FileVersionIdentifier;

@Slf4j
public class App {

    public static void main(String[] args) throws Exception {
        Properties p = new Properties();
        p.load(new FileInputStream(args[0]));

        log.info("Properties has been loaded: {}", p);

        MediaTypeResolver resolver = new MediaTypeResolver();
        resolver.loadMediaTypes(args[1]);

        Injector injector = Guice.createInjector(new NanoPodModule(p, resolver));

        String command = args[2];

        if (command.equals("backup")) {
            log.info("Backup has been started.");

            SmbProcessor processor = injector.getInstance(SmbProcessor.class);
            SmbFile root = new SmbFile(p.getProperty("path"));

            log.info("Start backup of {}", root);
            processor.process(root, "/", p.getProperty("targetId"), true);
            log.info("Finish backup of {}", root);

        } else if (command.equals("restore")) {
            log.info("Restoring has been started.");

        } else if (command.equals("check_broken")) {
            log.info("Load broken versions.");

            String targetId = p.getProperty("targetId");

            ConsoleClient consoleClient = injector.getInstance(ConsoleClient.class);
            String sessionId = consoleClient.startSessionForTarget(targetId);

            CAFSClient client = injector.getInstance(CAFSClient.class);

            List<FileVersionIdentifier> brokenVersions = client.getBrokenVersions(sessionId, targetId);

            log.info("Finish loading of {} broken versions.", brokenVersions.size());

            for (FileVersionIdentifier brokenVersion : brokenVersions) {
                File file = new File();
                file.setTargetId(brokenVersion.getTargetId());
                file.setPath(brokenVersion.getPath());

                FileVersion fv = new FileVersion();
                fv.setVersionId(brokenVersion.getVersionId());
                fv.setFile(file);

                client.removeBrokenVersionsFromCafsRegistry(sessionId, fv);
            }
        }
    }//main
}
