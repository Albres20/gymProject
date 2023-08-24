package com.example.gimnasio.POJO;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

//segun java persistence query lenguage
@NamedQuery(name="user.findByEmailId", query = "select u from user u where u.email=:email")

@Data //Remove Anotation if you wanna make constructor or getter and setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name="user")
public class user {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name="lastname")
    private String lastname;

    @Column(name="contactNumber")
    private String number;

    @Column(name="email")
    private String email;
    @Column(name = "password")
    private String password;

    @Column(name="status")
    private String status;

    @Column(name="rol")
    private String rol;



}
