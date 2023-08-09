package com.ssafy.tab.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.ssafy.tab.domain.BusStation;
import com.ssafy.tab.repository.BusStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
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

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.hikari.username}")
    private String username;

    @Value("${spring.datasource.hikari.password}")
    private String password;

    final String CSV_FILE_PATH = "src/main/resources/2022년_전국버스정류장 위치정보_데이터.csv";
    final String TABLE_NAME = "bus_station";


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
