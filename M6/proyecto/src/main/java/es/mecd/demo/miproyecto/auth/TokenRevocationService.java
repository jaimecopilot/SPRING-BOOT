package es.mecd.demo.miproyecto.auth;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

/**
 * Lista negra/registro de uso en memoria para el ejercicio de logout y rotación.
 *
 * <p>Es adecuada para el curso y un único proceso; producción distribuida
 * requiere almacenamiento compartido y política de revocación explícita.</p>
 */
@Service
public class TokenRevocationService {
    private final Map<String, Instant> revokedAccess = new ConcurrentHashMap<>();
    private final Map<String, Instant> consumedRefresh = new ConcurrentHashMap<>();

    public void revokeAccess(String jti, Instant expiresAt) {
        purgeExpired();
        revokedAccess.put(jti, expiresAt);
    }

    public boolean isAccessRevoked(String jti) {
        purgeExpired();
        return revokedAccess.containsKey(jti);
    }

    public boolean consumeRefresh(String jti, Instant expiresAt) {
        purgeExpired();
        return consumedRefresh.putIfAbsent(jti, expiresAt) == null;
    }

    public boolean isRefreshConsumed(String jti) {
        purgeExpired();
        return consumedRefresh.containsKey(jti);
    }

    private void purgeExpired() {
        Instant now = Instant.now();
        revokedAccess.entrySet().removeIf(entry -> entry.getValue().isBefore(now));
        consumedRefresh.entrySet().removeIf(entry -> entry.getValue().isBefore(now));
    }
}
