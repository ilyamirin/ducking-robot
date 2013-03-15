package me.ilyamirin.little.hub.invasion;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.ilyamirin.little.hub.invasion.clients.ConsoleClient;

/**
 *
 * @author ilamirin
 */
@Data
@RequiredArgsConstructor
public class SessionHolder {

    @NonNull
    private String targetId;
    @NonNull
    private ConsoleClient client;
    private String sessionId;

    public String getSessionId() {
        if (sessionId == null || sessionId.isEmpty()) {
            sessionId = client.startSession(targetId);
        }
        return sessionId;
    }
}
