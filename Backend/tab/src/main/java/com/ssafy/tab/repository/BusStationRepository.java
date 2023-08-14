package com.ssafy.tab.repository;

import com.ssafy.tab.domain.BusStation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusStationRepository extends JpaRepository <BusStation, String> {
}
