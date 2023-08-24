package com.example.gimnasio.serviceIMPL;


import com.example.gimnasio.POJO.user;
import com.example.gimnasio.constents.gymConstants;
import com.example.gimnasio.dao.userDAO;
import com.example.gimnasio.service.userService;
import com.example.gimnasio.utils.gymUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;


@Slf4j
@Service
public class userServiceImpl implements userService {

    @Autowired
    userDAO userDAO;
    @Override
    public ResponseEntity<String> signup(Map<String, String> requestMap) {
        log.info("Dentro de registrarse:", requestMap);
        try {
            if(validateSignUpMap(requestMap)){
                user user=userDAO.findByEmailId(requestMap.get("email"));
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
