package com.inspien.asset_manager.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 자산을 빌렸는지
    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    // 대여한 사원 이름
    private String user;

    // 대여일
    private LocalDateTime checkoutDate;

    // 반납 예정일
    private LocalDateTime dueDate;

    // 실제 반납일 (nullable)
    private LocalDateTime returnDate;
}
