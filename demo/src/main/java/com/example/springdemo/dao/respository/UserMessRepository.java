package com.example.springdemo.dao.respository;

import com.example.springdemo.dao.entity.UserMess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserMessRepository extends JpaRepository<UserMess,Long> {

    @Query(value = "select * from tbl_user_mess where accept_num = ?1 limit ?2,?3",nativeQuery = true)
    List<UserMess> findAllUserMess(String phone,Integer start,Integer end);
}
