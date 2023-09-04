package com.example.gimnasio.serviceIMPL;


import com.example.gimnasio.JWT.CustomerUsersDetailsService;
import com.example.gimnasio.JWT.JwtUtil;
import com.example.gimnasio.POJO.user;
import com.example.gimnasio.constents.gymConstants;
import com.example.gimnasio.dao.userDAO;
import com.example.gimnasio.service.userService;
import com.example.gimnasio.utils.gymUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;


@Slf4j
@Service
public class userServiceImpl implements userService{

    @Autowired
    userDAO userDAO;

    @Autowired
    @Lazy
    AuthenticationManager authenticationManager;
    @Autowired
    CustomerUsersDetailsService customerUsersDetailsService;
    @Autowired
    JwtUtil jwUtil;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;
    /*@Override
    public ResponseEntity<String> signup(Map<String, String> requestMap) {
        log.info("Dentro de registrarse:", requestMap);

        try {
            if(validateSignUpMap(requestMap)){
                user user=userDAO.findByEmailId(requestMap.get("email"));

                System.out.println("here");
                if(Objects.isNull(user)){
                    userDAO.save(getUserFromMap(requestMap));
                    return gymUtils.getResponseEntity("Registrado exitosamente", HttpStatus.OK);
                }
                else{
                    return gymUtils.getResponseEntity("El correo existe.", HttpStatus.BAD_REQUEST);
                }

            }
            else{
                return gymUtils.getResponseEntity(gymConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return gymUtils.getResponseEntity(gymConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);


    }*/
    //*------------------------new---start
    public ResponseEntity<String> signup(Map<String, String> requestMap) {
        log.info("Entrando en el método signup para el usuario con email: {}", requestMap.get("email"));

        try {
            if (validateSignUpMap(requestMap)) {
                String email = requestMap.get("email");
                user existingUser = userDAO.findByEmailId(email);

                if (existingUser == null) {
                    String rawPassword = requestMap.get("password");
                    String encodedPassword = encodePassword(rawPassword); // Utiliza BCrypt para cifrar la contraseña

                    user newUser = getUserFromMap(requestMap);
                    newUser.setPassword(encodedPassword);

                    userDAO.save(newUser);

                    return gymUtils.getResponseEntity("Registrado exitosamente", HttpStatus.OK);
                } else {
                    return gymUtils.getResponseEntity("El correo ya está registrado.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return gymUtils.getResponseEntity("Error: Los datos proporcionados son incorrectos o faltantes.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.error("Error al registrar un nuevo usuario:", ex);
            return gymUtils.getResponseEntity("Error interno del servidor al registrar un nuevo usuario.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Método para cifrar la contraseña usando BCrypt
    private String encodePassword(String rawPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(rawPassword);
    }
    //// new --------------------end
    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");
        try{
            System.out.println("------------------------------------------------>---");
            Authentication auth=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );
            System.out.println("------------------------------------------------"+auth+">---");
            if(auth.isAuthenticated()){
                if(customerUsersDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")){
                    System.out.println("token exitoso");

                    return new ResponseEntity<String>("{\"token\":\""+jwUtil.generateToken(customerUsersDetailsService.getUserDetail().getEmail(), customerUsersDetailsService.getUserDetail().getRol())+"\"}",
                    HttpStatus.OK);
                }
                else{
                    return new ResponseEntity<String>("{\"message\":\""+"Espera a que el edmin lo apruebe"+"\"}", HttpStatus.BAD_REQUEST);
                }

            }
        }catch (Exception ex){
            log.error("{}", ex);
        }
        return new ResponseEntity<String >("{\"message\":\""+"Espera la probación del admin."+"\"}", HttpStatus.BAD_REQUEST);
    }

    @Override
    public UserDetailsService userDetailsService() {/*
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return (UserDetails) userDAO.findByEmailId(username);
            }
        };*/
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                UserDetails userDetails = customerUsersDetailsService.loadUserByUsername(username);
                if (userDetails == null) {
                    throw new UsernameNotFoundException("User not found");
                }
                return userDetails;
            }
        };
    }

    private boolean validateSignUpMap(Map<String,String> requestMap){
        if(requestMap.containsKey("name") && requestMap.containsKey("lastName") && requestMap.containsKey("contactNumber") && requestMap.containsKey("email")
        && requestMap.containsKey("password")){
            return true;
        }
        return false;
    }

    private user getUserFromMap(Map<String, String> requestMap){
        user user=new user();
        user.setName(requestMap.get("name"));
        user.setLastname(requestMap.get("lastName"));
        user.setNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus(requestMap.get("false"));
        user.setRol(requestMap.get("user"));

        return user;
    }
}
