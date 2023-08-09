package com.ssafy.tab.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BusApiTest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String routeId;
    String routeNo;
    
}
