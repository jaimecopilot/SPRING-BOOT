package es.mecd.demo.miproyecto.jwt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Laboratorio ejecutable del punto 6.5. Implementa un JWT HS256 con Java estándar
 * para observar header, payload y firma antes de introducir JJWT en 6.6.
 */
public final class JwtManual {
    public static final String DIDACTIC_SECRET =
            "claveSecretaMuyLargaParaHMACSHA256ConAlMenos32Bytes";

    private JwtManual() {
    }

    public static String base64Url(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public static String firmar(String headerJson, String payloadJson, String secret) {
        String header = base64Url(headerJson.getBytes(StandardCharsets.UTF_8));
        String payload = base64Url(payloadJson.getBytes(StandardCharsets.UTF_8));
        String firmaInput = header + "." + payload;
        return firmaInput + "." + firmarHmacSha256(firmaInput, secret);
    }

    public static boolean verificar(String token, String secret) {
        try {
            String[] partes = token.split("\\.", -1);
            if (partes.length != 3) {
                return false;
            }
            String firmaInput = partes[0] + "." + partes[1];
            byte[] esperada = Base64.getUrlDecoder().decode(
                    firmarHmacSha256(firmaInput, secret));
            byte[] recibida = Base64.getUrlDecoder().decode(partes[2]);
            return MessageDigest.isEqual(esperada, recibida);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static String decodificarPayload(String token) {
        String[] partes = token.split("\\.", -1);
        if (partes.length != 3) {
            throw new IllegalArgumentException("Token mal formado");
        }
        byte[] decoded = Base64.getUrlDecoder().decode(partes[1]);
        return new String(decoded, StandardCharsets.UTF_8);
    }

    /** Crea un token adulterado conservando deliberadamente la firma original. */
    public static String adulterarPayload(String token, String nuevoPayloadJson) {
        String[] partes = token.split("\\.", -1);
        if (partes.length != 3) {
            throw new IllegalArgumentException("Token mal formado");
        }
        String nuevoPayload = base64Url(nuevoPayloadJson.getBytes(StandardCharsets.UTF_8));
        return partes[0] + "." + nuevoPayload + "." + partes[2];
    }

    private static String firmarHmacSha256(String input, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return base64Url(mac.doFinal(input.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            throw new IllegalStateException("No se pudo calcular HMAC-SHA256", ex);
        }
    }

    public static void main(String[] args) {
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payload = "{\"sub\":\"ana\",\"email\":\"ana@educacion.gob.es\","
                + "\"roles\":[\"ROLE_USER\"],\"iat\":1516239022}";

        String jwt = firmar(header, payload, DIDACTIC_SECRET);
        System.out.println("JWT generado:");
        System.out.println(jwt);
        System.out.println("Token válido: " + verificar(jwt, DIDACTIC_SECRET));

        String payloadAdulterado = "{\"sub\":\"pedro\",\"email\":\"ana@educacion.gob.es\","
                + "\"roles\":[\"ROLE_USER\"],\"iat\":1516239022}";
        String jwtAdulterado = adulterarPayload(jwt, payloadAdulterado);
        System.out.println("Token modificado válido: "
                + verificar(jwtAdulterado, DIDACTIC_SECRET));
        System.out.println("Con clave incorrecta: " + verificar(jwt, "otraClave"));
        System.out.println("Payload decodificado: " + decodificarPayload(jwt));
    }
}
