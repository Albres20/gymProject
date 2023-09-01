package com.example.gimnasio.dao;

import com.example.gimnasio.POJO.user;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface userDAO extends JpaRepository<user, Integer> {

    public user findByEmailId(@Param("email") String email);
}
