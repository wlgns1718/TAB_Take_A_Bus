package com.ssafy.tab.controller;

import com.ssafy.tab.domain.BusAPI;
import com.ssafy.tab.service.BusStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequiredArgsConstructor
public class BusStationController {

    private final BusStationService busStationService;

    @GetMapping("/api/stops/{cityCode}/{stationId}") // CITY_CODE = "37050", NODE_ID = "GMB383"
    public ResponseEntity<Map<String, Object>> searchBus(@PathVariable("cityCode") String cityCode, @PathVariable("stationId") String stationId) {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            List<BusAPI> result = busStationService.findAll(cityCode, stationId);

            if (result != null && !result.isEmpty()) {
                resultMap.put("data", result);
                resultMap.put("code", "200");
                resultMap.put("msg", "해당 정류장 버스 정보를 성공적으로 가져왔습니다.");
            } else {
                resultMap.put("code", "401");
                resultMap.put("msg", "해당 정류장에 도착 예정인 버스가 없습니다");
            }

        } catch (IOException e) {
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "해당 정류장 버스 정보를 가져오지 못했습니다.");
        } finally {
            return new ResponseEntity<Map<String, Object>>(resultMap, status);
        }
    }
    @GetMapping("/api/busstationdata/{cityName}")
    public ResponseEntity<Map<String, Object>> busStationData(@PathVariable("cityName") String cityName) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        boolean bool = busStationService.busStationData(cityName);
        if(bool){
            resultMap.put("code", "200");
            resultMap.put("msg", "성공적으로 DB에 정류장 데이터를 올렸습니다.");
        }
        else{
            resultMap.put("code", "500");
            resultMap.put("msg", "DB를 불러오지 못했습니다.");
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }
}