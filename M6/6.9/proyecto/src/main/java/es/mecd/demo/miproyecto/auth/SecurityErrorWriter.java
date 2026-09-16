package es.mecd.demo.miproyecto.auth;
import com.fasterxml.jackson.databind.ObjectMapper; import jakarta.servlet.http.*; import
    java.io.IOException; import java.time.Instant; import java.util.Map; import
    org.springframework.http.MediaType; import org.springframework.stereotype.Component;
@Component public class SecurityErrorWriter { private final ObjectMapper mapper; public
    SecurityErrorWriter(ObjectMapper m){mapper=m;} public void write(HttpServletRequest req,
    HttpServletResponse res,int status,String codigo,String mensaje)throws IOException{
    res.setStatus(status);res.setContentType(MediaType.APPLICATION_JSON_VALUE);
    res.setCharacterEncoding("UTF-8");mapper.writeValue(res.getWriter(),Map.of("timestamp",Instant.now()
    .toString(),"status",status,"codigo",codigo,"mensaje",mensaje,"path",req.getRequestURI()));} }
