package pro.itfray.config;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

/** Spring Security configuration for the application. */
@NullMarked
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(proxyTargetClass = true)
public class SecurityConfig {

  private static final String[] ACTUATOR_REQUEST_MATCHERS = {"/actuator/**"};

  private static final String[] SWAGGER_REQUEST_MATCHERS = {
    "/v3/api-docs/**",
    "/swagger-ui/**",
    "/swagger-resources/**",
    "/documentation/**",
    "/swagger-ui.html",
    "/webjars/**"
  };

  private static final String REALM_CLAIM = "realm_access";
  private static final String ROLES_CLAIM = "roles";

  /**
   * Security filter chain configuration allowing actuator health to be public and enforcing
   * JWT-based authentication for other endpoints.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) {
    http.authorizeHttpRequests(
            auth ->
                auth.requestMatchers(ACTUATOR_REQUEST_MATCHERS)
                    .permitAll()
                    .requestMatchers(SWAGGER_REQUEST_MATCHERS)
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .oauth2ResourceServer(
            configurer ->
                configurer.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .csrf(AbstractHttpConfigurer::disable);

    return http.build();
  }

  /** Converter that maps a Jwt to Spring Security Authentication with granted authorities. */
  @Bean
  public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
    final var jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter());
    return jwtAuthenticationConverter;
  }

  /** Extracts granted authorities from a Keycloak JWT token, including realm roles. */
  @Bean
  public Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
    final var delegate = new JwtGrantedAuthoritiesConverter();
    return jwt -> {
      Collection<GrantedAuthority> grantedAuthorities = delegate.convert(jwt);

      Map<String, Object> realmAccess = jwt.getClaim(REALM_CLAIM);
      if (Objects.isNull(realmAccess)) {
        return grantedAuthorities;
      }

      if (Objects.isNull(realmAccess.get(ROLES_CLAIM))) {
        return grantedAuthorities;
      }

      @SuppressWarnings("unchecked")
      List<String> roles = (List<String>) realmAccess.get(ROLES_CLAIM);

      final List<SimpleGrantedAuthority> keycloakAuthorities =
          roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList();

      grantedAuthorities.addAll(keycloakAuthorities);
      return grantedAuthorities;
    };
  }
}
