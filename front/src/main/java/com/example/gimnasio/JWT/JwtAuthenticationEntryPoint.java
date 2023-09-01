package com.example.gimnasio.JWT;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws ServletException, IOException {
        // Entra en acción cuando se detecta una autenticación no válida o falta de autenticación

        // Envía una respuesta de error con el código de estado HTTP 401 (No autorizado)
        // y un mensaje indicando que el usuario no está autorizado
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized user");
    }
}
