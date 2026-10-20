package pro.itfray.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Registers the {@link OpenAPI}. */
@NullMarked
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class OpenApiConfig {

  /** Configures OpenAPI to include a security scheme for JWT-based authentication. */
  @Bean
  public OpenAPI openApi() {
    return new OpenAPI()
        .components(
            new Components()
                .addSecuritySchemes(
                    "bearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
  }
}
