package com.nscaled.nanopod.clients;

import com.google.inject.Inject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.thoughtworks.xstream.XStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import javax.ws.rs.core.MediaType;
import jcifs.util.Base64;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.nscaled.nanopod.interaction.console.AuthSessionRequest;
import com.nscaled.nanopod.interaction.console.AuthSessionResponse;
import com.nscaled.nanopod.interaction.console.AuthSessionTypeEnum;

@Slf4j
public class ConsoleClient {

    private Properties properties;
    private XStream xs;

	private static final String AUTHENTICATION_URL = "service/pod_service/auth_session";

    @Inject
    public ConsoleClient(Properties properties, XStream xs) {
        this.properties = properties;
        this.xs = xs;
    }

	String generateToken() {
		StringBuilder tokenComponents = new StringBuilder();
		tokenComponents.append(properties.get("uuid"));
		tokenComponents.append(properties.get("password"));
		try {
			byte[]  bytesOfMessage = tokenComponents.toString().getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(bytesOfMessage);

			return Base64.encode(thedigest);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

    public String startSessionForTarget(String targetId) {
        log.info("Asking console about a session for target {}.", targetId);
        AuthSessionRequest request = new AuthSessionRequest();
        request.setSessionType(AuthSessionTypeEnum.HUB);
        request.setToken(generateToken());
        request.setUuid(properties.getProperty("uuid"));
        request.setCorrelationId(targetId);

        Client client = Client.create();

        ClientResponse response = client
                .resource(properties.getProperty("console.url"))
                .path(AUTHENTICATION_URL)
                .accept(MediaType.APPLICATION_XML_TYPE)
                .post(ClientResponse.class, request);
        xs.processAnnotations(AuthSessionResponse.class);
        AuthSessionResponse sessionResponse =
                (AuthSessionResponse) xs.fromXML(response.getEntity(String.class));
        log.info("Session with id: {} has been recieved.", sessionResponse.getSessionId());
        return sessionResponse.getSessionId();
    }
}