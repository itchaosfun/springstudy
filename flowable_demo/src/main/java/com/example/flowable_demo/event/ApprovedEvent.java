package com.example.flowable_demo.event;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

@Slf4j
public class ApprovedEvent implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
      log.info("ApprovedEvent execute {}",delegateExecution);
    }
}
