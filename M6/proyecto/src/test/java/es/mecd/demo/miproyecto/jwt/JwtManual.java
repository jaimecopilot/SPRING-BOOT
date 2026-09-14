package es.mecd.demo.miproyecto.jwt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Implementación didáctica mínima para comprender la estructura JWT.
 *
 * <p>No se utiliza como mecanismo productivo de autenticación. El punto 6.6
 * introduce JJWT para generación y validación reales.</p>
 */
public final class JwtManual {
    /** Clave exclusivamente didáctica. No es un secreto de producción. */
    public static final String DIDACTIC_SECRET =
            "claveDidacticaM6HmacSha256ConLongitudSuficiente2026";

    private JwtManual() {
    }

    public static String generar(
            String header,
            String payload,
            String secret) throws Exception {
        String headerBase64 = base64UrlEncode(header);
        String payloadBase64 = base64UrlEncode(payload);
        String firmaInput = headerBase64 + "." + payloadBase64;
        String firma = firmarHmacSha256(firmaInput, secret);
        return firmaInput + "." + firma;
    }

    public static String base64UrlEncode(String input) {
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(
                        input.getBytes(StandardCharsets.UTF_8));
    }

    public static String decodificarPayload(String jwt) {
        String[] partes = jwt.split("\\.", -1);
        if (partes.length != 3) {
            throw new IllegalArgumentException("Token mal formado");
        }

        byte[] decoded = Base64.getUrlDecoder()
                .decode(partes[1]);
        return new String(decoded, StandardCharsets.UTF_8);
    }

    public static boolean verificar(
            String jwt,
            String secret) throws Exception {
        String[] partes = jwt.split("\\.", -1);
        if (partes.length != 3) {
            return false;
        }

        String firmaInput = partes[0] + "." + partes[1];
        String firmaEsperada =
                firmarHmacSha256(firmaInput, secret);

        try {
            byte[] esperada = Base64.getUrlDecoder()
                    .decode(firmaEsperada);
            byte[] recibida = Base64.getUrlDecoder()
                    .decode(partes[2]);

            return MessageDigest.isEqual(
                    esperada,
                    recibida);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    private static String firmarHmacSha256(
            String input,
            String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256");
        mac.init(keySpec);

        byte[] firma = mac.doFinal(
                input.getBytes(StandardCharsets.UTF_8));

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(firma);
    }

    public static void main(String[] args) throws Exception {
        String header =
                "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payload =
                "{\"sub\":\"ana\","
                + "\"roles\":[\"ROLE_USER\"],"
                + "\"iat\":1516239022}";

        String jwt = generar(
                header,
                payload,
                DIDACTIC_SECRET);

        System.out.println("JWT generado:");
        System.out.println(jwt);
        System.out.println("Payload:");
        System.out.println(decodificarPayload(jwt));
        System.out.println(
                "Firma válida: "
                + verificar(jwt, DIDACTIC_SECRET));
    }
}
