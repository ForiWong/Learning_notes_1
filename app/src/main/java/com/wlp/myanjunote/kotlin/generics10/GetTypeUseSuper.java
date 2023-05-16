package com.wlp.myanjunote.kotlin.generics10;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


public abstract class GetTypeUseSuper<T> {
    private final Type type;

    public GetTypeUseSuper() {
        Type superClass = getClass().getGenericSuperclass();
        if (!(superClass instanceof ParameterizedType)) {
            throw new IllegalArgumentException("无泛型类型信息");
        }
        type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    public Type getType() {
        return type;
    }
}

