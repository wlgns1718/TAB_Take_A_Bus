package com.ssafy.tab.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.tab.domain.BustestApi;
import com.ssafy.tab.service.BusStationService;
import com.ssafy.tab.service.BusTestService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
    @ApiOperation(value = "도시에 해당하는 모든 버스 정류장 DB 저장", notes = "도시 이름은 한글로 CSV에 파일에 명시된 대로 작성해야 불러올 수 있습니다. /구미시", response = Map.class)
    @GetMapping("/{cityName}")
    public ResponseEntity<Map<String, Object>> StationData(@PathVariable("cityName") @ApiParam(value = "도시 한글 이름", required = true) String cityName) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try{
            if(busStationService.busStationData(cityName)){
                resultMap.put("code", "200");
                resultMap.put("msg", "성공적으로 " +  cityName + "에 해당하는 정류장 데이터를 DB에 올렸습니다.");
            }else{
                resultMap.put("code", "500");
                resultMap.put("msg", "해당 하는 도시의 이름을 다시 입력해주세요.");
            }
        } catch (Exception e){
            String message = e.getMessage();
            resultMap.put("code", "500");
            resultMap.put("msg", "DB를 불러오는 도중 오류가 발생했습니다.");
            resultMap.put("errorMessage", message);
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }

    /*
    정류장 번호를 입력했을 때 정류장 이름을 반환
     */
    @ApiOperation(value = "정류장 번호를 입력했을 때 정류장 이름을 반환", notes = "정류장 코드를 입력했을 때 DB에서 그에 매핑 되는 정류장 한글 이름을 반환합니다. /present/GMB383", response = Map.class)
    @GetMapping("/present/{stationId}")
    public ResponseEntity<Map<String, Object>> presentStationData(@PathVariable("stationId") @ApiParam(value = "정류장 고유 코드", required = true)String stationId) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        String presentStationName = null;
        try{
            presentStationName = busStationService.presentStationName(stationId);
            System.out.println(presentStationName);
            resultMap.put("code", "200");
            resultMap.put("msg", "성공적으로 정류장 이름을 불러왔습니다.");
            resultMap.put("presentStationName", presentStationName);
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }catch (Exception e){
            String message = e.getMessage();
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "현재 버스장 정보를 받아오지 못했습니다.");
            resultMap.put("errorMessage", message);
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }

    @ApiOperation(value = "현재 버스정류장에 정차하는 노선의 데이터를 DB에 저장합니다.", notes = "/37050/GMB383", response = Map.class)
    @GetMapping("/{cityCode}/{stationId}") // CITY_CODE = "37050", NODE_ID = "GMB383"
        public ResponseEntity<Map<String, Object>> storeBusStationData(@PathVariable("cityCode") @ApiParam(value = "도시코드", required = true) String cityCode, @PathVariable("stationId") @ApiParam(value = "정류장 고유 번호", required = true) String stationId) throws IOException {
        Map<String, Object> resultMap = new HashMap<>();
        //모든 노선을 저장.
        try {
            List<Map<String, String>> busTestList = busTestService.saveAllBus(cityCode, stationId, keyIndex);
            //각 노선에 대한 정류장 정보 저장
            for (Map<String, String> stringStringMap : busTestList) {
                String routeId = stringStringMap.get("routeId");
                String routeNo = stringStringMap.get("routeNo");
                busTestService.saveAllBusStation(cityCode, routeId, routeNo, keyIndex);
                resultMap.put("code", "200");
                resultMap.put("msg", "성공적으로 현재 버스정류장에 정차하는 노선의 정보를 DB에 저장했습니다.");
            }
        } catch (JsonProcessingException e) {
            String message = e.getMessage();
            keyIndex = (keyIndex + 1) % 5;
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "API키가 만료 되었습니다. 다시 요청하면 자동으로 키가 바뀝니다. 다시 요청하세요.");
            resultMap.put("errorMessage", message);
        } catch (IOException e) {
            String message = e.getMessage();
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "API 호출에 실패하였습니다.");
            resultMap.put("errorMessage", message);
        }
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }

    @ApiOperation(value = "실시간으로 버스 정류장에 도착하는 버스 호출 API.", notes = "VehicleNo가 null이 될 수 있으니 저장하는 것이 좋습니다.  /37050/GMB383/삼성전자후문", response = Map.class)
    @GetMapping("/{cityCode}/{stationId}/api") // CITY_CODE = "37050", NODE_ID = "GMB383"
    public ResponseEntity<Map<String, Object>> StationAPI(@PathVariable("cityCode") @ApiParam(value = "도시코드", required = true) String cityCode, @PathVariable("stationId") @ApiParam(value = "정류장 고유 번호", required = true) String stationId) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        //모든 노선을 저장.
        try{
            List<BustestApi> allInfo = busTestService.findAllInfo(cityCode, stationId, keyIndex);
            resultMap.put("code", "200");
            resultMap.put("msg", "성공적으로 API를 호출하였습니다.");
            resultMap.put("data", allInfo);
        } catch (JsonProcessingException e) {
            String message = e.getMessage();
            keyIndex = (keyIndex + 1) % 5;
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "API키가 만료 되었습니다. 다시 요청하면 자동으로 키가 바뀝니다. 다시 요청하세요.");
            resultMap.put("errorMessage", message);
        } catch (IOException e) {
            String message = e.getMessage();
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "API 호출에 실패하였습니다.");
            resultMap.put("errorMessage", message);
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }

    //여행 관련 API 작성, routeId는 해당 버스의 노선 아이디, 프론트단에서 보여줄 땐 routeno을 보여주기(ex, 388)
    //@GetMapping("/trip/{cityCode}/{routeId}/{tripType}")
 }
