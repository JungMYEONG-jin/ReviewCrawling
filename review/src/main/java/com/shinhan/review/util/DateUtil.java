package com.shinhan.review.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public String getNow(){
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(now);

    }

}
