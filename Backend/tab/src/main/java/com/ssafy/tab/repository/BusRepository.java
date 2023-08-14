package com.ssafy.tab.repository;

import com.ssafy.tab.domain.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusRepository extends JpaRepository<Bus,Long> {

    Optional<Bus> findByVehicleNo(String vehicleNo);
}
