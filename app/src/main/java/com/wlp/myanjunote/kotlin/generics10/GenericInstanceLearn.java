package com.wlp.myanjunote.kotlin.generics10;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 使用反射对Java泛型进行实例化
 * 在带泛型的基类，通过反射，完成了对泛型的实例化，统一操作
 * 从而，在子类就可以直接使用，不用管每次都实例化一次了
 */
public class GenericInstanceLearn {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        UserDao userDao = new UserDao();
        System.out.println(userDao.toString());
    }
}

class Dao<T> {
    public Class<T> clazz;
    public T user;

    public Dao() throws IllegalAccessException, InstantiationException {
        Type superclass = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = null;
        if (superclass instanceof ParameterizedType) {
            parameterizedType = (ParameterizedType) superclass;
            Type[] typeArray = parameterizedType.getActualTypeArguments();
            if (typeArray != null && typeArray.length > 0) {
                clazz = (Class<T>) typeArray[0];
                user= clazz.newInstance(); //反射创建T对象
            }
        }
    }

    @Override
    public String toString() {
        return "Dao{" +
                "user=" + user.toString() +
                '}';
    }
}

class UserDao extends Dao<User> {

    public UserDao() throws IllegalAccessException, InstantiationException {
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

class User {
    String name;

    public User() {
        this.name = "default name";
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }
}
