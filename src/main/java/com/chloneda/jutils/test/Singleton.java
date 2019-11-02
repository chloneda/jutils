package com.chloneda.jutils.test;

/**
 * Created by chloneda
 * Description:
 */
public enum Singleton {
    FISH,MEAT,RICE;

    Singleton getInstance() {
        return Singleton.FISH;
    }
}
