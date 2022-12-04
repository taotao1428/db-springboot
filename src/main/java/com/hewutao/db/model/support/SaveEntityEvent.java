package com.hewutao.db.model.support;

import com.hewutao.db.model.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveEntityEvent {
    private Entity entity;
}
