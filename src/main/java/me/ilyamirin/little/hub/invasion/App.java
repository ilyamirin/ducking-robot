package me.ilyamirin.little.hub.invasion;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.util.List;
import jcifs.smb.SmbFile;
import me.ilyamirin.little.hub.invasion.clients.CAFSClient;
import lombok.extern.slf4j.Slf4j;
import me.ilyamirin.little.hub.invasion.clients.ConsoleClient;
import me.ilyamirin.little.hub.invasion.interaction.cafs.FileVersionIdentifier;

@Slf4j
public class App {

    public static void main(String[] args) throws Exception {
        String uuid = null;
        String password = null;
        String targetId = null;
        String action = null;
        String pathTo = null;
        String consoleLocation = null;

        for (int i = 0; i < args.length; i += 2) {
            if (args[i].equals("--uuid")) {
                uuid = args[i + 1];
            } else if (args[i].equals("--password")) {
                password = args[i + 1];
            } else if (args[i].equals("--target")) {
                targetId = args[i + 1];
            } else if (args[i].equals("--path")) {
                pathTo = args[i + 1];
            } else if (args[i].equals("--console")) {
                consoleLocation = args[i + 1];
            } else if (args[i].equals("--action")) {
                action = args[i + 1];
            }
        }

        CAFSClient client = CAFSClient.build();
        ConsoleClient authClient = new ConsoleClient(password, uuid, new XStream(new StaxDriver()));
        SessionHolder sessionHolder = new SessionHolder(targetId, authClient);

        if (action.equals("backup")) {
            log.info("Backup has been started.");

            log.info("Start clearing of broken versions.");
            List<FileVersionIdentifier> brokenVersions =
                     client.getBrokenVersions(authClient.startSession(targetId), targetId);
            log.info("Finish clearing of {} broken versions.", brokenVersions.size());

            SmbProcessor processor = new SmbProcessor(client, sessionHolder);
            SmbFile root = new SmbFile(pathTo);
            log.info("Start backup of {}", root);
            processor.process(root, "/", targetId, true);
            log.info("Finish backup of {}", root);

        } else if (action.equals("restore")) {
            log.info("Restoring has been started.");
        }
    }
}
