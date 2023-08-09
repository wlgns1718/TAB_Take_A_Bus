package com.ssafy.tab.controller;

import com.ssafy.tab.domain.BusAPI;
import com.ssafy.tab.domain.BusTest;
import com.ssafy.tab.domain.BustestApi;
import com.ssafy.tab.service.BusStationService;
import com.ssafy.tab.service.BusTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequiredArgsConstructor
@RequestMapping("/tab/station")
@CrossOrigin("*")
public class BusStationController {

    private final BusStationService busStationService;
    private final BusTestService busTestService;
    private int keyIndex = 0;

    /*
    도시를 입력하면 그에 해당하는 버스 정류장을 DB에 저장해주는 REST API
     */
    @GetMapping("/{cityName}")
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
    정류장 번호를 입력했을 때 정류장 이름을 반환
     */
    @GetMapping("/present/{stationNo}")
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
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "현재 버스장 정보를 받아오지 못했습니다.");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/{cityCode}/{stationId}") // CITY_CODE = "37050", NODE_ID = "GMB383"
        public ResponseEntity<Map<String, Object>> storeBusStationData(@PathVariable("cityCode") String cityCode, @PathVariable("stationId") String stationId) throws IOException {
            Map<String, Object> resultMap = new HashMap<>();
            //모든 노선을 저장.
            try{
                List<Map<String, String>> busTestList = busTestService.saveAllBus(cityCode, stationId);
                //각 노선에 대한 정류장 정보 저장
                for (Map<String, String> stringStringMap : busTestList) {
                    String routeId = stringStringMap.get("routeId");
                    String routeNo = stringStringMap.get("routeNo");
                    List<BusTest> busTest = busTestService.saveAllBusStation(cityCode, routeId, routeNo,keyIndex);
                resultMap.put("code", "200");
                resultMap.put("msg", "성공적으로 정류장 이름을 불러왔습니다.");
            }
        } catch (Exception e){
                keyIndex = (keyIndex + 1)%5;
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "현재 버스정류장에 정차하는 데이터를 받아오지 못했습니다.");
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{cityCode}/{stationId}/{stationName}") // CITY_CODE = "37050", NODE_ID = "GMB383"
    public ResponseEntity<Map<String, Object>> StationAPI(@PathVariable("cityCode") String cityCode, @PathVariable("stationId") String stationId, @PathVariable("stationName") String stationName) throws IOException {
        Map<String, Object> resultMap = new HashMap<>();
        //모든 노선을 저장.
        try{
            List<BustestApi> allInfo = busTestService.findAllInfo(cityCode, stationId, stationName,keyIndex);
            resultMap.put("code", "200");
            resultMap.put("msg", "성공적으로 API를 호출하였습니다.");
            resultMap.put("data", allInfo);
        } catch (Exception e){
            keyIndex = (keyIndex + 1)%5;
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "API를 호출하는데 문제가 발생하였습니다.");
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }
}