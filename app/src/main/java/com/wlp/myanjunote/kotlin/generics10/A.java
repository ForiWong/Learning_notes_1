package com.wlp.myanjunote.kotlin.generics10;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class A<T, E> {
    public A() {
        //通过反射获取当前类表示的实体（类，接口，基本类型或void）的直接父类的Type
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        //ParameterizedType 强转为泛型类型
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        //如果支持泛型，返回表示此类型实际类型参数的Type对象的数组,数组里放的都是对应类型的Class，因为可能有多个，所以是数组。
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Class tClass = (Class) actualTypeArguments[0];

        System.out.println(tClass.getName());

        // 用一句话获取就是下面一行代码，只是去除了中间变量，可读性差了些
        // Class tCls = (Class) (((ParameterizedType) (this.getClass().getGenericSuperclass())).getActualTypeArguments()[0]);
    }
}