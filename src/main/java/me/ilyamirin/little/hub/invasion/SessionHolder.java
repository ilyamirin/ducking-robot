package me.ilyamirin.little.hub.invasion;

import java.util.Properties;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import me.ilyamirin.little.hub.invasion.clients.ConsoleClient;

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

    private String sessionId;

    @Synchronized
    public String getSessionId() {
        if (sessionId == null || sessionId.isEmpty()) {
            sessionId = client.startSessionForTarget(properties.getProperty("targetId"));
        }
        return sessionId;
    }
}
