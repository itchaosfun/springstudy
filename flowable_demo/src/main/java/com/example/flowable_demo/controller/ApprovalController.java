package com.example.flowable_demo.controller;

import com.example.flowable_demo.service.ApprovalService;
import com.example.flowable_demo.service.impl.ApprovalServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.flowable.bpmn.model.Process;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/approval")
public class ApprovalController {

    private final ApprovalServiceImpl approvalService;

    public ApprovalController(ApprovalServiceImpl approvalService) {
        this.approvalService = approvalService;
    }

    @GetMapping("/create")
    public Map<String, Object> createHello() {

        Map<String, Object> res = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        String flowPath = "src/main/resources/process/approval4-model.bpmn20.xml";

        Map<String, Object> createRes = approvalService.createFlow(flowPath);

        if (null == createRes) {
            res.put("msg", "创建流程失败");
            res.put("res", "0");
            res.put("data", data);
            return res;
        }
        List<Process> processes = (List<Process>) createRes.get("processes");

        ArrayList<String> ids = new ArrayList<>();
        for (Process process : processes) {
            ids.add(process.getId());
        }
        data.put("processKeys", ids);
        data.put("deployId", ((Deployment) createRes.get("deployment")).getId());
        res.put("data", data);
        res.put("msg", "创建流程成功");
        res.put("res", "1");
        return res;
    }

    @RequestMapping("/start")
    @ResponseBody
    public Map<String, Object> startFlow(@PathParam("startUserId") String startUserId,
                                         @PathParam("approvalUserId") String approvalUserId,
                                         @PathParam("processKey") String processKey,
                                         @PathParam("businessKey") String businessKey) {
        Map<String, Object> res = new HashMap<>();
        Map<String, String> data = new HashMap<>();

        if (StringUtils.isEmpty(startUserId)) {
            res.put("msg", "启动流程失败");
            res.put("res", "0");
            data.put("error", "startUserId不能为空");
            res.put("data", data);
            return res;
        }


        if (StringUtils.isEmpty(approvalUserId)) {
            res.put("msg", "启动流程失败");
            res.put("res", "0");
            data.put("error", "approvalUserId不能为空");
            res.put("data", data);
            return res;
        }


        if (StringUtils.isEmpty(processKey)) {
            res.put("msg", "启动流程失败");
            res.put("res", "0");
            data.put("error", "processKey不能为空");
            res.put("data", data);
            return res;
        }

        if (StringUtils.isEmpty(businessKey)) {
            res.put("msg", "启动流程失败");
            res.put("res", "0");
            data.put("error", "businessKey不能为空");
            res.put("data", data);
            return res;
        }

        ProcessInstance processInstance = approvalService.startFlow(startUserId, approvalUserId, processKey, businessKey);
        if (null == processInstance) {
            res.put("msg", "启动流程失败");
            res.put("res", "0");
            res.put("data", data);
            return res;
        }
        data.put("processId", processInstance.getId());
        res.put("msg", "启动流程成功");
        res.put("res", "1");
        res.put("data", data);
        return res;
    }

    @RequestMapping("/commit")
    public Map<String, Object> commit(@PathParam("taskId") String taskId,
                                      @PathParam("approvalUserId") String approvalUserId,
                                      @PathParam("commodityId") String commodityId) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        if (StringUtils.isEmpty(taskId)) {
            res.put("msg", "拒绝失败");
            res.put("res", "0");
            data.put("error", "taskId不能为空");
            res.put("data", data);
        }

        if (StringUtils.isEmpty(approvalUserId)) {
            res.put("msg", "拒绝失败");
            res.put("res", "0");
            data.put("error", "approvalUserId不能为空");
            res.put("data", data);
        }

        if (StringUtils.isEmpty(commodityId)) {
            res.put("msg", "拒绝失败");
            res.put("res", "0");
            data.put("error", "commodityId不能为空");
            res.put("data", data);
        }

        Boolean approve = approvalService.commit(taskId, approvalUserId, commodityId);
        res.put("msg", "审批成功");
        res.put("res", "1");
        res.put("data", data);

        return res;
    }

    @RequestMapping("/approve")
    public Map<String, Object> approve(@PathParam("taskId") String taskId) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        if (StringUtils.isEmpty(taskId)) {
            res.put("msg", "拒绝失败");
            res.put("res", "0");
            data.put("error", "taskId不能为空");
            res.put("data", data);
        }

        Boolean approve = approvalService.approve(taskId);
        res.put("msg", "审批成功");
        res.put("res", "1");
        res.put("data", data);

        return res;
    }

    @RequestMapping("/reject")
    public Map<String, Object> reject(@PathParam("taskId") String taskId, @PathParam("comment") String comment) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        if (StringUtils.isEmpty(taskId)) {
            res.put("msg", "拒绝失败");
            res.put("res", "0");
            data.put("error", "taskId不能为空");
            res.put("data", data);
        }

        Boolean approve = approvalService.reject(taskId, comment);
        res.put("msg", "拒绝成功");
        res.put("res", "1");
        res.put("data", data);

        return res;
    }

    @RequestMapping("/getTask")
    public Map<String, Object> getTask(@PathParam("userId") String userId) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        if (StringUtils.isEmpty(userId)) {
            res.put("msg", "获取任务失败");
            res.put("res", "0");
            data.put("error", "userId不能为空");
            res.put("data", data);
        }

        List<Task> tasks = approvalService.getTask(userId);
        for (Task task : tasks) {
            Map<String, Object> taskLocalVariables = task.getTaskLocalVariables();
            log.info("task = {}, variable = {}", task, taskLocalVariables);
        }
        res.put("msg", "获取任务成功");
        res.put("res", "1");
        data.put("task", tasks);
        res.put("data", data);

        return res;
    }


}
