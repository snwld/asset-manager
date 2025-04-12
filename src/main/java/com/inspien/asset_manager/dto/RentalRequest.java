package com.inspien.asset_manager.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalRequest {
    private Long assetId;
    private String user;
    private LocalDateTime checkoutDate;
    private LocalDateTime dueDate;
}
