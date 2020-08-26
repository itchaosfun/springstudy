package com.example.flowable_demo.listener;

import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEventType;
import org.flowable.common.engine.impl.event.FlowableEntityEventImpl;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.flowable.engine.delegate.event.FlowableProcessStartedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GlobalTaskCreateListener extends AbstractFlowableEngineEventListener {

    @Override
    protected void taskCreated(FlowableEngineEntityEvent event) {
        if (event instanceof FlowableEntityEventImpl){
            FlowableEventType type = event.getType();
            if (FlowableEngineEventType.TASK_CREATED.equals(type)) {
                //任务创建
                log.info("任务创建");
            } else if (FlowableEngineEventType.TASK_ASSIGNED.equals(type)) {
                //任务分配
                log.info("任务分配");
            } else if (FlowableEngineEventType.TASK_COMPLETED.equals(type)) {
                //   任务完成
                log.info("任务完成");
            }
        }
    }

    @Override
    protected void taskAssigned(FlowableEngineEntityEvent event) {
        //任务分配
        log.info("任务分配");
    }

    @Override
    protected void taskCompleted(FlowableEngineEntityEvent event) {
        //   任务完成
        log.info("任务完成");
    }

    @Override
    protected void processCreated(FlowableEngineEntityEvent event) {
        //流程创建
        log.info("流程创建");
    }

    @Override
    protected void processStarted(FlowableProcessStartedEvent event) {
        //流程开启
        log.info("流程开启");
    }

    @Override
    protected void processCompleted(FlowableEngineEntityEvent event) {
        //流程完成
        log.info("流程完成");
    }
}
