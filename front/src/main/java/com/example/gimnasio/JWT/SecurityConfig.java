package com.example.gimnasio.JWT;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.example.gimnasio.service.userService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig   {
/*permite configurar las solicitudes con autenticacion y sin autentificacion*/
    @Autowired
    CustomerUsersDetailsService customerUsersDetailsService;

    @Autowired
    userService userService;
/*
    private final userServiceImpl userService;*/
    @Autowired
    private JwtRequestFilter authenticationFilter;
    @Autowired
    JwtAuthenticationEntryPoint unauthorizedHandler;
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customerUsersDetailsService).passwordEncoder(passwordEncoder());
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
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }/*
    @Bean
    public AuthenticationManager securityAuthenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }*/

}
