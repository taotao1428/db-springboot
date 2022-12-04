package com.hewutao.db.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryInstanceListReq {
    private String mode;
    private String name;
    private String engineId;
}
