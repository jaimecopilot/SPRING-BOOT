package es.mecd.demo.miproyecto.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.List;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    public static final String BEARER_AUTH = "bearerAuth";

    @Bean
    public OpenAPI apiBecasOpenAPI() {
        SecurityScheme bearer = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
        return new OpenAPI()
                .components(new Components().addSecuritySchemes(BEARER_AUTH, bearer))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH));
    }

    @Bean
    public OpenApiCustomizer publicEndpointsWithoutSecurity() {
        return openApi -> {
            clearPostSecurity(openApi, "/api/v1/auth/registro");
            clearPostSecurity(openApi, "/api/v1/auth/login");
            clearPostSecurity(openApi, "/api/v1/auth/refresh");
            clearGetSecurity(openApi, "/api/v1/becas");
            clearGetSecurity(openApi, "/api/v1/becas/{id}");
        };
    }

    private void clearPostSecurity(OpenAPI openApi, String path) {
        PathItem item = openApi.getPaths() == null ? null : openApi.getPaths().get(path);
        if (item != null && item.getPost() != null) item.getPost().setSecurity(List.of());
    }

    private void clearGetSecurity(OpenAPI openApi, String path) {
        PathItem item = openApi.getPaths() == null ? null : openApi.getPaths().get(path);
        if (item != null && item.getGet() != null) item.getGet().setSecurity(List.of());
    }
}
