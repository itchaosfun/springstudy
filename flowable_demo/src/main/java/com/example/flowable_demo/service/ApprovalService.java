package com.example.flowable_demo.service;

import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface ApprovalService {

    /**
     * 创建工作流
     * @param filePath
     * @return
     */
    Map<String,Object> createFlow(String filePath);

    /**
     * 启动工作流
     * @param processKey
     * @return
     */
    ProcessInstance startFlow(String userId, String processKey);


    /**
     * 获取用户任务
     * @param userId
     * @return
     */
    List<Task> getTask(String userId);

    /**
     * 通过
     * @param taskId
     * @return
     */
    Boolean approve(String taskId);

    /**
     * 拒绝
     * @param taskId
     * @return
     */
    Boolean reject(String taskId);

}
