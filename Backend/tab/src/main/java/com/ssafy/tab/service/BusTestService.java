package com.ssafy.tab.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.tab.domain.*;
import com.ssafy.tab.dto.TripInfoDto;
import com.ssafy.tab.repository.BusTestRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import org.json.simple.*;
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

    public Map<String,Object> getTripInfo(String cityCode, String routeId, String tripType,int keyIndex)throws JsonProcessingException {

        //공공데이터 포털 Key
        String key = keyList.get(keyIndex);
        //여행지 정보 담기
        Map<String,Object> tripInfos = new HashMap<>();

        // 1. cityCode와 routeId로 해당 버스가 갈 수 있는 모든 정류장 추출
        // 2. 모든 정류장에서 몇 정거장마다 위도,경도 추출
        // 3. 해당 위도, 경도 List를 사용해 tripType을 이용해 HashMap을 사용하여 중복된 정보 없이 데이터 반환

        //1. cityCode와 routeId로 해당 버스가 갈수 있는 정류장 추출
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

            String inputLine = "";
            inputLine = br.readLine();
            //불러온 값을 JOSN형태로 바꿔주기 위한 객체 생성
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(inputLine);
            br.close();
            //불러온 값에서 key = response인 값을 불러오기
            JSONObject response = (JSONObject) jsonObject.get("response");
            System.out.println("=============================================");
            System.out.println(response);
            //해당 routeId가 갈 수 있는 모든 정류장 가져오기
            JSONObject items = (JSONObject) ((JSONObject) response.get("body")).get("items");

            //해당 값들 리스트 형태로 전환
            JSONArray jsonArray = (JSONArray) items.get("item");
            List<String[]> gpsInfo = new ArrayList<>();
            double gpsx = 1000;
            double gpsy = 1000;
            double temp_gpsx = 0;
            double temp_gpsy = 0;

            for(int i = 0; i < jsonArray.size(); i++){//거리가 1km 이상만 담기
                JSONObject bus_info = (JSONObject) jsonArray.get(i);
                //위도 경도만 나타내기
                String gpslong = String.valueOf(bus_info.get("gpslong"));
                String gpslati = String.valueOf(bus_info.get("gpslati"));
                temp_gpsx = Double.parseDouble(gpslong);
                temp_gpsy = Double.parseDouble(gpslati);
                //계산 로직
                if (gpsx == 1000 && gpsy == 1000){ // 초기값 설정
                    gpsx = temp_gpsx;
                    gpsy = temp_gpsy;
                    gpsInfo.add(new String[] {String.valueOf(bus_info.get("nodenm")),String.valueOf(gpsy),String.valueOf(gpsx)});
                    continue;
                }
                else{
                    //두 거리 계산
                    double dist = distance(gpsy,gpsx,temp_gpsy,temp_gpsx);
                    //System.out.println("두 지점 사이의 거리는 : " + dist+"Km");
                    if(dist > 1){
                        gpsy = temp_gpsy;
                        gpsx = temp_gpsx;
                        gpsInfo.add(new String[] {String.valueOf(bus_info.get("nodenm")),String.valueOf(gpsy),String.valueOf(gpsx)});
                    }
                }
            }
            //gpsInfo로 관광지 정보 불러오기
            tripInfos = findAllTripInfo(gpsInfo,keyIndex,tripType);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return tripInfos;
    }
    public Map<String,Object> findAllTripInfo(List<String[]> infos,int keyIndex,String tripType){
        Map<String,Object> list = new HashMap<>();
        URL apiurl = null;
        HttpURLConnection conn = null;
        BufferedReader br = null;
        JSONParser jsonParser = new JSONParser();
        System.out.println("infos정보 찾기");
//        System.out.println(infos.toString());
        for(String[] temp : infos){
            System.out.println(Arrays.toString(temp));
        }
        for (String[] gps : infos) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("https://apis.data.go.kr/B551011/KorService1/locationBasedList1?serviceKey=");
                sb.append(keyList.get(keyIndex));
                sb.append("&numOfRows=20&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&listYN=Y&arrange=A&mapX=");
                sb.append(gps[2]);
                sb.append("&mapY=");
                sb.append(gps[1]);
                sb.append("&radius=1000&contentTypeId=");
                sb.append(tripType);

                apiurl = new URL(sb.toString());
                conn = (HttpURLConnection) apiurl.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
                br = new BufferedReader(new InputStreamReader(apiurl.openStream(), "UTF-8"));
                String inputLine = "";
                inputLine = br.readLine();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(inputLine);
                JSONObject response = (JSONObject) jsonObject.get("response");
                JSONObject body = (JSONObject) response.get("body");
                if(String.valueOf(body.get("items")).equals("")){
                    continue;
                }
                JSONObject items = (JSONObject) body.get("items");
                JSONArray jsonArray = (JSONArray) items.get("item");

                List<TripInfoDto> trips = new ArrayList<>();
                for(int i = 0; i<jsonArray.size(); i++){
                    JSONObject json = (JSONObject) jsonArray.get(i);
                    TripInfoDto tripDto = new TripInfoDto();
                    tripDto.setFirstimage(String.valueOf(json.get("firstimage")));
                    tripDto.setAddr1(String.valueOf(json.get("addr1")));
                    tripDto.setMapx(String.valueOf(json.get("mapx")));
                    tripDto.setMapy(String.valueOf(json.get("mapy")));
                    tripDto.setTel(String.valueOf(json.get("tel")));
                    tripDto.setTitle(String.valueOf(json.get("title")));
                    tripDto.setFirstimage2(String.valueOf(json.get("firstimage2")));
                    trips.add(tripDto);
                }
                System.out.println(gps[0]);
                list.put(gps[0],trips);
            }catch (NullPointerException e){
                e.printStackTrace();
                continue;
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return list;
    }
    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        //킬로미터로 변환
        dist = dist * 1.609344;
        return dist;
    }
    // degree를 라디안으로 변환
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // 라디안을 degree로 변환
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}
