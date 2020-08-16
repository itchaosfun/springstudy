package com.example.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@Slf4j
public class WebSocketController {

    /**
     * 该接口直接浏览器访问，返回的是一个html页面，该页面放在resources/templates目录中
     * 通过返回指定html名称来决定返回哪一个页面至浏览器
     * 注意一个坑： 如果需要返回jsp/html页面的话，Controller类不能使用@RestController注解，应该用@Controller注解
     *
     * @param name
     * @param model
     * @return
     */
    @RequestMapping("/websocket/{name}")
    public String websocket(@PathVariable String name, Model model) {
        try {
            log.info("enter websocket page");
            model.addAttribute("username", name);
            return "websocket";
        } catch (Exception e) {
            log.info("enter websocket exception:{}", e.getMessage());
            return "error";
        }

    }
}
