package me.ilyamirin.little.hub.invasion;

import com.google.inject.Guice;
import com.google.inject.Injector;
import me.ilyamirin.little.hub.invasion.cache.Cache;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import jcifs.smb.SmbFile;
import me.ilyamirin.little.hub.invasion.clients.CAFSClient;
import lombok.extern.slf4j.Slf4j;
import me.ilyamirin.little.hub.invasion.clients.ConsoleClient;
import me.ilyamirin.little.hub.invasion.interaction.cafs.FileVersionIdentifier;

@Slf4j
public class App {

    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.load(new FileInputStream(args[0]));

        log.info("Properties has been loaded: {}", properties);

        Injector injector = Guice.createInjector(new NanoPodModule(properties));

        if (args[1].equals("backup")) {
            log.info("Backup has been started.");

            /*
            log.info("Start clearing of broken versions.");
            List<FileVersionIdentifier> brokenVersions = client.getBrokenVersions(
                    authClient.startSessionForTarget(properties.getProperty("targetId")),
                    properties.getProperty("targetId"));
            log.info("Finish clearing of {} broken versions.", brokenVersions.size());
            */

            SmbProcessor processor = injector.getInstance(SmbProcessor.class);
            SmbFile root = new SmbFile(properties.getProperty("path"));

            log.info("Start backup of {}", root);
            processor.process(root, "/", properties.getProperty("targetId"), true);
            log.info("Finish backup of {}", root);

        } else if (args[1].equals("restore")) {
            log.info("Restoring has been started.");
        }
    }
}
