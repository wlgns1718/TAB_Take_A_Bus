package com.ssafy.tab.repository;

import com.ssafy.tab.domain.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArduinoRepository extends JpaRepository<Bus,Long> {

    @Query("select b from Bus b where b.vehicleNo = :busNo")
    Optional<Bus> findByBusNo(String busNo);

//    @Query("select b.vulnerable from Bus b where b.vhhicle_no = :busNo")
//    Optional<String> findSalt(@Param("userId")String userId);
}
