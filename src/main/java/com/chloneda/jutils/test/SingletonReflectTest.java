package com.chloneda.jutils.test;

import java.lang.reflect.Constructor;

/**
 * Created by chloneda on 2019-10-14
 * Description:
 */
public class SingletonReflectTest {
    public static void main(String[] args) {
        //创建第一个实例
        Singleton instance1 = Singleton.INSTANCE;

        //通过反射创建第二个实例
        Singleton instance2 = null;
        try {
            Class<Singleton> clazz = Singleton.class;
            Constructor<Singleton> cons = clazz.getDeclaredConstructor();
            cons.setAccessible(true);
            instance2 = cons.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //检查两个实例的hash值
        System.out.println("Instance1 hashCode: " + instance1.hashCode());
        System.out.println("Instance2 hashCode: " + instance2.hashCode());
    }
}

