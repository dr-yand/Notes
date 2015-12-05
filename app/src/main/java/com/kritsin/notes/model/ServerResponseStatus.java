package com.kritsin.notes.model;

import java.util.HashMap;
import java.util.Map;

public enum ServerResponseStatus {
    OK(0), ERROR(-100),  NO_DATA(-1), NO_USER(-2);

    public final int value;

    private static Map<Integer, ServerResponseStatus> map = new HashMap<Integer, ServerResponseStatus>();

    static {
        for (ServerResponseStatus srs : ServerResponseStatus.values()) {
            map.put(srs.value, srs);
        }
    }

    private ServerResponseStatus(int i) {
        value = i;
    }

    public static ServerResponseStatus valueOf(int i){
        return map.get(i);
    }

    @Override
    public String toString(){
        if(value==OK.value){
            return "Успешно";
        }
        if(value==NO_DATA.value){
            return "Введите данные";
        }
        if(value==NO_USER.value){
            return "Такого пользователя не существует";
        }
        return "Произошла ошибка (код "+value+")";
    }
}
