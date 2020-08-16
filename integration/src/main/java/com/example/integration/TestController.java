package com.example.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.support.MutableMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class TestController {

    @Autowired
    @Qualifier("testInIntegrationFlow")
    IntegrationFlow integrationFlow;

    @GetMapping("/test")
    public String testApi() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("testheader","test");
        integrationFlow.getInputChannel().send(new MutableMessage("hello world",hashMap));
        return "hello world";
    }

}
