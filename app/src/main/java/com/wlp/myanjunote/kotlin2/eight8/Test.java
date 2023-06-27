package com.wlp.myanjunote.kotlin2.eight8;

import kotlin.jvm.functions.Function1;

public class Test {

    public static void main(String[] args){
        UtilsKt.filter("Love", new Function1<Character, Boolean>() {
            @Override
            public Boolean invoke(Character character) {
                return true;
            }
        });
    }

}
