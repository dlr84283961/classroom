package cn.ninghan.common.utils;

import org.apache.commons.lang3.StringUtils;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeUtil {
    private static ZoneId zoneId = ZoneId.systemDefault();
    public static DateTimeFormatter yyyy_MM_dd_hh_mm = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static DateTimeFormatter yyyy_MM_dd_hh_mm_ss = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static DateTimeFormatter HH_mm = DateTimeFormatter.ofPattern("HH:mm");
    public static String dataToString(Date date,DateTimeFormatter dateTimeFormatter){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(),zoneId);
        String timeStr = localDateTime.format(dateTimeFormatter);
        return timeStr;
    }
    public static Date stringToDate(String timeStr,DateTimeFormatter dateTimeFormatter){
        LocalDateTime localDateTime = LocalDateTime.parse(timeStr,dateTimeFormatter);
        Date date = Date.from(localDateTime.atZone(zoneId).toInstant());
        return date;
    }

    public static LocalDateTime stringToLocalDate(String timeStr,DateTimeFormatter dateTimeFormatter){
        LocalDateTime localDateTime = LocalDateTime.parse(timeStr,dateTimeFormatter);
        return localDateTime;
    }

    public static String fillTimeStrWith0(String hour,String minute){
        if(StringUtils.isBlank(hour)||StringUtils.isBlank(minute)){
            return "";
        }
        String tmpHour = hour;
        String tmpMinute = minute;
        if(tmpHour.length()<2){
            tmpHour = "0"+tmpHour;
        }
        if(tmpMinute.length()<2){
            tmpMinute = "0"+tmpMinute;
        }

        return tmpHour+":"+tmpMinute;
    }

    public static String fillTimeStrWith0(int hour,int minute){
        if(hour<0||minute<0){
            return "";
        }
        String tmpHour = String.valueOf(hour);
        String tmpMinute = String.valueOf(minute);
        if(tmpHour.length()<2){
            tmpHour = "0"+tmpHour;
        }
        if(tmpMinute.length()<2){
            tmpMinute = "0"+tmpMinute;
        }

        return tmpHour+":"+tmpMinute;
    }

}
