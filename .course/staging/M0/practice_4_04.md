    private String construirMensaje(String destinatario) {
        return "Hola, " + destinatario;
    }
}
```

## `application.properties`

```properties
spring.application.name=mi-proyecto
```

## `SaludoControllerTest.java`

```java
package es.mecd.demo.miproyecto.controller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SaludoControllerTest {

    private final SaludoController controller = new SaludoController();

    @Test
    void saludarDebeDevolverElMensajeEsperado() {
        assertThat(controller.saludar())
                .isEqualTo("Hola, Ministerio de Educación");
    }

    @Test
    void despedirDebeDevolverElMensajeEsperado() {
        assertThat(controller.despedir())
                .isEqualTo("Adiós, Ministerio de Educación");
    }
}
```

# Puente al Módulo 1

M0 termina con una aplicación real pero muy pequeña. Ya sabemos **hacerla funcionar** y observarla. El Módulo 1 empezará a preguntar **por qué funciona**: qué ha auto-configurado Spring Boot, cómo se relacionan cliente y servidor, qué significa realmente una petición HTTP, cómo entran JSON y Jackson y cómo diseñar una API REST con criterio.
