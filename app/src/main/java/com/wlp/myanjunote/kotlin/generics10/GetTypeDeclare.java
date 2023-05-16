package com.wlp.myanjunote.kotlin.generics10;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 原文链接：https://blog.csdn.net/JokerLJG/article/details/129041452

 ## 根据使用泛型位置的不同可以分为：声明侧泛型、使用侧泛型。

 ## 声明侧的泛型信息被记录在Class文件的Constant pool中以Signature的形式保存。而使用侧的泛型信息并没有保存。

 声明侧泛型包括：
     泛型类，或泛型接口的声明
     带有泛型参数的成员变量
     带有泛型参数的方法

 获取泛型类型相关方法
 上文有提到，声明侧的泛型被记录在Class文件的Constant pool中以Signature的形式保存。

 JDK的Class、Field、Method类提供了一系列的获取泛型类型的相关方法。

 ## 1. Class类的泛型方法
 Type getGenericSuperclass()：获取父类的Type

 若父类有泛型，返回的实际Type是ParameterizedType接口的实现类ParameterizedTypeImpl类
 若父类无泛型，返回的实际Type是Class类
 Type[] getGenericInterfaces()：获取父接口的Type集合

 若父类有泛型，返回的实际Type是ParameterizedType接口的实现类ParameterizedTypeImpl类
 若父类无泛型，返回的实际Type是Class类


 * */
public class GetTypeDeclare extends TestClass<String> implements TestInterface1<Integer>,TestInterface2<Long> {

    private List<Integer> list;

    private Map<Integer, String> map;

    public List<String> aa() {
        return null;
    }

    public void bb(List<Long> list) {

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public static void main(String[] args) throws Exception {
        System.out.println("======================================= 泛型类声明的泛型类型 =======================================");
        ParameterizedType parameterizedType = (ParameterizedType)GetTypeDeclare.class.getGenericSuperclass();
        System.out.println(parameterizedType.getTypeName() + "--------->" + parameterizedType.getActualTypeArguments()[0].getTypeName());
        //com.wlp.myanjunote.kotlin.generics10.TestClass<java.lang.String>--------->java.lang.String

        Type[] types = GetTypeDeclare.class.getGenericInterfaces();
        for (Type type : types) {
            ParameterizedType typ = (ParameterizedType)type;
            System.out.println(typ.getTypeName() + "--------->" + typ.getActualTypeArguments()[0].getTypeName());
        }
        //com.wlp.myanjunote.kotlin.generics10.TestInterface1<java.lang.Integer>--------->java.lang.Integer
        //com.wlp.myanjunote.kotlin.generics10.TestInterface2<java.lang.Long>--------->java.lang.Long

        System.out.println("======================================= 成员变量中的泛型类型 =======================================");
        ParameterizedType parameterizedType1 = (ParameterizedType)GetTypeDeclare.class.getDeclaredField("list").getGenericType();
        System.out.println(parameterizedType1.getTypeName() + "--------->" + parameterizedType1.getActualTypeArguments()[0].getTypeName());
        //java.util.List<java.lang.Integer>--------->java.lang.Integer

        ParameterizedType parameterizedType2 = (ParameterizedType)GetTypeDeclare.class.getDeclaredField("map").getGenericType();
        System.out.println(parameterizedType2.getTypeName() + "--------->" + parameterizedType2.getActualTypeArguments()[0].getTypeName()+","+parameterizedType2.getActualTypeArguments()[1].getTypeName());
        //java.util.Map<java.lang.Integer, java.lang.String>--------->java.lang.Integer,java.lang.String

        System.out.println("======================================= 方法参数中的泛型类型 =======================================");
        ParameterizedType parameterizedType3 = (ParameterizedType)GetTypeDeclare.class.getMethod("aa").getGenericReturnType();
        System.out.println(parameterizedType3.getTypeName() + "--------->" + parameterizedType3.getActualTypeArguments()[0].getTypeName());
        //java.util.List<java.lang.String>--------->java.lang.String

        System.out.println("======================================= 方法返回值中的泛型类型 =======================================");
        Type[] types1 = GetTypeDeclare.class.getMethod("bb", List.class).getGenericParameterTypes();
        for (Type type : types1) {
            ParameterizedType typ = (ParameterizedType)type;
            System.out.println(typ.getTypeName() + "--------->" + typ.getActualTypeArguments()[0].getTypeName());
        }
        //java.util.List<java.lang.Long>--------->java.lang.Long

    }
}

class TestClass<T> {

}

interface TestInterface1<T> {

}

interface TestInterface2<T> {

}
