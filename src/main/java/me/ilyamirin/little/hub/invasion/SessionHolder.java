package me.ilyamirin.little.hub.invasion;

import java.util.Properties;
import java.util.SortedMap;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import me.ilyamirin.little.hub.invasion.clients.ConsoleClient;
import org.apache.jdbm.DB;
import org.apache.jdbm.DBMaker;

/**
 *
 * @author ilamirin
 */
@Data
@RequiredArgsConstructor
public class SessionHolder {

    @NonNull
    private Properties properties;
    @NonNull
    private ConsoleClient client;
    @NonNull
    private Cache cache;

    public String getSessionId() {
        String sessionId = cache.get("sessionId", String.class);
        if (sessionId == null || sessionId.isEmpty()) {
            sessionId = client.startSessionForTarget(properties.getProperty("targetId"));
            cache.put("sessionId", sessionId);
        }
        return sessionId;
    }
}
