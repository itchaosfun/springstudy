package com.example.flowable_demo.service;

import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.Process;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.Task;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DelayServiceImpl implements ApprovalService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Override
    public Map<String, Object> createFlow(String filePath) {
        Map<String, Object> res = new HashMap<>();
        //解析BPMN模型看是否成功
        XMLStreamReader reader = null;
        InputStream inputStream = null;
        try {
            BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
            XMLInputFactory factory = XMLInputFactory.newInstance();
            inputStream = new FileInputStream(new File(filePath));
            reader = factory.createXMLStreamReader(inputStream);
            BpmnModel model = bpmnXMLConverter.convertToBpmnModel(reader);
            List<Process> processes = model.getProcesses();
            Process curProcess = null;
            if (CollectionUtils.isEmpty(processes)) {
                log.error("BPMN模型没有配置流程");
                return null;
            }
            res.put("processes", processes);
            curProcess = processes.get(0);

            inputStream = new FileInputStream(new File(filePath));
            DeploymentBuilder deploymentBuilder = repositoryService.createDeployment().name("TEST_FLOW")
                    .addInputStream(filePath, inputStream);

            Deployment deployment = deploymentBuilder.deploy();
            res.put("deployment", deployment);
            log.warn("部署流程 name:" + curProcess.getName() + " key " + deployment.getKey() + " deploy " + deployment);
            return res;
        } catch (Exception e) {
            log.error("BPMN模型创建流程异常", e);
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (XMLStreamException e) {
                log.error("关闭异常", e);
            }
        }
    }


    @Override
    public ProcessInstance startFlow(String startUserId, String approvalUserId, String processKey, String businessKey) {
        if (StringUtils.isEmpty(processKey)) {
            return null;
        }

        List<Deployment> deployments = repositoryService.createDeploymentQuery().processDefinitionKey(processKey).list();

        if (null == deployments || deployments.isEmpty()) {
            log.error("没有该流程");
            return null;
        }

        Authentication.setAuthenticatedUserId(startUserId);

        Map<String, Object> paras = new HashMap<>();
        paras.put("approvalUserId", approvalUserId);
        return runtimeService.startProcessInstanceByKey(processKey, businessKey, paras);
    }

    @Override
    public List<Task> getTask(String userId) {
        List<Task> list = taskService.createTaskQuery().taskAssignee(userId).orderByTaskCreateTime().desc().list();
        for (Task task : list) {
            String processInstanceId = task.getProcessInstanceId();
            List<Comment> taskComments = taskService.getProcessInstanceComments(processInstanceId);
            if (taskComments != null) {
                for (Comment taskComment : taskComments) {
                    log.info("taskId = {}, commentMessage = {}, taskComment = {}", task.getId(), taskComment.getFullMessage(), taskComment);
                }
            }
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            String businessKey = processInstance.getBusinessKey();
            String startUserId = processInstance.getStartUserId();
            log.info("taskId = {}, businessKey = {},startUserId = {}, task = {}", task.getId(), businessKey, startUserId, task);
        }
        return list;
    }

    /**
     * 通过
     *
     * @param taskId
     * @return
     */
    @Override
    public Boolean approve(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (null == task) {
            return false;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("audit", 1);
        taskService.complete(taskId, map);
        return true;
    }

    @Override
    public Boolean commit(String taskId, String approvalUserId, String commodityId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (null == task) {
            return false;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("approvalUserId", approvalUserId);
        map.put("commodityId", commodityId);
        taskService.complete(taskId, map);

        return true;
    }

    /**
     * 拒绝
     *
     * @param taskId
     * @param comment
     * @return
     */
    @Override
    public Boolean reject(String taskId, String comment) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (null == task) {
            return false;
        }
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        String businessKey = processInstance.getBusinessKey();
        String startUserId = processInstance.getStartUserId();

        log.info("businessKey = {}, startUserId = {}", businessKey, startUserId);
        Map<String, Object> map = new HashMap<>();
        map.put("audit", 0);
        map.put("commitUserId", startUserId);
        taskService.addComment(taskId, task.getProcessInstanceId(), comment);
        taskService.complete(taskId, map);
        return true;
    }

    @Override
    public void transBusinessTask(DelegateTask delegateTask) {
        log.info("delegateTask = {}",delegateTask);
    }
}
