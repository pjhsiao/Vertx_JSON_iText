package com.vertx.bankpayform.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class ValueConverter {
	/**
	 * 將西元年轉換為民國年
	 * @param date
	 * @return
	 */
	public static String toChineseDate(Date date){
		return toChineseDate(date,"Y年M月D日 (星期W)H:m:s");
	}
	/**
	 * 將西元年轉換為民國年無時間
	 * @param date
	 * @return
	 */
	public static String toChineseDateYMD(Date date){
		return toChineseDate(date,"民國Y年M月D日");
	}
	/**
	 * 將西元年依自訂格式轉換為民國年	<br/>
	 * Y --> 民國年	<br/>
	 * M --> 月份		<br/>
	 * D --> 日期		<br/>
	 * H --> 時間(24小時制)		<br/>
	 * h --> 時間(12小時制)		<br/>
	 * m --> 分鐘		<br/>
	 * s --> 秒		<br/>
	 * W --> 中文星期幾	<br/>
	 * w --> 數字星期幾	<br/>
	 * @param date
	 * @param format
	 * @return
	 */
	public static String toChineseDate(Date date,String formatStr){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int intYear = cal.get(Calendar.YEAR)-1911;
		int intMonth = cal.get(Calendar.MONTH)+1;
		int intDay = cal.get(Calendar.DAY_OF_MONTH);
		int intHr12 = cal.get(Calendar.HOUR);
		int intHr24 = cal.get(Calendar.HOUR_OF_DAY);
		int intMin = cal.get(Calendar.MINUTE);
		int intSec = cal.get(Calendar.SECOND);
		int intWeekDay = cal.get(Calendar.DAY_OF_WEEK);
		
		formatStr = formatStr.replaceAll("Y", String.valueOf(intYear));
		formatStr = formatStr.replaceAll("M", String.valueOf(intMonth));
		formatStr = formatStr.replaceAll("D", String.valueOf(intDay));
		formatStr = formatStr.replaceAll("H", String.format("%02d", intHr24));
		formatStr = formatStr.replaceAll("h", String.format("%02d", intHr12));
		formatStr = formatStr.replaceAll("m", String.format("%02d", intMin));
		formatStr = formatStr.replaceAll("s", String.format("%02d", intSec));
		formatStr = formatStr.replaceAll("W", String.valueOf(toChineseWeekDay(intWeekDay)));
		formatStr = formatStr.replaceAll("w", String.valueOf((intWeekDay-1==0)?7:intWeekDay-1));
		return formatStr;
	}
	
	public static String toChineseWeekDay(int weekDay){
		switch(weekDay){
			case Calendar.SUNDAY:
				return "日";
			case Calendar.MONDAY:
				return "一";
			case Calendar.TUESDAY:
				return "二";
			case Calendar.WEDNESDAY:
				return "三";
			case Calendar.THURSDAY:
				return "四";
			case Calendar.FRIDAY:
				return "五";
			case Calendar.SATURDAY:
				return "六";
			default:
				return null;
		}
	}
	/**
	 * 6碼民國年轉西元(A.D)
	 * param String
	 * return java.util.Date
	 */
	public static java.util.Date taiwan6CharToADdate(String AD) throws Exception{
		if("".equals(AD.trim())||6!=AD.trim().length()){
			throw new Exception("輸入的格式錯誤!");
		}
		
		String yy = AD.substring(0 , 2);
		String mm = AD.substring(2 , 4);
		String dd = AD.substring(4 , 6);
		
		if(    !Pattern.compile("[0-9]*").matcher(yy).matches()
		    || !Pattern.compile("[0-9]*").matcher(mm).matches()
		    || !Pattern.compile("[0-9]*").matcher(dd).matches()){
			
			throw new Exception("請輸入數字格式!");
		}
		    //檢查月份與日期是否正確
		int yyInt = Integer.parseInt(yy);
		int mmInt = Integer.parseInt(mm);
		int ddInt = Integer.parseInt(dd);
		
		if(mmInt== 1|| mmInt== 3|| mmInt==5|| mmInt==7
			|| mmInt==8||mmInt== 10||mmInt== 12){
			if(ddInt < 1 || ddInt > 31){
				throw new Exception("日期格式錯誤!");
			}
		}else{
			if(mmInt== 2){
				//檢查二月天數
				if(yyInt%4==0){
					if(ddInt < 1 || ddInt > 29){
						throw new Exception("日期格式錯誤!");
					}
				}else{
					if(ddInt < 1 || ddInt > 28){
						throw new Exception("日期格式錯誤!");
					}
				}
			}else{
				if(ddInt < 1 || ddInt > 30){
					throw new Exception("日期格式錯誤!");
				}
			}
		}
		// yyInt + 1911 to A.D.
		yyInt = yyInt + 1911;
		return  new SimpleDateFormat("yyyyMMdd").parse(Integer.toString(yyInt)+mm+dd);
	}
	
	/**
	 * 西元(A.D)轉字串陣列民國年[年,月,日]
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static String[] ADdateToTaiwan6Char(Date date) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String[] splitDate = sdf.format(date).split("/");
		splitDate[0] = String.valueOf(Integer.parseInt(splitDate[0])-1911);
		return splitDate;
	}
}
