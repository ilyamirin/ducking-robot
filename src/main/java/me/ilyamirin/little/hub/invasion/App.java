package me.ilyamirin.little.hub.invasion;

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

        CAFSClient client = CAFSClient.build();
        Cache cache = new Cache();
        ConsoleClient authClient = new ConsoleClient(properties, new XStream(new StaxDriver()));
        SessionHolder sessionHolder = new SessionHolder(properties, authClient, cache);

        if (args[1].equals("backup")) {
            log.info("Backup has been started.");

            /*
            log.info("Start clearing of broken versions.");
            List<FileVersionIdentifier> brokenVersions = client.getBrokenVersions(
                    authClient.startSessionForTarget(properties.getProperty("targetId")),
                    properties.getProperty("targetId"));
            log.info("Finish clearing of {} broken versions.", brokenVersions.size());
            */
            
            SmbProcessor processor = new SmbProcessor(client, sessionHolder, cache);
            SmbFile root = new SmbFile(properties.getProperty("path"));

            log.info("Start backup of {}", root);
            processor.process(root, "/", properties.getProperty("targetId"), true);
            log.info("Finish backup of {}", root);

        } else if (args[1].equals("restore")) {
            log.info("Restoring has been started.");
        }
    }
}
