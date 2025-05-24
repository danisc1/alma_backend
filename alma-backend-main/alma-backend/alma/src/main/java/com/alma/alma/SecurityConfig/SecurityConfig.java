package com.alma.alma.SecurityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://127.0.0.1:5500"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults()) 
            .csrf().disable() 
            .authorizeHttpRequests(auth -> auth
                // Permite acesso público para cadastro e login
                .antMatchers("/api/usuarios/cadastro", "/api/usuarios/login", "/uploads/**").permitAll()
                // Permite acesso do psicólogo para listar usuários
                .antMatchers("/api/usuarios/listar").hasRole("PSICOLOGO") // <<< ADICIONADO/AJUSTADO
                // Permite acesso do psicólogo para listar humor de paciente específico
                .antMatchers("/api/humor/usuario/**").hasRole("PSICOLOGO") // <<< ADICIONADO/AJUSTADO
                // Permite acesso do paciente para seus próprios registros de humor e upload de foto
                .antMatchers("/api/humor/meus-registros", "/api/humor/registros", "/api/usuarios/upload-foto", "/api/usuarios/logado").hasRole("PACIENTE") // <<< ADICIONADO/AJUSTADO
                // Qualquer outra requisição exige autenticação (pelo menos logado)
                .anyRequest().authenticated() 
            )
            .sessionManagement().maximumSessions(1); 

        return http.build();
    }
}