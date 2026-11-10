
package com.fidness.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth

                // Recursos públicos
                .requestMatchers(
                        "/login",
                        "/registro",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/webjars/**"
                ).permitAll()

                // Rutas del administrador
                .requestMatchers("/admin/**")
                .hasRole("ADMINISTRADOR")

                // Rutas del entrenador
                .requestMatchers("/entrenador/**")
                .hasRole("ENTRENADOR")

                // Rutas del cliente
                .requestMatchers("/cliente/**")
                .hasRole("CLIENTE")

                // Otras rutas requieren autenticación
                .anyRequest().authenticated()
            )

            .formLogin(form -> form

                // Página personalizada de inicio de sesión
                .loginPage("/login")

                // Procesamiento del formulario
                .loginProcessingUrl("/login")

                // Identificador del usuario
                .usernameParameter("correo")

                // Contraseña
                .passwordParameter("contrasena")

                // Redirección después del inicio de sesión
                .defaultSuccessUrl("/", true)

                // Mensaje de error
                .failureUrl("/login?error")

                .permitAll()
            )

            .logout(logout -> logout

                .logoutUrl("/logout")

                .logoutSuccessUrl("/login?logout")

                .invalidateHttpSession(true)

                .deleteCookies("JSESSIONID")

                .permitAll()
            );

        return http.build();
    }
}
