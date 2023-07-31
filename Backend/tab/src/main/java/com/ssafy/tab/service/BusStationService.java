package com.ssafy.tab.service;

import com.ssafy.tab.domain.BusAPI;
import com.ssafy.tab.repository.BusStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BusStationService {

    private final BusStationRepository busStationRepository;

    public List<BusAPI> findAll(String cityCode, String stationId) throws IOException {
        return busStationRepository.findAll(cityCode,stationId);
    }
}
