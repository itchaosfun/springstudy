package com.cmim.felowabledemo.controller;


import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ┃  * @Author: Oliver.zhai
 *
 * @Date: 2020/8/12 1:34 PM
 */
@RestController
@RequestMapping("flowable")
public class TestController {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private ManagementService managementService;
    @Autowired
    private DynamicBpmnService dynamicBpmnService;

    @GetMapping("add")
    public String addExpense(String userId, String days, String reason) {
        Map<String, Object> map = new HashMap<>();
        map.put("employee", userId);
        map.put("days", days);
        map.put("reason", reason);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holidayRequest", map);

        return "id:" + processInstance.getId();
    }

    /**
     * 获取指定用户组流程任务列表
     *
     * @param group
     * @return
     */
    @GetMapping("list")
    public Object list(String group) {
        try {

            List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(group).list();
            if (null == tasks) {
                return "null";
            }
            return tasks.toString();
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    /**
     * 通过/拒绝任务
     *
     * @param taskId
     * @param approved 1 ：true  2：false
     * @return
     */
    @GetMapping("apply")
    public String apply(String taskId, String approved) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            return "流程不存在";
        }
        Map<String, Object> variables = new HashMap<>();
        Boolean apply = approved.equals("1") ? true : false;
        variables.put("approved", apply);
        taskService.complete(taskId, variables);
        return "审批是否通过：" + approved;

    }

    /**
     * 查询流程图
     *
     * @param httpServletResponse
     * @param processId
     * @throws Exception
     */
    @RequestMapping(value = "processDiagram")
    public void genProcessDiagram(HttpServletResponse httpServletResponse, String processId) throws Exception {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();

        //流程走完的不显示图
        if (pi == null) {
            return;
        }
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        String InstanceId = task.getProcessInstanceId();
        List<Execution> executions = runtimeService
                .createExecutionQuery()
                .processInstanceId(InstanceId)
                .list();

        //得到正在执行的Activity的Id
        List<String> activityIds = new ArrayList<>();
        List<String> flows = new ArrayList<>();
        for (Execution exe : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
            activityIds.addAll(ids);
        }

        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
        ProcessEngineConfiguration engconf = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engconf.getProcessDiagramGenerator();
        InputStream in = diagramGenerator.generateDiagram(bpmnModel,
                "png",
                activityIds,
                flows,
                engconf.getActivityFontName(),
                engconf.getLabelFontName(),
                engconf.getAnnotationFontName(),
                engconf.getClassLoader(), 1.0, true);
        OutputStream out = null;
        byte[] buf = new byte[1024];
        int legth = 0;
        try {
            out = httpServletResponse.getOutputStream();
            while ((legth = in.read(buf)) != -1) {
                out.write(buf, 0, legth);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

}
