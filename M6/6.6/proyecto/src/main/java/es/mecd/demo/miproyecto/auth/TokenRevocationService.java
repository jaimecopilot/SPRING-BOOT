package es.mecd.demo.miproyecto.auth;
import java.time.Instant; import java.util.Map; import java.util.concurrent.ConcurrentHashMap; import
    org.springframework.stereotype.Service;
@Service public class TokenRevocationService { private final Map<String,Instant> revokedAccess=new
    ConcurrentHashMap<>(), consumedRefresh=new ConcurrentHashMap<>();
 public void revokeAccess(String jti,Instant exp){purgeExpired();revokedAccess.put(jti,exp);} public
     boolean isAccessRevoked(String jti){purgeExpired();return revokedAccess.containsKey(jti);} public
     boolean consumeRefresh(String jti,Instant exp){purgeExpired();return
     consumedRefresh.putIfAbsent(jti,exp)==null;} public boolean isRefreshConsumed(String jti){
     purgeExpired();return consumedRefresh.containsKey(jti);} private void purgeExpired(){Instant
     now=Instant.now();revokedAccess.entrySet().removeIf(e->e.getValue().isBefore(now));
     consumedRefresh.entrySet().removeIf(e->e.getValue().isBefore(now));} }
