package com.example.springdemo.controller;

import com.example.springdemo.dao.entity.CorporationInfo;
import com.example.springdemo.dao.entity.UserInfo;
import com.example.springdemo.dao.entity.UserMess;
import com.example.springdemo.dao.entity.UserMessData;
import com.example.springdemo.dao.respository.CorporationInfoRepository;
import com.example.springdemo.dao.respository.UserInfoRepository;
import com.example.springdemo.dao.respository.UserMessRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/jpa")
public class JpaController {

    private final UserInfoRepository userInfoRepository;
    private final UserMessRepository userMessRepository;
    private final CorporationInfoRepository corporationInfoRepository;

    public JpaController(UserInfoRepository userInfoRepository, UserMessRepository userMessRepository, CorporationInfoRepository corporationInfoRepository) {
        this.userInfoRepository = userInfoRepository;
        this.userMessRepository = userMessRepository;
        this.corporationInfoRepository = corporationInfoRepository;
    }

    @GetMapping("/getUser/{phone}")
    public String getUserByPhone(@PathVariable String phone){
        UserInfo userInfo = userInfoRepository.findNickByPhoneAndIsDelete(phone,0);
        System.out.println(userInfo);
        return userInfo.getNick();
    }

    @GetMapping("/getUser")
    public List<String> getUser(){
        Sort sort = Sort.by(Sort.Order.desc("nick"));
        Pageable pageable = PageRequest.of(0,10,sort);
        Page<UserInfo> userInfos = userInfoRepository.findAll(pageable);
        System.out.println(userInfos);
        List<String> collect =  userInfos.get().flatMap(userInfo -> Stream.of(userInfo.getNick())).collect(Collectors.toList());

        System.out.println(collect);
        return collect;
    }

    @GetMapping("/getUserMessInfo")
    public List<UserMessData> getUserMessInfo(){
        List<UserMessData> userMessInfo = userInfoRepository.findUserMessInfo();
        return userMessInfo;
    }

    @GetMapping("/getUserMess")
    public List<UserMess> getUserMess(@PathParam("phone") String phone,@PathParam("page") Integer page, @PathParam("num") Integer num){
        List<UserMess> userMess = userMessRepository.findAllUserMess(phone,page*num,num);
        return userMess;
    }

    @GetMapping("/getCorporationInfo")
    public List<CorporationInfo> getCorporationInfo(@PathParam("page") Integer page, @PathParam("num") Integer num){
        List<CorporationInfo> corporationInfos = corporationInfoRepository.findAllCorporationInfo(page*num,num);
        return corporationInfos;
    }
}
