package com.ssafy.tab.service;


import com.ssafy.tab.domain.Bus;
import com.ssafy.tab.repository.ArduinoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArduinoService {

    private final ArduinoRepository arduinoRepository;

    @Transactional(readOnly = true)

    public Optional<Bus> getInfo(String busNo) {
        //busNo로 정보 찾아오기
        Optional<Bus> bus = arduinoRepository.findById(busNo);
        return bus;
    }




}
