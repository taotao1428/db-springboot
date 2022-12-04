package com.hewutao.db.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryInstanceCondition {
    private String name;
    private String mode;
    private String engineId;
}
