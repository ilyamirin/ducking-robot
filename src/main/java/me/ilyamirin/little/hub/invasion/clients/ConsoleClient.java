package me.ilyamirin.little.hub.invasion.clients;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.thoughtworks.xstream.XStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.ws.rs.core.MediaType;
import jcifs.util.Base64;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ilyamirin.little.hub.invasion.models.AuthSessionRequest;
import me.ilyamirin.little.hub.invasion.models.AuthSessionResponse;
import me.ilyamirin.little.hub.invasion.models.AuthSessionTypeEnum;

@Slf4j
@AllArgsConstructor
public class ConsoleClient {

    private static final String consoleBaseUrl = "http://localhost";
	private static final String AUTHENTICATION_URL = "/service/pod_service/auth_session";
	private static final String AUTHENTICATION_CHECK_URL = "/service/cafs_service/auth_session_validate";
    private String password;
    private String uuid;
    private XStream xs;

	String generateToken() {
		StringBuilder tokenComponents = new StringBuilder();
		tokenComponents.append(uuid);
		tokenComponents.append(password);
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

    public String startSession(String targetId) {
        log.info("Asking console about a session for me.");
        AuthSessionRequest request = new AuthSessionRequest();
        request.setSessionType(AuthSessionTypeEnum.HUB);
        request.setToken(generateToken());
        request.setUuid(uuid);
        request.setCorrelationId(targetId);

        Client client = Client.create();

        ClientResponse response = client
                .resource(consoleBaseUrl)
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