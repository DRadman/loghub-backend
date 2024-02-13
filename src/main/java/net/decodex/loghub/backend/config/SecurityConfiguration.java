package net.decodex.loghub.backend.config;

import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.config.auth.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    public static String[] allowedCors = new String[]{
            "http://localhost:8080",
            "http://localhost:4200",
            "http://localhost",
            "*"
    };

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/actuator", "/actuator/**", "/api-docs",
                            "/api/v1/auth/authenticate", "/api/v1/auth/refreshToken", "/api/v1/auth/register", "/api/v1/auth/username/taken",
                            "/api/v1/invitation/details/*", "/api/v1/invitation/accept/*", "/api/v1/files/public/**",
                            "/api/v1/project/details",
                            "/api/v1/auth/reset-password", "/api/v1/auth/forgot-password",
                            "/api/v1/device/register", "/api/v1/device/start-session/*", "/api/v1/device/update-session/*",
                            "/api/v1/log/capture", "/api/v1/log/message", "/api/v1/log/analytic", "/api/v1/log/vitals",
                            "/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
                            "/error/**", "/error", "/ws", "/ws/**").permitAll();
                    auth.anyRequest().authenticated();

                }
        );
        http.sessionManagement(sessionManagement ->
                sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of(allowedCors));
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "Cookies"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
