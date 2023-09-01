package com.example.gimnasio.JWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig   {
/*permite configurar las solicitudes con autenticacion y sin autentificacion*/
    @Autowired
    CustomerUsersDetailsService customerUsersDetailsService;

    @Autowired
    private JwtRequestFilter authenticationFilter;
    @Autowired
    JwtAuthenticationEntryPoint unauthorizedHandler;
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customerUsersDetailsService);
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf( csrf -> csrf.disable() )
                .cors( Customizer.withDefaults() )
                .authorizeHttpRequests(  authorize -> authorize
                        .requestMatchers(
                                "/api/auth/**",
                                "/user/create",
                                "/user/login",
                                "/user/signup",
                                "/user/forgotPassword"
                        ).permitAll()

                        .requestMatchers(HttpMethod.OPTIONS).permitAll()

                        .anyRequest().authenticated())

                .exceptionHandling(exc -> exc.authenticationEntryPoint(unauthorizedHandler))

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}
