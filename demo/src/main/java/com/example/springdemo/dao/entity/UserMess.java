package com.example.springdemo.dao.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_user_mess")
public class UserMess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "serial")
    private String serial;

    @Column(name = "accept_num")
    private String acceptNum;

    @Column(name = "user_serial")
    private String userSerial;

    @Column(name = "is_delete")
    private Integer isDelete;

}
