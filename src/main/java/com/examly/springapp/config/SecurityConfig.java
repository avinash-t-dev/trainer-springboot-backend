package com.examly.springapp.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

   
    

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).cors().and()
                .authorizeHttpRequests(auth -> auth
                    //.anyRequest().permitAll())
                        .requestMatchers("/api/login",
                                "/api/register","/api/test/login",
                                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/requirement").hasRole("Manager")
                        .requestMatchers(HttpMethod.GET, "/api/requirement/{requirementId}").hasRole("Manager")
                        .requestMatchers(HttpMethod.GET, "/api/requirement/trainer/{trainerId}").hasRole("Manager")
                        .requestMatchers(HttpMethod.GET, "/api/requirement").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/requirement/{requirementId}").hasAnyRole("Manager","Coordinator")
                        .requestMatchers(HttpMethod.DELETE, "/api/requirement/{requirementId}").hasRole("Manager")
                        .requestMatchers(HttpMethod.POST, "/api/trainer").hasRole("Coordinator")
                        .requestMatchers(HttpMethod.GET, "/api/trainer/{trainerId}").hasAnyRole("Manager","Coordinator")
                        .requestMatchers(HttpMethod.GET, "/api/trainer").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/trainer/{trainerId}").hasRole("Coordinator")
                        .requestMatchers(HttpMethod.DELETE, "/api/trainer/{trainerId}").hasRole("Coordinator")
                        .requestMatchers(HttpMethod.POST, "/api/feedback").hasRole("Manager")
                        .requestMatchers(HttpMethod.GET, "/api/feedback/{feedbackId}").hasAnyRole("Manager","Coordinator")
                        .requestMatchers(HttpMethod.GET, "/api/feedback").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/feedback/user/{userId}").hasRole("Manager")
                        .requestMatchers(HttpMethod.DELETE, "/api/feedback/{feedbackId}").hasRole("Manager")
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}




