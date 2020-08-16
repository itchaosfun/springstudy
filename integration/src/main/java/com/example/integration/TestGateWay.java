package com.example.integration;

import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = "testOOutChannel")
public interface TestGateWay {
    void test(String data);
}
