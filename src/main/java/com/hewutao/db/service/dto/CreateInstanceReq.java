package com.hewutao.db.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateInstanceReq {
    private String name;
    private String engineId;
    private String mode;
}
