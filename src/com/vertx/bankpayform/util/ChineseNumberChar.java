package com.vertx.bankpayform.util;


public class ChineseNumberChar  {
	public static String complexString(String number){
		StringBuffer chineseNumberString =new StringBuffer(""); 
		final String[] numbers= {"零","壹","貳","參","肆","伍","陸","柒","捌","玖","拾"};
		final String[] units  = {"", "拾","佰","仟","萬"};
		
		char[] handleChar = number.toCharArray();
		for (int i = 0, j = handleChar.length; i < handleChar.length ; i++,j--) {
			int num = Integer.valueOf(String.valueOf( handleChar[i]));
			if(num != 0){
				chineseNumberString.append(numbers[num]);
				chineseNumberString.append(units[j-1]);
			}
		}
		return chineseNumberString.append("元 整").toString();
	}
}
