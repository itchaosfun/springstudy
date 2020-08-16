package com.example.springdemo.dao.respository;

import com.example.springdemo.dao.entity.UserInfo;
import com.example.springdemo.dao.entity.UserMessData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {

    UserInfo findNickByPhoneAndIsDelete(String phone,Integer isDelete);

    @Query(value = "select i.phone,m.accept_num from tbl_user_info i left join tbl_user_mess m on i.phone=m.accept_num limit 100",nativeQuery = true)
    List<UserMessData> findUserMessInfo();

}
