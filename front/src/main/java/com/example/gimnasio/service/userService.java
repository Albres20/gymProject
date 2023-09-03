package com.example.gimnasio.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;

public interface userService {
    ResponseEntity<String> signup(Map<String, String> requestMap);
    ResponseEntity<String> login(Map<String, String> requestMap);
    //new
    UserDetailsService userDetailsService();

}
