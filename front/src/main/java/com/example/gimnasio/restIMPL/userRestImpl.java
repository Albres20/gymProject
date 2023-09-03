package com.example.gimnasio.restIMPL;

import com.example.gimnasio.constents.gymConstants;
import com.example.gimnasio.rest.userRest;
import com.example.gimnasio.service.userService;
import com.example.gimnasio.utils.gymUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class userRestImpl implements userRest {

    @Autowired
    userService userService;

    @Override
    public ResponseEntity<String> signup(Map<String, String> requestMap) {
        try {
            return userService.signup(requestMap);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return gymUtils.getResponseEntity(gymConstants.ALGO_SALIO_MAL, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try {
            return userService.login(requestMap);


        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return gymUtils.getResponseEntity(gymConstants.ALGO_SALIO_MAL, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
