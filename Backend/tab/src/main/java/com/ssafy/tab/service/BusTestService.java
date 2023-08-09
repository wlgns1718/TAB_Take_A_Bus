package com.ssafy.tab.service;

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

    String SERVICE_KEY = "zkCODX6NOK7DinFf2%2FT%2F%2BZjMmV3bl1nrS19hmRFlQN6AIDc83oY3AspWzKXaV%2BFTzme8ixiMnpkTrpp6MEoh%2BA%3D%3D";

    //현재 버스 정류장에 정차하는 모든 노선에 저장 후 모든 버스를 반환.
    public List<Map<String, String>> saveAllBus(String cityCode, String nodeId) {
        String apiUrl = API_BASE_URL + "/1613000/BusSttnInfoInqireService/getSttnThrghRouteList" +
                "?serviceKey=" + SERVICE_KEY +
                "&cityCode=" + cityCode +
                "&nodeid=" + nodeId +
                "&numOfRows=" + "1000" +
                "&pageNo=" + "1" +
                "&_type=" + "json";
        List<Map<String, String>> result = new ArrayList<>();
        try {
            String apiResponse = callApi(apiUrl);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(apiResponse);
            JsonNode items = jsonResponse.path("response").path("body").path("items").path("item");
            for (JsonNode item : items) {
                Map<String ,String> temp = new HashMap<>();
                temp.put("routeId", item.path("routeid").asText());
                temp.put("routeNo", item.path("routeno").asText());
                result.add(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    //각 노선에 대한 정보 저장
    public List<BusTest> saveAllBusStation(String cityCode, String routeId, String routeNo,int keyIndex) {
        String apiUrl = API_BASE_URL + "/1613000/BusRouteInfoInqireService/getRouteAcctoThrghSttnList" +
                "?serviceKey=" + keyList.get(keyIndex) +
                "&cityCode=" + cityCode +
                "&routeId=" + routeId +
                "&numOfRows=" + "1000" +
                "&pageNo=" + "1" +
                "&_type=" + "json";
        List<BusTest> result = new ArrayList<>();
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //현재 정류장에서 도착 예정인 노선 불러오기.
    public List<BustestApi> findAllInfo(String cityCode, String nodeId, String stationName,int keyIndex) {
        List<BustestApi> bustestApiList = new ArrayList<>();
        String apiUrl = API_BASE_URL + "/1613000/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList" +
                "?serviceKey=" + keyList.get(keyIndex) +
                "&cityCode=" + cityCode +
                "&nodeId=" + nodeId +
                "&numOfRows=" + "1000" +
                "&pageNo=" + "1" +
                "&_type=" + "json";
        try {
            String apiResponse = callApi(apiUrl);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(apiResponse);
            JsonNode items = jsonResponse.path("response").path("body").path("items").path("item");
            for (JsonNode item : items) {
                BustestApi bustestApi = BustestApi.builder()
                        .remainingStops(item.path("arrprevstationcnt").asInt())
                        .eta(item.path("arrtime").asInt())
                        .routeId(item.path("routeid").asText())
                        .routeNo(item.path("routeno").asText())
                        .busNo(item.path("routeno").asText())
                        .routeType(item.path("routetp").asText())
                        .vehicleType(item.path("vehicletp").asText())
                        .stationName(stationName)
                        .build();
                BustestApi tempBusTestApi = findPresentLocation(bustestApi);
                if(tempBusTestApi.getVehicleNo()==null){
                    BustestApi result = findVehicleNo(tempBusTestApi, cityCode);
                    bustestApiList.add(result);
                }else{
                    bustestApiList.add(tempBusTestApi);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bustestApiList;
    }
    //버스 번호와 몇 정거장 남았는지를 DB에 조회해서 현재 위치 가져오기
    public BustestApi findPresentLocation(BustestApi bustestApi){
        System.out.println(bustestApi.toString());
        BusTest findBusTest = busTestRepository.findByRouteNoAndStationNameAndRouteId(bustestApi.getRouteNo(), bustestApi.getStationName(), bustestApi.getRouteId());
        List<BusTest> ListBusTest = busTestRepository.findByRouteNoAndRouteId(bustestApi.getRouteNo(), bustestApi.getRouteId());
        int temp = 0;
        int presentOrder = 0;
        for (BusTest busToBusStation : ListBusTest) {
            if(busToBusStation.getStationName().equals(findBusTest.getStationName())){
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

    public BustestApi findVehicleNo(BustestApi bustestApi, String cityCode) {
        String apiUrl = API_BASE_URL + "/1613000/BusLcInfoInqireService/getRouteAcctoBusLcList" +
                "?serviceKey=" + SERVICE_KEY +
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
                    if(nodenm.equals(bustestApi.getStationName())){
                        bustestApi.setVehicleNo(item.path("vehicleno").asText());
                        break;
                    }
                }
            } else if (items.isObject()) {
                // Process as a single object
                String nodenm = items.path("nodenm").asText();
                if(nodenm.equals(bustestApi.getStationName())){
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
}
