package es.mecd.demo.miproyecto.info;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Endpoint público pedagógico del punto 6.1. */
@RestController
@RequestMapping("/api/v1/public")
public class PublicInfoController {
    @GetMapping("/info")
    public Map<String, String> info() {
        return Map.of("modulo", "M6", "seguridad", "activa");
    }
}
