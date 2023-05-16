package com.wlp.myanjunote.kotlin.generics10;

import android.annotation.SuppressLint;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

// 泛型不消失的情况对比
// getGenericSuperclass() 方法： 返回直接继承的父类
public class NotErase {

    private static class HashMapEx<K, V> extends HashMap<K, V> {
        public HashMapEx() {
            super();
        }
    }

    @SuppressLint("NewApi")
    public static void main1(String[] args) {
        // 此处必须用匿名内部类的方式写，如果使用new HashMapEx<String,Integer> 效果同上
        Map<String, Integer> map = new HashMap<String, Integer>() { };
        System.out.println(map.getClass());//class com.wlp.myanjunote.kotlin.generics10.NotErase$1

        Type type = map.getClass().getGenericSuperclass(); // 获取HashMapEx父类范型HashMap<String, Integer>
        ParameterizedType parameterizedType = ParameterizedType.class.cast(type);

        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments(); // 两个类型  一个是K，一个是V
        for (Type typeArgument : actualTypeArguments) {
            System.out.println(typeArgument.getTypeName()); //String, Integer（使用内部类泛型不消失）
        }
    }

    @SuppressLint("NewApi")
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<String, Integer>();

        Type type = map.getClass().getGenericSuperclass(); // 获取HashMap父类AbstractMap<K,V>  请注意：此处为<K,V>
        ParameterizedType parameterizedType = ParameterizedType.class.cast(type);

        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments(); // 两个类型  一个是K，一个是V
        for (Type typeArgument : actualTypeArguments) {
            System.out.println(typeArgument.getTypeName()); //k,v（泛型被擦除消失了）
        }
    }


    /**
     * getSuperclass 返回直接继承的父类（由于编译擦除，没有显示泛型参数）
     * getGenericSuperclass：返回直接继承的父类（包含泛型参数） 1.5后提供
     */
    public static void main3(String[] args) {
        // 此处必须用匿名内部类的方式写，如果使用new HashMapEx<String,Integer> 效果同上
        Map<String, Integer> map = new HashMap<String, Integer>() {
        };
        System.out.println(map.getClass().getSuperclass()); //class java.util.HashMap
        System.out.println(map.getClass().getGenericSuperclass()); //java.util.HashMap<java.lang.String, java.lang.Integer>

        // 但是如果是不带泛型的，两者等价
        Integer i = new Integer(1);
        System.out.println(i.getClass().getSuperclass()); //class java.lang.Number
        System.out.println(i.getClass().getGenericSuperclass()); //class java.lang.Number
    }
}
