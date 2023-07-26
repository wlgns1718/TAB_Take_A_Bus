package com.ssafy.tab.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.tab.domain.Bus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class BusStationRepository {

    private static final String API_BASE_URL = "http://apis.data.go.kr";
    private static final String SERVICE_KEY1 = "9WA%2BHUFdIqFxDd8muGKZJDVzIL%2FBvVkrto0IUImEsO10U9o2pDVezaGaRz0q8M4FduewWGEB5DwcT0LHVBTkkA%3D%3D";
    private static final String SERVICE_KEY2 = "zkCODX6NOK7DinFf2%2FT%2F%2BZjMmV3bl1nrS19hmRFlQN6AIDc83oY3AspWzKXaV%2BFTzme8ixiMnpkTrpp6MEoh%2BA%3D%3D";
    private static final String TYPE_JSON = "json";
    private static final int NUM_OF_ROWS = 10;
    private static final int PAGE_NO = 1;
    private static String CITY_CODE;
    private static String NODE_ID;

    public List<Bus> findAll(String cityCode, String stationId) throws IOException {
        CITY_CODE = cityCode;
        NODE_ID = stationId;

        String apiUrl1 = API_BASE_URL + "/1613000/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList" +
                "?serviceKey=" + SERVICE_KEY1 +
                "&cityCode=" + CITY_CODE +
                "&nodeId=" + NODE_ID +
                "&numOfRows=" + NUM_OF_ROWS +
                "&pageNo=" + PAGE_NO +
                "&_type=" + TYPE_JSON;

        List<Bus> finalResult = new ArrayList<>();


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
                    setBus(finalResult, (int)m.get("arrprevstationcnt"), (int)m.get("arrtime"), m.get("routeid"), m.get("routeno"), m.get("routetp"), m.get("vehicletp"), m);
                }else if(items1.get("item") instanceof List){
                    List<Map<String, Object>> src = (List<Map<String, Object>>) items1.get("item");
                    for (Map<String, Object> m : src) {
                        setBus(finalResult, (int) m.get("arrprevstationcnt"), (int) m.get("arrtime"), m.get("routeid"), m.get("routeno"), m.get("routetp"), m.get("vehicletp"), m);
                    }
                }
            }
        } else {
            System.out.println("The first API call failed. Response code: " + responseCode1);
        }
        conn1.disconnect();

        return finalResult;
    }

    private void setBus(List<Bus> finalResult, int arrprevstationcnt, int arrtime, Object routeid, Object routeno, Object routetp, Object vehicletp, Map m) throws IOException {
        Bus bus = new Bus();
        bus.setRemainingStops(arrprevstationcnt);
        bus.setEta(arrtime);
        bus.setRouteId(routeid.toString());
        bus.setBusNo(routeno.toString());
        bus.setRouteType(routetp.toString());
        bus.setVehicleType(vehicletp.toString());

        String apiUrl2 = API_BASE_URL + "/1613000/BusLcInfoInqireService/getRouteAcctoBusLcList" +
                "?serviceKey=" + SERVICE_KEY2 +
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
                        bus.setStationOrder((int) stringObjectMap.get("nodeord"));
                        bus.setVehicleNo(stringObjectMap.get("vehicleno").toString());
                        bus.setStationId(stringObjectMap.get("nodeid").toString());
                        bus.setStationName(stringObjectMap.get("nodenm").toString());
                        finalResult.add(bus);
                    } else if (ob2 instanceof Map) {
                        Map<String, Object> tmp = (Map<String, Object>) ob2;
                        bus.setStationOrder((int) tmp.get("nodeord"));
                        bus.setVehicleNo(tmp.get("vehicleno").toString());
                        bus.setStationId(tmp.get("nodeid").toString());
                        bus.setStationName(tmp.get("nodenm").toString());
                        finalResult.add(bus);
                    }
                }

            }
        } else {
            System.out.println("The second API call failed. Response code: " + responseCode2);
        }
        conn2.disconnect();
    }
}
