package lx.curriculumschedule.utils;

import java.util.Calendar;

public class DateUtils {

    public static String getTip(String name) {
       // String name = s+"同学\n";
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
       if (hour>=1&&hour<6){
           return "现在是凌晨,注意身体哦!"+name;
       }else if (hour>=6&&hour<=9){
           return "早上好!"+name;
       }else if (hour>9&&hour<=12){
           return "中午好!"+name;
       }else if (hour>12&&hour<=18){
           return "下午好!"+name;
       }
       else if (hour>=18&&hour<=24){
           return "晚上好!"+name;
       }
       return "欢迎您!";
    }
}
