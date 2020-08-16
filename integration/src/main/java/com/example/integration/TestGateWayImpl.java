package com.example.integration;

public class TestGateWayImpl implements TestGateWay {
    @Override
    public void test(String data) {
        System.out.println("data = " + data);
    }
}
