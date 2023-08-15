package com.ssafy.tab.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.tab.domain.*;
import com.ssafy.tab.repository.BusTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BusTestService {

    private final BusTestRepository busTestRepository;

    final String API_BASE_URL = "http://apis.data.go.kr";

    @Value("#{'${public.api.key}'.split(',')}")
    private List<String> keyList;

    //현재 버스 정류장에 정차하는 모든 노선에 저장 후 모든 버스를 반환.
    public List<Map<String, String>> saveAllBus(String cityCode, String nodeId, int keyIndex) throws IOException, JsonProcessingException {
        busTestRepository.deleteAll();
        String apiUrl = API_BASE_URL + "/1613000/BusSttnInfoInqireService/getSttnThrghRouteList" +
                "?serviceKey=" + keyList.get(keyIndex)  +
                "&cityCode=" + cityCode +
                "&nodeid=" + nodeId +
                "&numOfRows=" + "1000" +
                "&pageNo=" + "1" +
                "&_type=" + "json";
        List<Map<String, String>> result = new ArrayList<>();
            String apiResponse = callApi(apiUrl);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(apiResponse);
            JsonNode items = jsonResponse.path("response").path("body").path("items").path("item");
            for (JsonNode item : items) {
                Map<String, String> temp = new HashMap<>();
                temp.put("routeId", item.path("routeid").asText());
                temp.put("routeNo", item.path("routeno").asText());
                result.add(temp);
            }
        return result;
    }


    //각 노선에 대한 정보 저장
    public List<BusTest> saveAllBusStation(String cityCode, String routeId, String routeNo, int keyIndex) throws IOException, JsonProcessingException {
        String apiUrl = API_BASE_URL + "/1613000/BusRouteInfoInqireService/getRouteAcctoThrghSttnList" +
                "?serviceKey=" + keyList.get(keyIndex) +
                "&cityCode=" + cityCode +
                "&routeId=" + routeId +
                "&numOfRows=" + "1000" +
                "&pageNo=" + "1" +
                "&_type=" + "json";
        List<BusTest> result = new ArrayList<>();
            String apiResponse = callApi(apiUrl);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(apiResponse);
            JsonNode items = jsonResponse.path("response").path("body").path("items").path("item");
            for (JsonNode item : items) {
                Double latitude = item.path("gpslati").asDouble();
                Double longtitude = item.path("gpslong").asDouble();
                String stationId = item.path("nodeid").asText();
                int orderStop = item.path("nodeord").asInt();
                String stationName = item.path("nodenm").asText();
                BusTest bus = new BusTest();
                bus.setRouteNo(routeNo);
                bus.setRouteId(routeId);
                bus.setLatitude(latitude);
                bus.setLongtitude(longtitude);
                bus.setStationId(stationId);
                bus.setOrderStop(orderStop);
                bus.setStationName(stationName);
                busTestRepository.save(bus);
                result.add(bus);
            }
        return result;
    }

    //현재 정류장에서 도착 예정인 노선 불러오기.
    public List<BustestApi> findAllInfo(String cityCode, String nodeId, int keyIndex) throws IOException, JsonProcessingException {
        List<BustestApi> bustestApiList = new ArrayList<>();
        String apiUrl = API_BASE_URL + "/1613000/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList" +
                "?serviceKey=" + keyList.get(keyIndex) +
                "&cityCode=" + cityCode +
                "&nodeId=" + nodeId +
                "&numOfRows=" + "1000" +
                "&pageNo=" + "1" +
                "&_type=" + "json";
            String apiResponse = callApi(apiUrl);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(apiResponse);
            JsonNode items = jsonResponse.path("response").path("body").path("items").path("item");
            //하나의 버스만 있을 경우와 여러 대의 버스가 있을 경우를 따로 처리해줘야함.
            if (items.isArray()) {
                for (JsonNode item : items) {
                    BustestApi bustestApi = BustestApi.builder()
                            .remainingStops(item.path("arrprevstationcnt").asInt())
                            .eta(item.path("arrtime").asInt())
                            .routeId(item.path("routeid").asText())
                            .routeNo(item.path("routeno").asText())
                            .busNo(item.path("routeno").asText())
                            .routeType(item.path("routetp").asText())
                            .vehicleType(item.path("vehicletp").asText())
                            .stationId(nodeId)
                            .build();
                    BustestApi tempBusTestApi = findPresentLocation(bustestApi);
                    if (tempBusTestApi.getVehicleNo() == null) {
                        BustestApi result = findVehicleNo(tempBusTestApi, cityCode, keyIndex);
                        bustestApiList.add(result);
                    } else {
                        bustestApiList.add(tempBusTestApi);
                    }
                }
            } else if (items.isObject()) {
                // Process as a single object
                BustestApi bustestApi = BustestApi.builder()
                        .remainingStops(items.path("arrprevstationcnt").asInt())
                        .eta(items.path("arrtime").asInt())
                        .routeId(items.path("routeid").asText())
                        .routeNo(items.path("routeno").asText())
                        .busNo(items.path("routeno").asText())
                        .routeType(items.path("routetp").asText())
                        .vehicleType(items.path("vehicletp").asText())
                        .stationId(nodeId)
                        .build();
                BustestApi tempBusTestApi = findPresentLocation(bustestApi);
                if (tempBusTestApi.getVehicleNo() == null) {
                    BustestApi result = findVehicleNo(tempBusTestApi, cityCode, keyIndex);
                    bustestApiList.add(result);
                } else {
                    bustestApiList.add(tempBusTestApi);
                }
            }
        return bustestApiList;
    }

    //버스 번호와 몇 정거장 남았는지를 DB에 조회해서 현재 위치 가져오기
    public BustestApi findPresentLocation(BustestApi bustestApi) {
        BusTest findBusTest = busTestRepository.findByRouteNoAndStationIdAndRouteId(bustestApi.getRouteNo(), bustestApi.getStationId(), bustestApi.getRouteId());
        List<BusTest> ListBusTest = busTestRepository.findByRouteNoAndRouteId(bustestApi.getRouteNo(), bustestApi.getRouteId());
        int temp = 0;
        int presentOrder = 0;
        for (BusTest busToBusStation : ListBusTest) {
            if (busToBusStation.getStationName().equals(findBusTest.getStationName())) {
                temp = busToBusStation.getOrderStop();
                presentOrder = temp - bustestApi.getRemainingStops();
                break;
            }
        }
        BusTest tempbus = busTestRepository.findByRouteNoAndOrderStopAndRouteId(bustestApi.getRouteNo(), presentOrder, bustestApi.getRouteId());
        bustestApi.setLatitude(tempbus.getLatitude());
        bustestApi.setLongtitude(tempbus.getLongtitude());
        bustestApi.setStationOrder(presentOrder);
        bustestApi.setStationId(tempbus.getStationId());
        bustestApi.setStationName(tempbus.getStationName());
        return bustestApi;
    }

    public BustestApi findVehicleNo(BustestApi bustestApi, String cityCode, int keyIndex) {
        String apiUrl = API_BASE_URL + "/1613000/BusLcInfoInqireService/getRouteAcctoBusLcList" +
                "?serviceKey=" + keyList.get(keyIndex) +
                "&cityCode=" + cityCode +
                "&routeId=" + bustestApi.getRouteId() +
                "&numOfRows=" + "1000" +
                "&pageNo=" + "1" +
                "&_type=" + "json";
        try {
            String apiResponse = callApi(apiUrl);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(apiResponse);
            JsonNode items = jsonResponse.path("response").path("body").path("items").path("item");
            if (items.isArray()) {
                for (JsonNode item : items) {
                    String nodenm = item.path("nodenm").asText();
                    if (nodenm.equals(bustestApi.getStationName())) {
                        bustestApi.setVehicleNo(item.path("vehicleno").asText());
                        break;
                    }
                }
            } else if (items.isObject()) {
                // Process as a single object
                String nodenm = items.path("nodenm").asText();
                if (nodenm.equals(bustestApi.getStationName())) {
                    bustestApi.setVehicleNo(items.path("vehicleno").asText());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bustestApi;
    }

    //api 불러오는 로직
    public String callApi(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } else {
            return null;
        }
    }

    public List<Map<String, Object>> getTripInfo(String cityCode, String routeId, int tripType) {

        //공공데이터 포털 Key
        String key = "5ts%2Baf9Tv7mT28mcFD0Y8pzBg7sy1TYdLve4W7vJd5pt44kEEAkpi8AbNEVKnb%2Fk2z79M9WDxTozeVzNWlPkdA%3D%3D";

        //여행지 정보 담기
        List<Map<String, Object>> resultMap = new ArrayList<>();

        // 1. cityCode와 routeId로 해당 버스가 갈 수 있는 모든 정류장 추출
        // 2. 모든 정류장에서 몇 정거장마다 위도,경도 추출
        // 3. 해당 위도, 경도 List를 사용해 tripType을 이용해 HashMap을 사용하여 중복된 정보 없이 데이터 반환

        //1. cityCode와 routeId로 해당 버스가 갈수 있는 정류장 추출
        List<Map<String, String>> busStations = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("https://apis.data.go.kr/1613000/BusRouteInfoInqireService/getRouteAcctoThrghSttnList?serviceKey=");
        sb.append(key);
        sb.append("&pageNo=1&numOfRows=100&_type=json&cityCode=");
        sb.append(cityCode);
        sb.append("&routeId=");
        sb.append(routeId);
        try {
            URL apiUrl = new URL(sb.toString());
            HttpURLConnection conn = (HttpURLConnection) apiUrl.openConnection();

            //요청 방식 지정
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");

            //결과 받아오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = br.readLine()) != null) { //응답값 모두 받아오기
                response.append(inputLine);
            }
            br.close();

            //받아온 값 String으로 변환
            String tempResponse = response.toString();

            //각 요소 분리하기, items로 부터 +7이 item요소 시작
            int index = tempResponse.indexOf("itmes") + 7;

            //parsing을 위한 string 생성
            String getResponse = tempResponse.substring(index);
            //Json객체로 생성



        } catch (Exception e) {
            e.printStackTrace();
        }
        //============================수정
        return resultMap;
    }


}
