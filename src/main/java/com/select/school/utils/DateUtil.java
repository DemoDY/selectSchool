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

    /**
     * 根据格式 把Date类型转为String类型
     * @param date
     * @param format
     * @return String
     */
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
     * 获取当前日期的前一天（昨天）
     *
     * @return Date
     *
     */
    public static Date getYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 获取当前日期的前一天（昨天）的指定格式字符串
     *
     */
    public static String getYesterday(String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(getYesterday());
    }

    /**
     * 获取当前日期的前三个月
     */
    public static String getThreeManthDay(Date pattern){
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(pattern);//把当前时间赋给日历
        calendar.add(Calendar.MONTH, -3);  //设置为前3月
        dBefore = calendar.getTime();   //得到前3月的时间
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式
        String defaultStartDate = sdf.format(dBefore);    //格式化前3月的时间
//        String defaultEndDate = sdf.format(pattern); //格式化当前时间
//        System.out.println("三个月之前时间======="+defaultStartDate);
//        System.out.println("当前时间==========="+defaultEndDate);
        return defaultStartDate;
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
     * Double 转化为百分数
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
        String newStr = str.replace("%","");//去掉百分号
        return newStr;
    }

    public static String getPercentInter(double number,double maxNumber) {
        double i = number/(maxNumber/100);//录取率/(最高成绩/100)
        Double d = new Double(i);
        int doubleToInt = d.intValue();
        String num = String.valueOf(doubleToInt);
        return num;
    }


    public static String getInter(double number,double maxNumber) {
        double i = number / (maxNumber / 100);//录取率/(最高成绩/100)
        NumberFormat nf = java.text.NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(0);// 小数点后保留几位
        String str = nf.format(i);
        return str;
    }
}
