package com.example.myapplication.utils;

import java.text.DecimalFormat;

public class StringUtil {

    public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.isEmpty())
            return false;
        return true;
    }
}
