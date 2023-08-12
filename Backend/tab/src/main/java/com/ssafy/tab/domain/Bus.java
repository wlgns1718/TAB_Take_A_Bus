package com.ssafy.tab.domain;


import lombok.Getter;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Bus {

    /*
    id : 버스 차량 번호
    routeNo : 버스 노선 번호
    createDate : 날짜
    stationId : 버스정류장 번호
    vulnerable : 교통약자 여부
     */
    @Id
    @Column(name = "VHHICLE_NO", length = 20)
    private String id;

    @Column(name = "ROUTE_NO", length = 20)
    private String routeNo;

    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;

    @Column(name = "STAION_ID", length = 20)
    private String stationId;

    @Column(name = "VULNERABLE")
    private boolean vulnerable;

}
