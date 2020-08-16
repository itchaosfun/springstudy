package com.example.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.DirectChannelSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;

@Configuration
public class TestIntegrationConfig {

    @Bean
    public DirectChannelSpec testOutChannel() {
        return MessageChannels.direct("testOutChannel");
    }

    @Bean
    public DirectChannelSpec testOOutChannel() {
        return MessageChannels.direct("testOOutChannel");
    }

    @Bean
    @ServiceActivator(inputChannel = "testOOutChannel")
    public GenericHandler testHandler(){
        return (message,header) -> {
            System.out.println("message payload = " + message);
            return message;
        };
    }

    @Bean
    public IntegrationFlow testInIntegrationFlow() {
        return IntegrationFlows
                .from(MessageChannels.direct("testInChannel"))
                .<String, String>transform(String::toUpperCase)
                .handle(new GenericHandler<Object>() {
                    @Override
                    public Object handle(Object o, MessageHeaders messageHeaders) {
                        System.out.println("object = " + o);
                        return o;
                    }
                })
                .channel(testOutChannel())
                .get();
    }

    @Bean
    public IntegrationFlow testOutIntegrationFlow() {
        return IntegrationFlows
                .from(testOutChannel())
                .<String, String>transform(t -> (t + ", " + t).toUpperCase())
                .channel(testOOutChannel())
                .get();
    }
}
