package cc.htdf.msgcloud.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by JT on 2017/10/10.
 */
public class DateUtils  {

    public static final String	FORMAT_yyyy_MM_dd_hh_mm_ss	= "yyyy-MM-dd HH:mm:ss";
    private static final String	DEFAULT_DATE_FORMATE		= "yyyy-MM-dd";
    public static final String	FORMATyyyyMMddhhmmss		= "yyyyMMddHHmmss";

    /**
     * 字符串时间格式转换
     * @param str：字符串类型时间
     * @param srcPattern：源格式
     * @param targetPattern：目标格式
     * @return
     * @throws ParseException
     */
    public static String format(String str, String srcPattern, String targetPattern) throws ParseException {
        return formatDateToStr(formatStrToDate(str, srcPattern), targetPattern);
    }


    /**
     * 增加天
     * @param str
     * @param day
     * @param srcPattern
     * @param targetPattern
     * @return
     * @throws ParseException
     */
    public static String addDays(String str, Integer day, String srcPattern, String targetPattern) throws ParseException {
        return  formatDateToStr(addDays(formatStrToDate(str, srcPattern), day), targetPattern);
    }

    /**
     * 增加天
     * @param date
     * @param count
     * @return
     */
    public static Date addDays(Date date, int count) {
        return org.apache.commons.lang3.time.DateUtils.addDays(date, count);
    }

    /**
     * 增加小时
     * @param str：字符串时间
     * @param hour：小时
     * @param srcPattern：源格式
     * @param targetPattern：目标格式
     * @return
     * @throws ParseException
     */
    public static String addHour(String str, Integer hour, String srcPattern, String targetPattern) throws ParseException {
        return formatDateToStr(addHour(formatStrToDate(str, srcPattern), hour), targetPattern);
    }

    public static String addHAndM(String str, int hour, int minutes,String srcPattern, String targetPattern) throws ParseException {
        Date date = formatStrToDate(str, srcPattern);
        return formatDateToStr(addMinutes(addHour(date,hour),minutes),targetPattern);
    }

    /**
     * 增加小时
     * @param date
     * @param count
     * @return
     */
    public static Date addHour(Date date, int count) {
        return org.apache.commons.lang3.time.DateUtils.addHours(date, count);
    }

    /**
     * 增加分钟
     * @param date
     * @param count
     * @return
     */
    public static Date addMinutes(Date date, int count) {
        return org.apache.commons.lang3.time.DateUtils.addMinutes(date, count);
    }
    /**
     * 字符串时间 转为 时间格式
     * @param str：字符串时间
     * @param pattern：时间格式
     * @return
     * @throws ParseException
     */
    public static Date formatStrToDate(String str, String pattern) throws ParseException {
        return org.apache.commons.lang3.time.DateUtils.parseDate(str, pattern);
    }


    /**
     * 时间转为字符串
     * @param date：时间
     * @param pattern：时间格式
     * @return
     */
    public static String formatDateToStr(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * 获得当前时间戳
     * @return
     */
    public static long getTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间
     * @param pattern：时间格式
     * @return
     */
    public static String getNowFormat(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 当前日期时间向前增加hour小时
     *
     * @param date
     * @return FORMAT_yyyy_MM_dd_hh_mm_ss
     */
    public static String getDateHourAdd(String date, int hours)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.FORMAT_yyyy_MM_dd_hh_mm_ss);
        Calendar c = Calendar.getInstance();
        try
        {
            c.setTime(sdf.parse(date));
        }
        catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int hour = c.get(Calendar.HOUR);
        c.set(Calendar.HOUR, hour + hours);
        Date d = c.getTime();
        return DateUtils.toString(d, DateUtils.FORMAT_yyyy_MM_dd_hh_mm_ss);
    }

    /*
     * 将日期格式转换成字符格式
     *
     * @parm date 日期类型 fromatString 日期格式
     *
     * @author yanjie
     *
     * @return String
     */
    public static String toString(Date date, String formatString)
    {
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        return format.format(date);
    }

    /**
     * 获取系统当前日期,日期格式为yyyy-MM-dd。
     *
     * @return 返回字符串型的日期 String
     * @author zhuangruhai
     * @since 2007-9-27
     */
    public static String getCurrentDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMATE);
        return sdf.format(getDate());
    }

    /**
     * 获取系统指定格式当前日期
     *
     * @param formate
     *            日期格式(比如：yyyy-MM-dd)
     * @return 返回字符串型的日期 String
     * @author zhuangruhai
     * @since 2007-9-27
     */
    public static String getCurrentDate(String formate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(formate);
        return sdf.format(getDate());
    }

    /**
     * 获取系统当前默认日期
     *
     * @return 返回日期 Date
     * @author zhuangruhai
     * @since 2007-9-27
     */
    public static Date getDate()
    {
        return new Date();
    }

    /**
     * 字符串转化为时间
     * @param str：字符串
     * @param pattern：格式
     * @return
     */
    public static Date parseDate(String str, String pattern) throws ParseException {
        return org.apache.commons.lang3.time.DateUtils.parseDate(str, pattern);
    }

    public static String format(Date date, String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date setYear(Date date, Integer year) {
        return org.apache.commons.lang3.time.DateUtils.setYears(date, year);
    }

    public static Date setMonth(Date date, Integer month) {
        return org.apache.commons.lang3.time.DateUtils.setMonths(date, month);
    }

    public static Date setDays(Date date, Integer days) {
        return org.apache.commons.lang3.time.DateUtils.setDays(date, days);
    }

    public static Date setHour(Date date, Integer hour) {
        return org.apache.commons.lang3.time.DateUtils.setHours(date, hour);
    }

    public static Date setMinute(Date date, Integer minutes) {
        return org.apache.commons.lang3.time.DateUtils.setMinutes(date, minutes);
    }

    public static Date setSeconds(Date date, Integer seconds) {
        return org.apache.commons.lang3.time.DateUtils.setSeconds(date, seconds);
    }

    /**
     * 日期转星期
     *
     * @param datetime
     * @return
     */
    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static String getUniversalTime(){
        long lm =  System.currentTimeMillis();
        Calendar cd = Calendar.getInstance();
        cd.setTimeInMillis(lm);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
//        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String timeStr = sdf.format(cd.getTime());
        return timeStr;
    }
}
