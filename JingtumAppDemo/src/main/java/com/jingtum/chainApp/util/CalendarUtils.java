package com.jingtum.chainApp.util;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.ParseException;
import org.springframework.util.Assert;

public class CalendarUtils implements DateFormator {
	private CalendarUtils() {

	}

	/**
	 * get the time string of current time
	 * 
	 * @param pattern
	 * @return
	 */
	public static String now(String pattern) {

		Assert.notNull(pattern);

		GregorianCalendar gerCalendar = new GregorianCalendar();
		Date date = gerCalendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * get default time String of current time,"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @return
	 */
	public static String now() {
		return CalendarUtils.now(YEAR_MONTH_DAY_HH_MM_SS);
	}

	/**
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T now(Class<T> clazz) {

		Assert.notNull(clazz);

		if (GregorianCalendar.class.equals(clazz)) {
			return (T) new GregorianCalendar();
		} else if (Calendar.class.equals(clazz)) {
			return (T) new GregorianCalendar();
		} else if (Date.class.equals(clazz)) {
			return (T) new GregorianCalendar().getTime();
		} else if (String.class.equals(clazz)) {
			return (T) CalendarUtils.now(YEAR_MONTH_DAY_HH_MM_SS);
		} else {
			throw new IllegalArgumentException(
					"argument must be in [java.util.Calendar,java.util.GregorianCalendar,java.util.Date,String]");
		}
	}

	public static GregorianCalendar toCalendar(String time, String pattern) {

		Assert.notNull(time);
		Assert.notNull(pattern);

		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = format.parse(time);
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			return calendar;
		} catch (ParseException e) {
			throw new RuntimeException(
					"[Pase Exception]: the time string doesn't match for pattern.");
		}
	}

	public static GregorianCalendar toCalendar(String time) {
		Assert.notNull(time);
		return toCalendar(time, YEAR_MONTH_DAY_HH_MM_SS);
	}

	public static long getDiffMillis(Calendar c1, Calendar c2) {
		Assert.notNull(c1);
		Assert.notNull(c2);

		long diff = c1.getTimeInMillis() - c2.getTimeInMillis();

		return diff;
	}

	public static int compare(Calendar c1, Calendar c2) {
		Assert.notNull(c1);
		Assert.notNull(c2);

		return c1.compareTo(c2);
	}

	public static String toString(Calendar c, String pattern) {
		Assert.notNull(c);
		Assert.notNull(pattern);

		Date date = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	public static String toString(Calendar c) {
		Assert.notNull(c);
		return toString(c, YEAR_MONTH_DAY_HH_MM_SS);
	}

	public static GregorianCalendar calculator(Calendar c, int diffDays) {
		GregorianCalendar cloneCalendar = (GregorianCalendar) c.clone();
		cloneCalendar.add(Calendar.DATE, diffDays);
		return cloneCalendar;
	}

	public static GregorianCalendar calculator(int diffDays) {
		return calculator(new GregorianCalendar(), diffDays);
	}

	public static Calendar beginTimeOfMonth(int year, int month) {
		Calendar first = new GregorianCalendar(year, month - 1, 1, 0, 0, 0);
		return first;
	}

	public static Calendar endTimeOfMonth(int year, int month) {
		Calendar first = new GregorianCalendar(year, month, 1, 0, 0, 0);
		first.add(Calendar.MILLISECOND, -1);
		return first;
	}

	/**
	 * 返回系统当前日期的前N天的日期（GregorianCalendar）。
	 * 
	 * @param preDays
	 * @return
	 */

	public static GregorianCalendar preDaysCalendar(int preDays) {
		return CalendarUtils.preDaysCalendar(new GregorianCalendar(), preDays);
	}

	/**
	 * 计算输入日期(c1)前N天（preDays）的日期，并返回那天日期的GregorianCalendar类型。
	 * 
	 * @param c1
	 * @param preDays
	 * @return
	 */

	public static GregorianCalendar preDaysCalendar(Calendar c1, int preDays) {
		GregorianCalendar cloneCalendar = (GregorianCalendar) c1.clone();
		cloneCalendar.add(Calendar.DATE, -preDays);
		return cloneCalendar;
	}

	/**
	 * 把表示日期的字符串解析为GregorianCalendar对象，日期字符串输入的默认格式为：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dateStringToParse
	 * @return
	 */

	public static GregorianCalendar parseToCalendar(String dateStringToParse) {
		return CalendarUtils.parseToCalendar(dateStringToParse,
				YEAR_MONTH_DAY_HH_MM_SS);
	}

	/**
	 * 把表示日期的字符串解析为GregorianCalendar对象，可以自定义解析日期字符串的表现形式（pattern），
	 * 表现形式规则同DateFormat
	 * 
	 * @param dateStringToParse
	 * @param pattern
	 * @return
	 */

	public static GregorianCalendar parseToCalendar(String dateStringToParse,
			String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = format.parse(dateStringToParse);
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			return calendar;
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException(
					"[Pase Exception]: please check the input date string format.");
		}
	}

	/**
	 * 根据日期得到该日期上一周的星期一的开始时间 2009年9月30日17:37:11
	 * 
	 * @param date
	 *            格式YYYY-MM-DD
	 * @return
	 */
	public static GregorianCalendar getStartDate(String date) {
		int week = getWeekByDate(date);
		GregorianCalendar beginGC = CalendarUtils.parseToCalendar(date
				+ " 00:00:00");
		GregorianCalendar beginDate = CalendarUtils.preDaysCalendar(beginGC,
				week + 7 - 0);
		return beginDate;
	}

	/**
	 * 根据日期得到该日期上一周的星期天的结束时间 2009年9月30日17:39:12
	 * 
	 * @param date
	 *            格式YYYY-MM-DD
	 * @return
	 */
	public static GregorianCalendar getEndDate(String date) {
		int week = getWeekByDate(date);
		GregorianCalendar endGC = CalendarUtils.parseToCalendar(date
				+ " 23:59:59");
		GregorianCalendar endDate = CalendarUtils.preDaysCalendar(endGC,
				week + 6 - 6);
		return endDate;
	}

	/**
	 * 得到这个日期是星期几
	 * 
	 * @param date
	 *            格式为:YYYY-MM-DD
	 */
	public static int getWeekByDate(String date) {
		GregorianCalendar c = CalendarUtils.toCalendar(date,
				CalendarUtils.YEAR_MONTH_DAY);
		int weekDay = c.get(Calendar.DAY_OF_WEEK);
		// 因为通过c.get(Calendar.DAY_OF_WEEK)等到的周日为1，周六为6，所以需要转换一下
		if (weekDay == 1) {
			weekDay = 7;
		} else {
			weekDay--;
		}
		return weekDay;
	}

	/**
	 * 获取当前日期是星期几，周一为1，一直到周日，周日为7
	 * 
	 * @return
	 */
	public static int getNowWeekDay() {
		GregorianCalendar c = new GregorianCalendar();
		int weekDay = c.get(Calendar.DAY_OF_WEEK);
		// 因为通过c.get(Calendar.DAY_OF_WEEK)等到的周日为1，周六为6，所以需要转换一下
		if (weekDay == 1) {
			weekDay = 7;
		} else {
			weekDay--;
		}
		return weekDay;
	}
	
	/**
	 * 获取当前日期是几号
	 * 
	 * @return
	 */
	public static int getNowMonthDay() {
		GregorianCalendar c = new GregorianCalendar();
		int monthDay = c.get(Calendar.DAY_OF_MONTH);
		return monthDay;
	}
	
	/**
	 * 获取当前月最大是几号
	 * 
	 * @return
	 */
	public static int getNowMaxMonthDay() {
		GregorianCalendar c = new GregorianCalendar();
		int monthDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		return monthDay;
	}

	/**
	 * 判断是否是润年 参数： 年份: i
	 */
	public static boolean juageDate(int i) {
		if (((i % 4 == 0) && (i % 100 != 0)) || (i % 4 == 0 && i % 400 == 0)) {
			return true;
		}
		return false;
	}

	public static long getDiffDays(Calendar c1, Calendar c2) {
		Assert.notNull(c1);
		Assert.notNull(c2);

		Calendar c1Copy = new GregorianCalendar(c1.get(YEAR), c1.get(MONTH), c1
				.get(DATE));
		Calendar c2Copy = new GregorianCalendar(c2.get(YEAR), c2.get(MONTH), c2
				.get(DATE));
		
		long diffMillis = getDiffMillis(c1Copy, c2Copy);

		long dayMills = 24L * 60L * 60L * 1000L;
		long diffDays = diffMillis / dayMills;

		return diffDays;
	}
}
