package com.ecomerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecomerce.entities.User;
import com.ecomerce.services.UserService;

@RequestMapping("/users")
@RestController
public class UserController {

    @Autowired
    private UserService userService;    
    
    @PostMapping("/add")
    public ResponseEntity<User> criarUser(@RequestBody User user) {
        try {
            User novoUser = userService.addUser(user);
            return new ResponseEntity<>(novoUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}