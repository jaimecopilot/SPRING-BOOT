package es.mecd.demo.miproyecto.jwt;
import java.nio.charset.StandardCharsets; import java.security.MessageDigest; import java.util.Base64;
import javax.crypto.Mac; import javax.crypto.spec.SecretKeySpec;
/** Laboratorio manual de JWT del punto 6.5; no usa JJWT. */
public final class JwtManual {
 private JwtManual(){}
 public static String base64Url(byte[] b){return Base64.getUrlEncoder().withoutPadding().encodeToString(b);}
 public static String firmar(String headerJson,String payloadJson,String secret){try{String
     h=base64Url(headerJson.getBytes(StandardCharsets.UTF_8));String
     p=base64Url(payloadJson.getBytes(StandardCharsets.UTF_8));String input=h+"."+p;Mac
     mac=Mac.getInstance("HmacSHA256");mac.init(new
     SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),"HmacSHA256"));return
     input+"."+base64Url(mac.doFinal(input.getBytes(StandardCharsets.UTF_8)));}catch(Exception e){throw
     new IllegalStateException(e);}}
 public static boolean verificar(String token,String secret){try{String[] x=token.split("\\.");
     if(x.length!=3)return false; String input=x[0]+"."+x[1];Mac mac=Mac.getInstance("HmacSHA256");
     mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),"HmacSHA256"));return
     MessageDigest.isEqual(Base64.getUrlDecoder().decode(x[2]),
     mac.doFinal(input.getBytes(StandardCharsets.UTF_8)));}catch(Exception e){return false;}}
 public static String decodificarPayload(String token){String[] x=token.split("\\.");return new
     String(Base64.getUrlDecoder().decode(x[1]),StandardCharsets.UTF_8);}
 public static void main(String[] args){if(args.length>0)System.out.println(decodificarPayload(args[0]));}
}
