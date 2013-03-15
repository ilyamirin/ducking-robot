package me.ilyamirin.little.hub.invasion;

import com.google.inject.Guice;
import com.google.inject.Injector;
import me.ilyamirin.little.hub.invasion.cache.Cache;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Properties;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import me.ilyamirin.little.hub.invasion.clients.CAFSClient;
import lombok.extern.slf4j.Slf4j;
import me.ilyamirin.little.hub.invasion.clients.ConsoleClient;
import me.ilyamirin.little.hub.invasion.interaction.cafs.FileVersionIdentifier;

@Slf4j
public class App {

    public static void processShare(Properties p, Goal goal) throws MalformedURLException, SmbException, IOException {
        Injector injector = Guice.createInjector(new NanoPodModule(p));

        if (goal.equals(Goal.BACKUP)) {
            log.info("Backup has been started.");

            /*
            log.info("Start clearing of broken versions.");
            List<FileVersionIdentifier> brokenVersions = client.getBrokenVersions(
                    authClient.startSessionForTarget(properties.getProperty("targetId")),
                    properties.getProperty("targetId"));
            log.info("Finish clearing of {} broken versions.", brokenVersions.size());
            */

            SmbProcessor processor = injector.getInstance(SmbProcessor.class);
            SmbFile root = new SmbFile(p.getProperty("path"));

            log.info("Start backup of {}", root);
            processor.process(root, "/", p.getProperty("targetId"), true);
            log.info("Finish backup of {}", root);

        } else if (goal.equals(Goal.RESTORE)) {
            log.info("Restoring has been started.");
        }
    }

    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.load(new FileInputStream(args[0]));

        log.info("Properties has been loaded: {}", properties);

        processShare(properties, Goal.valueOf(args[1]));
    }
}
