package com.hewutao.db.model2.support;

import com.hewutao.db.model2.Delay;

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
