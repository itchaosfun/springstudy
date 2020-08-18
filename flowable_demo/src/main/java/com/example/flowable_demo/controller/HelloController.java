package com.example.flowable_demo.controller;

import org.flowable.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/hello")
public class HelloController {

    @Autowired
    private RuntimeService runtimeService;

    @GetMapping("/create")
    public String createHello(){

        runtimeService.activateProcessInstanceById("holidayRequest");

        return "create success";
    }

}
