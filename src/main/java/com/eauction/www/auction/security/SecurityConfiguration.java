package com.eauction.www.auction.security;

import com.eauction.www.auction.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtFilter jwtFilter;

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//                .cors(cors -> {})
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        // Public endpoints
//                        .requestMatchers("/authenticate", "/registration", "/auction/*/result", "/auctions").permitAll()
//
//                        // Admin endpoints
//                        .requestMatchers("/admins/**").hasRole("ADMIN")
//
//                        // User + Admin endpoints
//                        .requestMatchers("/users/**", "/useradmin/**").hasAnyRole("USER", "ADMIN")
//
//                        // All others
//                        .anyRequest().authenticated()
//                )
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                );
//
//        // Add JWT filter before UsernamePasswordAuthenticationFilter
//        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//
//        // 1. Origins (Ensure these match your browser URL exactly)
//        config.setAllowedOrigins(List.of(
//                "http://localhost:3000",
//                "http://localhost:4200",
//                "http://localhost:5173"
//        ));
//
//        // 2. Methods
//        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//
//        // 3. Allowed Headers (Add common headers used by fetch/axios)
//        config.setAllowedHeaders(List.of(
//                "Authorization",
//                "Content-Type",
//                "Accept",
//                "Origin",
//                "X-Requested-With"
//        ));
//
//        // 4. Exposed Headers (Crucial if you send JWT back in headers)
//        config.setExposedHeaders(List.of("Authorization"));
//
//        // 5. Credentials (Required for cookies or Authorization headers)
//        config.setAllowCredentials(true);
//
//        // 6. Max Age (Tells browser to cache this "permission" for 1 hour)
//        config.setMaxAge(3600L);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {})
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // 1. Keep the "Open Doors" here (No login required)
                        .requestMatchers("/authenticate", "/registration").permitAll()

                        // 2. Everything else just requires a VALID JWT
                        // The specific "Role" check happens inside the Controller
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // Add JWT filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Authentication Manager (required for login/authenticate API)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Password Encoder (SECURE)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}