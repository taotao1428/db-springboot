package com.hewutao.db.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InstanceDTO {
    private String id;
    private String name;
    private String mode;
    private String enginId;
}
