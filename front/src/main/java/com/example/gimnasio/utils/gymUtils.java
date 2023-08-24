package com.example.gimnasio.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class gymUtils {

    private gymUtils(){

    }

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){

        return  new ResponseEntity<>("{\"message\":\""+responseMessage+"\"}", httpStatus);
    }
}
