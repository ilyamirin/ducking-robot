package me.ilyamirin.little.hub.invasion;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

    public static void main(String[] args) throws Exception {
        CAFSClient client = CAFSClient.build();
        SessionHolder sessionHolder = new SessionHolder(args[2]);
        CIFSShareProcessor processor = new CIFSShareProcessor(client, sessionHolder);
        processor.process(args[0], args[1]);
    }
}
