package com.ssafy.tab.repository;

import com.ssafy.tab.domain.BusTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusTestRepository extends JpaRepository<BusTest, String> {


    BusTest findByRouteNoAndStationNameAndRouteId(String routeNo, String stationName, String routeId);
    List<BusTest> findByRouteNoAndRouteId(String routeNo,String routeId);
    BusTest findByRouteNoAndOrderStopAndRouteId(String routeNo, int orderStop, String routeId);
}
