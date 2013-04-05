package me.ilyamirin.little.hub.invasion;

import com.google.inject.AbstractModule;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.util.Properties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.ilyamirin.little.hub.invasion.clients.CAFSClient;

/**
 *
 * @author ilamirin
 */
@RequiredArgsConstructor
public class NanoPodModule extends AbstractModule {

    @NonNull
    private Properties p;

    @Override
    protected void configure() {
        bind(Properties.class).toInstance(p);
        bind(CAFSClient.class).toInstance(CAFSClient.build(p));
        bind(XStream.class).toInstance(new XStream(new StaxDriver()));
    }

}
