package com.example.userservice.controller;

import com.example.userservice.dao.UserRepository;
import com.example.userservice.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/{id}")
    public User findById(@PathVariable Long id) {
        System.out.println("id = " + id);
        User user = this.userRepository.getOne(id);
        System.out.println("user = " + user);
        return user;
    }
}
