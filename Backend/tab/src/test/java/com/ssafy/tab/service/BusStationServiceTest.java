package com.ssafy.tab.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional()
class BusStationServiceTest {

    @Autowired BusStationService busStationService;


    @Test
    public void addData() throws Exception{
        //given

        boolean test = busStationService.busStationData("구미시");
        Assertions.assertThat(test).isEqualTo(true);

        //when

        //then
    }
}