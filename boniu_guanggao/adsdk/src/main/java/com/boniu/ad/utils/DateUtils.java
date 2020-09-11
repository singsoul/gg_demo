package com.boniu.ad.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    //时间戳转字符串  固定格式MM月dd日 HH:mm:ss
    public static String getTypeTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(new Date());
        return format+"";
    }
}
