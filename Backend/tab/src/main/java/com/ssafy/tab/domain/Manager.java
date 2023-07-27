package com.ssafy.tab.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Manager {

    @Id @GeneratedValue
    @Column(name = "MANAGER_NO")
    private Long id;

    @Column(name = "MANAGER_KEY",length = 30)
    private String key;

    @Column(name = "MANAGER_NAME",length = 20)
    private String name;

    @Column(name = "MANAGER_AREA",length = 20)
    private String area;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private User user;


}
