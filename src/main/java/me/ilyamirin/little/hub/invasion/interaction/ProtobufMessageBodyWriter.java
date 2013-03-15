package me.ilyamirin.little.hub.invasion.models;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Message;
import lombok.extern.slf4j.Slf4j;

/**
 * getSize is called first that's where the response is serialized and will be
 * returned later
 */
@Slf4j
@Provider
@Produces("application/x-protobuf")
public class ProtobufMessageBodyWriter implements MessageBodyWriter<Message> {

    public boolean isWriteable(Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return Message.class.isAssignableFrom(type);
    }

    public long getSize(Message m, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    public void writeTo(Message m, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
            throws IOException, WebApplicationException {
        entityStream.write(m.toByteArray());
    }
}