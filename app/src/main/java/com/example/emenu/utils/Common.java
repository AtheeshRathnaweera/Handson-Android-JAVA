package com.example.emenu.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Common {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public static Date convertStringToDate(String recDate) {

        try {
            return simpleDateFormat.parse(recDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertDateToString(Date date) {
        return simpleDateFormat.format(date);
    }
}
