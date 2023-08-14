package com.ssafy.tab.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "MANAGER")
public class Manager {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MANAGER_NO")
    private Long id;

    @Column(name = "MANAGER_KEY",length = 30)

    private String key;

    @Column(name = "MANAGER_NAME",length = 20)
    private String name;

    @Column(name = "MANAGER_AREA",length = 20)
    private String area;

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "USER_NO")
    private User user;


}

