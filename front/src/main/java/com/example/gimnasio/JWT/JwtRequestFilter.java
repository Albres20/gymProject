package com.example.gimnasio.JWT;

import com.example.gimnasio.serviceIMPL.userServiceImpl;
import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Clase para interceptar las solicitudes a la aplicación
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private CustomerUsersDetailsService userDetailsService;
/*
    @Autowired
    private UserDetailsService userDetailsService;*/
    /*@Autowired
    userDAO userdao;*/
    private userServiceImpl userDetailsServices;
    @Autowired
    private  JwtUtil jwtUtil;

    Claims claims=null;
    String userName = null;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {

// 1. Obtener el encabezado de autorización de la solicitud
        //final String token = getTokenFromRequest(httpServletRequest);
        final String authorizationHeader = httpServletRequest.getHeader("Authorization");


        Claims claims;
        // 2. Verificar si la ruta coincide con ciertas rutas de usuario
        /*httpServletRequest.getServletPath():
        Esto obtiene la ruta del servlet actual (parte de la URL después del nombre de dominio y el contexto de la aplicación). Por ejemplo, si la URL es "http://ejemplo.com/contexto/user/login", esto devolvería "/user/login".

        .matches("/user/login|/user/signup|/user/forgotPassword"):
        Esto verifica si la ruta del servlet coincide con cualquiera de las tres cadenas proporcionadas: "/user/login", "/user/signup" o "/user/forgotPassword". El uso del operador de tubería | denota una alternativa en la expresión regular, lo que significa que coincide con cualquiera de las cadenas separadas por ese operador.*/
        if (httpServletRequest.getServletPath().matches("/user/login|/user/signup|/user/forgotPassword")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);/*El filtro permite que la solicitud actual continúe su procesamiento normal en el ciclo de vida del servlet, o puede realizar ciertas operaciones antes o después de dejar que la solicitud continúe.*/

        } else {
            String token = null;
            final String authHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
// 3. Extraer y validar el token JWT del encabezado de autorización
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                userName = jwtUtil.extracUsername(token);
                claims = jwtUtil.extractAllClaims(token);

            }
            // 4. Validar el token y autenticar al usuario
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUtil.extracUsername(token));


                if (jwtUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    //Se establecen detalles adicionales de autenticación, como la dirección IP y otros detalles de la solicitud actual.
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    //Finalmente, se establece la autenticación recién creada en el contexto de seguridad, lo que significa que el usuario está autenticado y autorizado para acceder a recursos protegidos.
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            }
            // 5. Permitir que la solicitud continúe su procesamiento normal

            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }



       /*if (token==null)
        {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        username=jwtService.extracUsername(token);*/


    }

    /*private String getTokenFromRequest(HttpServletRequest httpServletRequest) {
        final String authHeader=httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if(authHeader!=null && authHeader.startsWith("Bearer "))
        {
            return authHeader.substring(7);
        }
        return null;
    }*/

    //https://github.com/irojascorsico/spring-boot-jwt-authentication/blob/main/src/main/java/com/irojas/demojwt/Jwt/JwtService.java#L19
    public boolean isAdmin(){
        return "admin".equalsIgnoreCase((String) claims.get("role"));
    }
    public boolean isUser(){
        return "user".equalsIgnoreCase((String) claims.get("role"));
    }
    public String getCurrentUser(){
        return userName;
    }
}
