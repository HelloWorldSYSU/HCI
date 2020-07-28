package com.sysumach.voiceindentify;

import android.icu.text.SimpleDateFormat;

import java.util.Date;

public class GetAnswer {

    public static String get(String s){

        if(s.equals("小爱小爱你好")){
            return "你好";
        }else if(s.equals("今天天气怎么样")){
            return "今天天气不错";
        }else if(s.equals("现在几点了") || s.equals("现在几点啦")){
            SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String str = formatter.format(curDate);
            return str;
        }else if(s.equals("关机")){
            return "好的，五秒之后关机";
        }

        return "小爱不知道你在说什么~";

    }



}
