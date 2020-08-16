package com.example.websocket;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@ServerEndpoint("/websocket/{username}")
public class WebSocketServer {

    public static int onLineNumber = 0;

    private static Map<String, Connector> clients = new ConcurrentHashMap<>();

    //客户端连接
    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) {
        log.info("有新的客户端连接了：session Id = {},username = {}", session.getId(), username);

        //通知所有人，我上线了
        Map<String, Object> newOnLineMap = new HashMap<>();
        newOnLineMap.put("messageType", Constance.NEW_ONLINE_USER_MESSAGE_TYPE);
        newOnLineMap.put("username", username);
        this.sendAll(JSON.toJSONString(newOnLineMap), username);

        onLineNumber++;

        clients.put(username, new Connector(session, username));

        //获取当前所有在线的用户,通知给前端页面
        Map<String, Object> onLinesMap = new HashMap<>();
        onLinesMap.put("messageType", Constance.ALL_ONLINE_USER_MESSAGE_TYPE);
        onLinesMap.put("onLineUsers", clients.keySet());
        onLinesMap.put("onLineNumber", onLineNumber);

        sendMessageTo(JSON.toJSONString(onLinesMap), username);


    }

    //客户端关闭
    @OnClose
    public void onClose(@PathParam("username") String username, Session session) {
        log.info("有用户断开了，id=：{},username = {}", session.getId(), username);
        onLineNumber--;
        clients.remove(username);

        Map<String, Object> offlineMap = new HashMap<>();
        offlineMap.put("messageType", Constance.NEW_OFFLINE_USER_MESSAGE_TYPE);
        offlineMap.put("onLineUsers", clients.keySet());
        offlineMap.put("username", username);

        sendAll(JSON.toJSONString(offlineMap), username);

    }


    //发生错误
    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    //接收消息
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("服务端收到客户端的消息了：session is = {}, message = {}", session.getId(), message);

        JSONObject jsonObject = JSON.parseObject(message);
        String textMessage = jsonObject.getString("message");
        String fromUsername = jsonObject.getString("username");
        String toUsername = jsonObject.getString("to");

        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("messageType", Constance.SEND_MSG_MESSAGE_TYPE);
        messageMap.put("textMessage", textMessage);
        messageMap.put("fromUsername", fromUsername);

        if (toUsername.equals("All")) {
            messageMap.put("toUsername", "所有人");
            sendAll(JSON.toJSONString(messageMap), fromUsername);
        } else {
            messageMap.put("toUsername", toUsername);
            sendMessageTo(JSON.toJSONString(messageMap), toUsername);
        }
    }

    //指定用户发送消息
    private void sendMessageTo(String message, String toUsername) {
        for (Map.Entry<String, Connector> connectorEntry : clients.entrySet()) {
            Connector connector = connectorEntry.getValue();
            if (toUsername.equals(connector.getUsername())) {
                connector.getSession().getAsyncRemote().sendText(message);
                break;
            }
        }
    }

    //群发消息
    private void sendAll(String message, String fromUsername) {
        for (Map.Entry<String, Connector> connectorEntry : clients.entrySet()) {
            connectorEntry.getValue().getSession().getAsyncRemote().sendText(message);
        }
    }


}
