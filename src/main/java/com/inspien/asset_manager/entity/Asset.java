package com.inspien.asset_manager.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    private AssetStatus status;

    private String cpu;
    private String memory;
    private String hdd;


    @Column(columnDefinition = "TEXT")
    private String memo;
}
