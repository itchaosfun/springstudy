package com.example.flowable_demo.event;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

@Slf4j
public class RejectEvent implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        log.info("RejectEvent execute {}",delegateExecution);
    }
}
