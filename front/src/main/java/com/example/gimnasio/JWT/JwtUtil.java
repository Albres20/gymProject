package com.example.gimnasio.JWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

//jason web token
@Service
public class JwtUtil {
    private String secret = "gymClaveSecret";
    /*
    Método genérico extractClaims:

        - Toma un token JWT y una función (claisResolver) que toma las reclamaciones (Claims) del token y devuelve un tipo específico de valor.
        - Este método extrae todas las reclamaciones del token y las pasa a la función claisResolver para obtener el valor deseado (como el sujeto o la fecha de expiración).
     */
    public <T> T extractClaims(String token, Function<Claims, T> claisResolver){
        final Claims claims= extractAllClaims(token);

        return claisResolver.apply(claims);
    }
    /*
    Método extractAllClaims:

        - Utiliza la biblioteca io.jsonwebtoken para analizar el token y extraer todas las reclamaciones del cuerpo del token.
    */
    public Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //Toma un token JWT como entrada y utiliza la función extractClaims para obtener el sujeto (que generalmente es el nombre de usuario) de las reclamaciones del token.
    public String extracUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }
    //Toma un token JWT como entrada y utiliza la función extractClaims para obtener el sujeto (que generalmente es el nombre de usuario) de las reclamaciones del token.
    public Date extractExpiration(String token){
        return extractClaims(token, Claims::getExpiration);
    }





    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username, String role){
        Map<String, Object> claims =new HashMap<>();
        claims.put("role", role);
        return  createToken(claims, username);
    }
    private String createToken(Map<String, Object> Claims, String subject){

        return Jwts.builder()
                .setClaims(Claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*10))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public boolean validateToken(String token, UserDetails userDetails){
        final String username= extracUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
