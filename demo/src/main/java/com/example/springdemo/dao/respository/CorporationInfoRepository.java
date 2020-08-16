package com.example.springdemo.dao.respository;

import com.example.springdemo.dao.entity.CorporationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CorporationInfoRepository extends JpaRepository<CorporationInfo,Long> {

    @Query(value = "select * from tbl_corporation_info limit ?1,?2",nativeQuery = true)
    List<CorporationInfo> findAllCorporationInfo(Integer start, Integer end);

}
