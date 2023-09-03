package com.example.gimnasio.JWT;

import com.example.gimnasio.dao.userDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class ApplicationConfig {
    @Autowired
    public userDAO userDAO;
    UserDetails userDetails;
    // Definición de un bean para UserDetailsService
    @Bean
    public UserDetailsService userDetailsService(){
        return (username -> {
            // Implementación que utiliza userDAO para cargar los detalles del usuario
            userDetails=userDAO.findByEmailId(username);
            return  userDetails;
        });

    }
    // Definición de un bean para AuthenticationManager

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        // Obtiene el AuthenticationManager de la configuración de autenticación

        return authenticationConfiguration.getAuthenticationManager();
    }
    // Definición de un bean para PasswordEncoder
    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

}
