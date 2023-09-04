package com.example.gimnasio.JWT;

import com.example.gimnasio.dao.userDAO;
import com.example.gimnasio.service.userService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApplicationConfig {/*
    @Autowired
    userService userService;*/
    /*@Autowired
    public userDAO userDAO;
    UserDetails userDetails;*/
    // Definición de un bean para UserDetailsService
    /*
    @Bean
    public UserDetailsService userDetailsService(){

        // Implementación que utiliza userDAO para cargar los detalles del usuario
        return (username -> {
            userDetails=(UserDetails)userDAO.findByEmailId(username);
            return  userDetails;
        });

    }*/
    // Definición de un bean para AuthenticationManager

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        // Obtiene el AuthenticationManager de la configuración de autenticación

        return authenticationConfiguration.getAuthenticationManager();
    }/*
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/
/*
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }*/
    // Definición de un bean para PasswordEncoder
    /*@Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }*/


}
