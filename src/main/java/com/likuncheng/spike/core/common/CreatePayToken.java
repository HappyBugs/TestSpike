package com.likuncheng.spike.core.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class CreatePayToken {

	private static final String SUO = "";

	public static final String[] ARRAYS = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
			"Q", "R", "S", "T", "U", "V", "W", "X", "Y","Z" };
	
	public static String getRandom() {
		String result = "";
		for(int i = 0 ; i < 10 ; i++) {
			result = result+ARRAYS[new Random().nextInt(26)];
		}
		return result;
	}

	public static String createPayToekn() {
		synchronized (SUO) {
			Date date = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			String payToken = "PAYTOKEN-" + sf.format(date) + "-" + System.currentTimeMillis()+getRandom();
			return payToken;
		}
	}

}
