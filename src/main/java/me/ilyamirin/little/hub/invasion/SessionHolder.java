package me.ilyamirin.little.hub.invasion;

import com.google.inject.Inject;
import me.ilyamirin.little.hub.invasion.cache.Cache;
import java.util.Properties;
import me.ilyamirin.little.hub.invasion.clients.ConsoleClient;

/**
 *
 * @author ilamirin
 */
public class SessionHolder {

    private Properties properties;
    private ConsoleClient client;
    private Cache cache;

    @Inject
    public SessionHolder(Properties properties, ConsoleClient client, Cache cache) {
        this.properties = properties;
        this.client = client;
        this.cache = cache;
    }

    public String getSessionId() {
        String sessionId = cache.get("sessionId", String.class);
        if (sessionId == null || sessionId.isEmpty()) {
            sessionId = client.startSessionForTarget(properties.getProperty("targetId"));
            cache.put("sessionId", sessionId);
        }
        return sessionId;
    }
}
