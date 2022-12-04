package com.hewutao.db.model.support;

import com.hewutao.db.model.Delay;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityUtils {
    public static  <T>  List<T> getData(List<T> entityList) {
        if (entityList instanceof Delay) {
            if (!((Delay) entityList).loaded()) {
                return entityList;
            }
        }
        return new ArrayList<>(entityList);
    }


}
