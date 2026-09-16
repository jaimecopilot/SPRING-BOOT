package es.mecd.demo.miproyecto.jwt;
import static org.junit.jupiter.api.Assertions.*; import org.junit.jupiter.api.Test;
class JwtManualTest { @Test void firmaYDetectaManipulacion(){String
    t=JwtManual.firmar("{\"alg\":\"HS256\",\"typ\":\"JWT\"}","{\"sub\":\"ana\"}",
    "secreto-de-laboratorio-1234567890");assertTrue(JwtManual.verificar(t,
    "secreto-de-laboratorio-1234567890"));String[] x=t.split("\\.");String
    adulterado=x[0]+"."+JwtManual.base64Url("{\"sub\":\"admin\"}".getBytes())+"."+x[2];
    assertFalse(JwtManual.verificar(adulterado,"secreto-de-laboratorio-1234567890"));} }
