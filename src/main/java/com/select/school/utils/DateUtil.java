package com.select.school.utils;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class DateUtil {
    
    private static Logger log = LoggerFactory.getLogger(DateUtil.class);
    
    private static String DATE_PATTERN_YYYYMMDD = "yyyy-MM-dd";
    private static String DATE_PATTERN_YYYYMMDDHHmmSS = "yyyy-MM-dd HH:mm:ss";

    public static Date getCurDate() {
        Calendar cd = Calendar.getInstance();
        cd.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return cd.getTime();
    }

    public static String toString(Date date, String format) {
        if (StringUtils.hasText(format) && date != null) {
            SimpleDateFormat simpleDateFormat = null;

            try {
                simpleDateFormat = new SimpleDateFormat(format);
                return simpleDateFormat.format(date);
            } catch (IllegalArgumentException arg3) {
                log.error(MessageFormat.format("日期转换时格式不符合要求：[format={0}]", new Object[]{format}));
                return null;
            }
        } else {
            log.error("日期转换时参数不能为空！");
            return null;
        }
    }

    public static String[] getDatePattern() {
        ArrayList<String> patternStrList = new ArrayList<String>();
        patternStrList.add(DATE_PATTERN_YYYYMMDD);
        patternStrList.add(DATE_PATTERN_YYYYMMDDHHmmSS);
        String[] dateFormatList = new String[0];
        return (String[]) patternStrList.toArray(dateFormatList);
    }

    /**
     * 带百分号的String类型 转成double类型
     * @param print
     * @return
     */
    public static double stringToDouble(String print){
        double num = 0;
        if (print!=null&&!"".equals(print)){
            //去掉%号 将含有%的字符串转换成浮点型数据类型。
             num = Double.parseDouble(print.replace("%",""))*0.01;
        }
        return num;
    }

    /**
     * Double转化为百分数
     * @param d
     * @param IntegerDigits
     * @param FractionDigits
     * @return
     */
    public static String getPercentFormat(double d,int IntegerDigits,int FractionDigits) {
        NumberFormat nf = java.text.NumberFormat.getPercentInstance();
        nf.setMaximumIntegerDigits(IntegerDigits);//小数点前保留几位
        nf.setMinimumFractionDigits(FractionDigits);// 小数点后保留几位
        String str = nf.format(d);
        return str;
    }
}
