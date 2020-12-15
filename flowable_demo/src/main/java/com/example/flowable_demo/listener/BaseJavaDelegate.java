package com.example.flowable_demo.listener;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("baseJavaDelegate")
@Slf4j
public class BaseJavaDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        log.info("BaseJavaDelegateService delegateExecution:{}", delegateExecution);
    }
}
