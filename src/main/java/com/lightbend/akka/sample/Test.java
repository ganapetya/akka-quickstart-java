package com.lightbend.akka.sample;

import java.util.Calendar;
import java.util.Date;

public class Test {


    public static void main(String[] args) {

       Date localDate =  toGMTDate(new Date());


    }

    public static  Date toGMTDate(Date date) {
        if (date == null) {
            return null;
        }

        int localDelta = (Calendar.getInstance().get(Calendar.ZONE_OFFSET) + Calendar
                .getInstance().get(Calendar.DST_OFFSET));
        Date localDate = new Date(date.getTime() - localDelta);
        return localDate;
    }


}
