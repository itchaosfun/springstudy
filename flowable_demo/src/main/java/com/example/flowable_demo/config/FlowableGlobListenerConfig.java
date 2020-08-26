package com.example.flowable_demo.config;


import com.example.flowable_demo.listener.GlobalTaskCreateListener;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEventDispatcher;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;


@Configuration
public class FlowableGlobListenerConfig implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private SpringProcessEngineConfiguration configuration;
    @Autowired
    private GlobalTaskCreateListener globalTaskCreateListener;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        FlowableEventDispatcher dispatcher = configuration.getEventDispatcher();
        //任务创建全局监听
        dispatcher.addEventListener(globalTaskCreateListener, FlowableEngineEventType.TASK_CREATED);
        //添加任务完成全局监听
        dispatcher.addEventListener(globalTaskCreateListener, FlowableEngineEventType.TASK_COMPLETED);
        //添加流程实例创建全局监听
        dispatcher.addEventListener(globalTaskCreateListener, FlowableEngineEventType.PROCESS_CREATED);
        //添加流程实例开始全局监听
        dispatcher.addEventListener(globalTaskCreateListener, FlowableEngineEventType.PROCESS_STARTED);
        //添加流程实例结束全局监听
        dispatcher.addEventListener(globalTaskCreateListener, FlowableEngineEventType.PROCESS_COMPLETED);
        //添加任务分配全局监听
        dispatcher.addEventListener(globalTaskCreateListener, FlowableEngineEventType.TASK_ASSIGNED);
    }
}
