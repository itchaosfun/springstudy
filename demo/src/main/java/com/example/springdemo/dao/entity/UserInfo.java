package com.example.springdemo.dao.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity(name = "UserInfo")
@Table(name = "tbl_user_info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "serial")
    private String serial;

    @Column(name = "nick")
    private String nick;

    @Column(name = "phone")
    private String phone;

    @Column(name = "is_delete")
    private Integer isDelete;
}
