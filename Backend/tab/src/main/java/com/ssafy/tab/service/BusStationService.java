package com.ssafy.tab.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.ssafy.tab.domain.BusAPI;
import com.ssafy.tab.domain.BusStation;
import com.ssafy.tab.repository.BusStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BusStationService {

    private final BusStationRepository busStationRepository;

    private static final String API_BASE_URL = "http://apis.data.go.kr";

    @Value("#{'${public.api.key}'.split(',')}")
    private List<String> keyList;

    private static final String TYPE_JSON = "json";
    private static final int NUM_OF_ROWS = 10;
    private static final int PAGE_NO = 1;
    private static String CITY_CODE;
    private static String NODE_ID;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.hikari.username}")
    private String username;

    @Value("${spring.datasource.hikari.password}")
    private String password;

    final String CSV_FILE_PATH = "src/main/resources/2022년_전국버스정류장 위치정보_데이터.csv";
    final String TABLE_NAME = "bus_station";


    public List<BusAPI> findAll(String cityCode, String stationId, int keyIndex) throws IOException {
        CITY_CODE = cityCode;
        NODE_ID = stationId;
        String apiUrl1 = API_BASE_URL + "/1613000/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList" +
                "?serviceKey=" + keyList.get(keyIndex) +
                "&cityCode=" + CITY_CODE +
                "&nodeId=" + NODE_ID +
                "&numOfRows=" + NUM_OF_ROWS +
                "&pageNo=" + PAGE_NO +
                "&_type=" + TYPE_JSON;

        List<BusAPI> finalResult = new ArrayList<>();


        URL url1 = new URL(apiUrl1);
        HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
        conn1.setRequestMethod("GET");
        int responseCode1 = conn1.getResponseCode();
        if (responseCode1 == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in1 = new BufferedReader(new InputStreamReader(conn1.getInputStream()))) {
                StringBuilder response1 = new StringBuilder();
                String inputLine1;
                while ((inputLine1 = in1.readLine()) != null) {
                    response1.append(inputLine1);
                }
                ObjectMapper mapper1 = new ObjectMapper();
                Map<String, Object> result1 = mapper1.readValue(response1.toString(), Map.class);
                Map<String, Object> items = (Map<String, Object>) ((Map<String, Object>) result1.get("response")).get("body");
                LinkedHashMap items1 = (LinkedHashMap) items.get("items");
                if(items1.get("item") instanceof LinkedHashMap){ // 도착 예정버스의 갯수에 따라서 type이 달라짐, 예외 발생가능
                    LinkedHashMap m = (LinkedHashMap) items1.get("item");
                    setBus(finalResult, (int)m.get("arrprevstationcnt"), (int)m.get("arrtime"), m.get("routeid"), m.get("routeno"), m.get("routetp"), m.get("vehicletp"), m, keyIndex);
                }else if(items1.get("item") instanceof List){
                    List<Map<String, Object>> src = (List<Map<String, Object>>) items1.get("item");
                    for (Map<String, Object> m : src) {
                        setBus(finalResult, (int) m.get("arrprevstationcnt"), (int) m.get("arrtime"), m.get("routeid"), m.get("routeno"), m.get("routetp"), m.get("vehicletp"), m, keyIndex);
                    }
                }
            }
        } else {
            System.out.println("The first API call failed. Response code: " + responseCode1);
        }
        conn1.disconnect();

        return finalResult;
    }


    private void setBus(List<BusAPI> finalResult, int arrprevstationcnt, int arrtime, Object routeid, Object routeno, Object routetp, Object vehicletp, Map m, int keyIndex) throws IOException {
        BusAPI busAPI = new BusAPI();
        busAPI.setRemainingStops(arrprevstationcnt);
        busAPI.setEta(arrtime);
        busAPI.setRouteId(routeid.toString());
        busAPI.setBusNo(routeno.toString());
        busAPI.setRouteType(routetp.toString());
        busAPI.setVehicleType(vehicletp.toString());

        String apiUrl2 = API_BASE_URL + "/1613000/BusLcInfoInqireService/getRouteAcctoBusLcList" +
                "?serviceKey=" + keyList.get(keyIndex) +
                "&pageNo=" + PAGE_NO +
                "&numOfRows=" + NUM_OF_ROWS +
                "&_type=" + TYPE_JSON +
                "&cityCode=" + CITY_CODE +
                "&routeId=" + routeid;

        URL url2 = new URL(apiUrl2);
        HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
        conn2.setRequestMethod("GET");

        int responseCode2 = conn2.getResponseCode();

        if (responseCode2 == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in2 = new BufferedReader(new InputStreamReader(conn2.getInputStream()))) {
                StringBuilder response2 = new StringBuilder();
                String inputLine2;
                while ((inputLine2 = in2.readLine()) != null) {
                    response2.append(inputLine2);
                }


                ObjectMapper mapper2 = new ObjectMapper();
                Map<String, Object> result2 = mapper2.readValue(response2.toString(), Map.class);
                Map<String, Object> items2 = (Map<String, Object>) ((Map<String, Object>) result2.get("response")).get("body");


                Object items2obj = items2.get("items");
                if(items2obj instanceof LinkedHashMap){ // 도착 예정 버스가 없는 경우 Exception이 발생함
                    LinkedHashMap items3 = (LinkedHashMap) items2.get("items");

                    Object ob2 = items3.get("item");

                    if (ob2 instanceof List) {
                        List<Map<String, Object>> tmp = (List<Map<String, Object>>) ob2;
                        Map<String, Object> stringObjectMap = tmp.get(tmp.size() - 1);
                        busAPI.setStationOrder((int) stringObjectMap.get("nodeord"));
                        busAPI.setVehicleNo(stringObjectMap.get("vehicleno").toString());
                        busAPI.setStationId(stringObjectMap.get("nodeid").toString());
                        busAPI.setStationName(stringObjectMap.get("nodenm").toString());
                        finalResult.add(busAPI);
                    } else if (ob2 instanceof Map) {
                        Map<String, Object> tmp = (Map<String, Object>) ob2;
                        busAPI.setStationOrder((int) tmp.get("nodeord"));
                        busAPI.setVehicleNo(tmp.get("vehicleno").toString());
                        busAPI.setStationId(tmp.get("nodeid").toString());
                        busAPI.setStationName(tmp.get("nodenm").toString());
                        finalResult.add(busAPI);
                    }
                }

            }
        } else {
            System.out.println("The second API call failed. Response code: " + responseCode2);
        }
        conn2.disconnect();
    }

    @Transactional
    public boolean busStationData(String cityName){
        try (
            Connection connection = DriverManager.getConnection(url, username, password);
            CSVReader reader = new CSVReader(new FileReader(CSV_FILE_PATH))) {
            Class.forName("com.mysql.cj.jdbc.Driver");

            //미리 테이블 한 번 초기화해서 중복 제거
            String truncateSQL = "DELETE FROM bus_station";
            PreparedStatement preparedStatement = connection.prepareStatement(truncateSQL);

            preparedStatement.executeUpdate();
            
            //INSERT할 쿼리
            String insertSQL = "INSERT INTO " + TABLE_NAME + " (station_no, city_code, city_name, latitude, longtitude, station_name) VALUES (?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(insertSQL);

            String[] nextLine;
            reader.readNext(); // Skip header row
            while ((nextLine = reader.readNext()) != null) {
                String city_name = nextLine[7]; // 도시명
                if (!city_name.equals(cityName)) continue;
                String station_no = nextLine[0]; // 정류장 번호
                String station_name = nextLine[1]; // 정류장명
                String city_code = nextLine[6]; // 도시코드
                double latitude = Double.parseDouble(nextLine[2]); // 위도
                double longitude = Double.parseDouble(nextLine[3]); // 경도
                preparedStatement.setString(1, station_no);
                preparedStatement.setString(2, city_code);
                preparedStatement.setString(3, city_name);
                preparedStatement.setDouble(4, latitude);
                preparedStatement.setDouble(5, longitude);
                preparedStatement.setString(6, station_name);
                preparedStatement.addBatch();
            }
            int[] batchResult = preparedStatement.executeBatch();

            int totalRecordsInserted = 0;
            for (int count : batchResult) {
                totalRecordsInserted += count;
            }
            return true;
        } catch (ClassNotFoundException | SQLException | IOException | CsvValidationException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public String presentStationName(String stationNo) {
        BusStation busStation = busStationRepository.findById(stationNo).get();
        return busStation.getStationName();
    }
}
