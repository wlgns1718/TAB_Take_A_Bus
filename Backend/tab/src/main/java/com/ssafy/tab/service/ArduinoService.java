package com.ssafy.tab.service;


import com.ssafy.tab.domain.Bus;
import com.ssafy.tab.domain.BusData;
import com.ssafy.tab.domain.BusStation;
import com.ssafy.tab.dto.BusDataDto;
import com.ssafy.tab.dto.BusDto;
import com.ssafy.tab.repository.ArduinoRepository;
import com.ssafy.tab.repository.BusDataRepository;
import com.ssafy.tab.repository.BusRepository;
import com.ssafy.tab.repository.BusStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArduinoService {

    private final ArduinoRepository arduinoRepository;
    private final BusDataRepository busDataRepository;
    private final BusStationRepository busStationRepository;
    private final BusRepository busRepository;
    @Transactional(readOnly = true)
    public Optional<Bus> getInfo(String vehicleNo) {
        //busNo로 정보 찾아오기
        Optional<Bus> bus = busRepository.findByVehicleNo(vehicleNo);
        return bus;
    }


    public void createBus(BusDataDto busDataDto) {
        //BusStaion에 있는 Station_No FK를 참고해서 BusStation 객체 불러오기
        //Front에서 정류장Id, 승차인원, 차량 번호, 노션ID ,교통약자
        //DB의 BusData정보 busdata_no, boarding_time, count, route_no, vehicle_no, vulerable, station_no
        try {
            //아두이노 통신을 위한 데이터 넣어주기
            BusDto busDto = BusDto.toEntity(busDataDto);
            Bus bus = Bus.toEntity(busDto);
            busRepository.save(bus);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void deleteInfo(Long id) {
        busRepository.delete(busRepository.findById(id).get());
    }
}
