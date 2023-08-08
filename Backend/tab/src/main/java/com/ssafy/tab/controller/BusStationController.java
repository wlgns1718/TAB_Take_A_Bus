package com.ssafy.tab.controller;

import com.ssafy.tab.domain.BusAPI;
import com.ssafy.tab.service.BusStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class BusStationController {

    private final BusStationService busStationService;
    private int keyIndex = 0;
    @GetMapping("/api/stops/{cityCode}/{stationId}") // CITY_CODE = "37050", NODE_ID = "GMB383"
    public ResponseEntity<Map<String, Object>> searchBus(@PathVariable("cityCode") String cityCode, @PathVariable("stationId") String stationId) {

        Map<String, Object> resultMap = new HashMap<>();

        try {

            List<BusAPI> result = busStationService.findAll(cityCode, stationId, keyIndex);

            if (result != null && !result.isEmpty()) {
                resultMap.put("data", result);
                resultMap.put("code", "200");
                resultMap.put("msg", "해당 정류장 버스 정보를 성공적으로 가져왔습니다.");
            } else {
                resultMap.put("code", "401");
                resultMap.put("msg", "해당 정류장에 도착 예정인 버스가 없습니다");
            }
        } catch (IOException e) {
            keyIndex = (keyIndex + 1)%5; // key 호출횟수가 만료되면 키를 바꿈 (0, 1, 2, 3, 4, 0, 1, 2, 3, 4 ... 이렇게 순회)
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "해당 정류장 버스 정보를 가져오지 못했습니다.");
        } finally {
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }

    /*
    도시를 입력하면 그에 해당하는 버스 정류장을 DB에 저장해주는 REST API
     */
    @GetMapping("/api/station/{cityName}")
    public ResponseEntity<Map<String, Object>> StationData(@PathVariable("cityName") String cityName) {
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
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }

    /*
    정류장 번호를 입력했을 때 정류장 번호를 반환
     */
    @GetMapping("/api/presentstation/{stationNo}")
    public ResponseEntity<Map<String, Object>> presentStationData(@PathVariable("stationNo") String stationNo) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        String presentStationName = null;
        try{
            presentStationName = busStationService.presentStationName(stationNo);
            System.out.println(presentStationName);
            resultMap.put("code", "200");
            resultMap.put("msg", "성공적으로 정류장 이름을 불러왔습니다.");
            resultMap.put("presentStationName", presentStationName);
        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "현재 버스장 정보를 받아오지 못했습니다.");
        }finally {
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }
}