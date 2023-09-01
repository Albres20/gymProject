package com.example.gimnasio.JWT;


import com.example.gimnasio.dao.userDAO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Service
public class CustomerUsersDetailsService implements UserDetailsService {
    @Autowired
    userDAO userDao;

    private com.example.gimnasio.POJO.user userDetail;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername", username); //Registro de información utilizando SLF4J
        userDetail=userDao.findByEmailId(username);//Obtención de los detalles del usuario a través de userDAO
        if(!Objects.isNull(userDetail)){
            //Si los detalles del usuario no son nulos, se crea y devuelve un objeto UserDetails con los datos del usuario
            return new User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());

        }
        else {
            // Si no se encuentra el usuario, se lanza una excepción UsernameNotFoundException

            throw new UsernameNotFoundException("User not found");
        }
    }
    // Método para obtener los detalles del usuario
    public com.example.gimnasio.POJO.user getUserDetail(){
        return userDetail;
    }


}
/*//https://www.youtube.com/watch?v=5MBYlYSczGg https://www.youtube.com/watch?v=oRSH8-fJWQA&list=PLdRq0mbeEBmwdwZF3lWwCcWmD76GfEFVT&index=3*/