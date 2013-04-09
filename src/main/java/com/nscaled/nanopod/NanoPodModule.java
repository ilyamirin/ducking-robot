package com.nscaled.nanopod;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.io.IOException;
import java.util.Properties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import com.nscaled.nanopod.clients.CAFSClient;

/**
 *
 * @author ilamirin
 */
@RequiredArgsConstructor
public class NanoPodModule extends AbstractModule {

    @NonNull
    private Properties p;
    @NonNull
    private MediaTypeResolver mediaTypeResolver;

    @Override
    protected void configure() {
        bind(MediaTypeResolver.class).toInstance(mediaTypeResolver);
        bind(Properties.class).toInstance(p);
        bind(CAFSClient.class).toInstance(CAFSClient.build(p));
        bind(XStream.class).toInstance(new XStream(new StaxDriver()));
    }

}
