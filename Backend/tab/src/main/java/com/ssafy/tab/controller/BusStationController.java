package com.ssafy.tab.controller;

import com.ssafy.tab.domain.Bus;
import com.ssafy.tab.service.BusStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BusStationController {

    private final BusStationService busStationService;

    @GetMapping("/api/stops/{cityCode}/{stationId}")
    public List<Bus> searchBus(@PathVariable("cityCode")String cityCode, @PathVariable("stationId")String stationId){

        return busStationService.findBus(cityCode,stationId);
    }

}
