package com.jingtum.chainApp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期工具类,封装了日期中常用的一些方法
 * @author 
 *
 */
public class DateUtils implements DateFormator{
	//定义默认的日期格式，以供下面正则表达式使用
	private static Map<String, String> defaultDateFormatMap = new HashMap<String, String>();
	static {
		defaultDateFormatMap.put("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}",YEAR_MONTH_DAY);
		defaultDateFormatMap
		.put("[0-9]{4}/[0-9]{1,2}/[0-9]{1,2}", "yyyy/MM/dd");
		defaultDateFormatMap
		.put(
				"[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}",
				YEAR_MONTH_DAY_HH_MM_SS);
		defaultDateFormatMap
		.put(
				"[0-9]{4}/[0-9]{1,2}/[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}",
		"yyyy/MM/dd HH:mm:ss");
		defaultDateFormatMap.put("[0-9]{4}-[0-9]{1,2}", YEAR_MONTH);
		defaultDateFormatMap.put("[0-9]{4}/[0-9]{1,2}", "yyyy/MM");
	}

	
	public static void main(String[] args) {
		Date da=DateUtils.toDate("2015-05-26");
		
		System.out.println(DateUtils.toString(da, "yyyyMMdd"));
	}
	
	/**
	 * 将日期按照格式转换成String
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String toString(Date date, String pattern) {
		Assert.notNull(date);
		Assert.notNull(pattern);
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * 将字符串按照格式转化成日期
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static Date toDate(String time, String pattern) {
		Assert.notNull(time);
		Assert.notNull(pattern);
		if(time.length() <11 && pattern.length() >11){
			pattern="yyyy-MM-dd";
		}
		return CalendarUtils.toCalendar(time, pattern).getTime();
	}

	/**
	 * 提供从String类型到Date类型的类型转化，目前自动支持 "yyyy-MM-dd"、"yyyy-MM"、 "yyyy-MM-dd
	 * HH:mm:ss"、"MM-dd"等4种日期格式的自动转化
	 * 
	 * @param time
	 * @return
	 */
	public static Date toDate(String time) {
		Assert.notNull(time);
		if(defaultDateFormatMap.keySet().size()==0){
			defaultDateFormatMap.put("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}",YEAR_MONTH_DAY);
			defaultDateFormatMap
			.put("[0-9]{4}/[0-9]{1,2}/[0-9]{1,2}", "yyyy/MM/dd");
			defaultDateFormatMap
			.put(
					"[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}",
					YEAR_MONTH_DAY_HH_MM_SS);
			defaultDateFormatMap
			.put(
					"[0-9]{4}/[0-9]{1,2}/[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}",
			"yyyy/MM/dd HH:mm:ss");
			defaultDateFormatMap.put("[0-9]{4}-[0-9]{1,2}", YEAR_MONTH);
			defaultDateFormatMap.put("[0-9]{4}/[0-9]{1,2}", "yyyy/MM");
		}
		for (String key : defaultDateFormatMap.keySet()) {
			if (isDateFormat(time, key)) {
				return DateUtils.toDate(time, defaultDateFormatMap.get(key));
			}
		}
		return null;
	}

	//是否是日期格式化类型
	public static boolean isDateFormat(String date) {
		Assert.notNull(date);
		for (String key : defaultDateFormatMap.keySet()) {
			if (isDateFormat(date, key)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isDateFormat(String date, String format) {
		Assert.notNull(date);
		return isDefinedPattern(date, format);
	}

	/**
	 * 判断str字符串是否符合我们定义的标准格式，pattern表示格式(正则表达式)。
	 * 
	 * @param str
	 * @param pattern
	 */
	private static boolean isDefinedPattern(String str, String pattern) {
		Assert.notNull(str);
		Assert.notNull(pattern);

		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 根据年月获取一个月的开始时间（第一天的凌晨）
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date beginTimeOfMonth(int year, int month) {
		Calendar first = new GregorianCalendar(year, month - 1, 1, 0, 0, 0);
		return first.getTime();
	}

	/**
	 * 根据年月获取一个月的结束时间（最后一天的最后一毫秒）
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date endTimeOfMonth(int year, int month) {
		Calendar first = new GregorianCalendar(year, month, 1, 0, 0, 0);
		first.add(Calendar.MILLISECOND, -1);
		return first.getTime();
	}

	/**
	 * 获取后nextDays天的Date对象
	 * 
	 * @param date
	 * @param nextDays
	 * @return
	 */
	public static Date nextDays(Date date, int nextDays) {
		GregorianCalendar c1 = new GregorianCalendar();
		c1.setTime(date);
		GregorianCalendar cloneCalendar = (GregorianCalendar) c1.clone();
		cloneCalendar.add(Calendar.DATE, nextDays);
		return cloneCalendar.getTime();
	}

	/**
	 * 间隔天数
	 * 
	 * @param d1
	 * @param d2
	 * @return d1 - d2 实际天数,如果 d1 表示的时间等于 d2 表示的时间，则返回 0 值；如果此 d1
	 *         的时间在d2表示的时间之前，则返回小于 0 的值；如果 d1 的时间在 d2 表示的时间之后，则返回大于 0 的值。
	 */
	public static long dayDiff(Date d1, Date d2) {
		Assert.notNull(d1);
		Assert.notNull(d2);

		Calendar c1 = new GregorianCalendar();
		Calendar c2 = new GregorianCalendar();

		c1.setTime(d1);
		c2.setTime(d2);

		long diffDays = CalendarUtils.getDiffDays(c1, c2);

		return diffDays;
	}
	/**
	 * 获取一周的开始时间
	 * @return
	 */
	public static Date beginTimeOfWeek(){
		Calendar calendar = Calendar.getInstance();
		int min = calendar.getActualMinimum(Calendar.DAY_OF_WEEK); //获取周开始基准
		int current = calendar.get(Calendar.DAY_OF_WEEK); //获取当天周内天数
		calendar.add(Calendar.DAY_OF_WEEK, min-current); //当天-基准，获取周开始日期
		Date start = calendar.getTime();
		return start;
	}

	/**
	 * 获取一周的结束时间
	 * @return
	 */
	public static Date endTimeOfWeek(){
		return nextDays(beginTimeOfWeek(),6);
	}

	/**
	 * 判断两个日期是否在同一周
	 */
	public static int sameWeek(String date2){
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = format.parse(date2);
			Calendar calendar = Calendar.getInstance();
			calendar.setFirstDayOfWeek(Calendar.MONDAY);
			calendar.setTime(date);
			calendar.setFirstDayOfWeek(1);
			return calendar.get(Calendar.WEEK_OF_YEAR)-1;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static Date nextMonth(Date d){
		Calendar c=Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.MONTH,1); //将当前日期加一个月
		return c.getTime();
	}
    /**
	 * 格式化日期字符串
	 */	
	public static void dateFormat(List list,String formatString){
		for(int i=0;i<list.size();i++){
			Map map =(Map)list.get(i);
			Set<String> keys = map.keySet();
			for(String key:keys){
				Object obj = map.get(key);
				if(obj instanceof java.util.Date){
					SimpleDateFormat sdf = new SimpleDateFormat(formatString);
					String str = sdf.format(obj);
					map.put(key, str);
				}
			}
		}
	}
	
	public static long getCurDayLeftSeconds(){
		long leftSeconds=0;
		Date nextDateTime=nextDays(new Date(),1);
		Date nextDate=toDate(toString(nextDateTime,"yyyy/MM/dd"),"yyyy/MM/dd");		
		leftSeconds=(nextDate.getTime()-System.currentTimeMillis())/1000;		
		return leftSeconds;
	}
	
	public static String toChineseTime(String strTime){
		String chineseTime=strTime;
		if (strTime.equalsIgnoreCase("08:00:00")){
			chineseTime="8点";
		}else if (strTime.equalsIgnoreCase("09:00:00")){
			chineseTime="9点";
		}else if (strTime.equalsIgnoreCase("10:00:00")){
			chineseTime="10点";
		}else if (strTime.equalsIgnoreCase("11:00:00")){
			chineseTime="11点";
		}else if (strTime.equalsIgnoreCase("12:00:00")){
			chineseTime="12点";
		}else if (strTime.equalsIgnoreCase("13:00:00")){
			chineseTime="13点";
		}else if (strTime.equalsIgnoreCase("14:00:00")){
			chineseTime="14点";
		}else if (strTime.equalsIgnoreCase("15:00:00")){
			chineseTime="15点";
		}else if (strTime.equalsIgnoreCase("16:00:00")){
			chineseTime="16点";
		}else if (strTime.equalsIgnoreCase("17:00:00")){
			chineseTime="17点";
		}else if (strTime.equalsIgnoreCase("18:00:00")){
			chineseTime="18点";
		}else if (strTime.equalsIgnoreCase("19:00:00")){
			chineseTime="19点";
		}else if (strTime.equalsIgnoreCase("20:00:00")){
			chineseTime="20点";
		}else if (strTime.equalsIgnoreCase("21:00:00")){
			chineseTime="21点";
		}else if (strTime.equalsIgnoreCase("22:00:00")){
			chineseTime="22点";
		}else if (strTime.equalsIgnoreCase("23:00:00")){
			chineseTime="23点";
		}
		return chineseTime;
	}
}
