package com.example.flowable_demo.listener;

import com.example.flowable_demo.service.ApprovalService;
import com.example.flowable_demo.service.impl.ApprovalServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("baseTaskListener")
@Slf4j
public class BaseTaskListener implements TaskListener {

    private static final long serialVersionUID = 6177079849427958268L;

    @Autowired
    ApprovalServiceImpl approvalService;

    @Override
    public void notify(DelegateTask delegateTask) {
       log.info("delegateTask = {}",delegateTask);
       approvalService.transBusinessTask(delegateTask);
    }
}
