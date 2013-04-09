package com.nscaled.nanopod;

import com.google.inject.Inject;
import java.util.Properties;
import com.nscaled.nanopod.clients.ConsoleClient;
import org.joda.time.DateTime;

/**
 *
 * @author ilamirin
 */
public class SessionHolder {

    private Properties properties;
    private ConsoleClient client;
    private String sessionId;
    private DateTime sessionExpiration = DateTime.now();

    @Inject
    public SessionHolder(Properties properties, ConsoleClient client) {
        this.properties = properties;
        this.client = client;
    }

    public String getSessionId() {
        if (sessionId == null || sessionId.isEmpty() || sessionExpiration.isBeforeNow()) {
            sessionId = client.startSessionForTarget(properties.getProperty("targetId"));
            sessionExpiration = DateTime.now().plusMinutes(15);
        }
        return sessionId;
    }
}
