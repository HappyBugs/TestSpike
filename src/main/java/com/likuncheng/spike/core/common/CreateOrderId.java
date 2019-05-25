package com.likuncheng.spike.core.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateOrderId {
	
    private static Integer SORT = 0;
	
	public static String createOrderId() {
		synchronized (SORT) {
			Date date = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			SORT++;
			String orderId = "ORDER-"+sf.format(date)+"-"+System.currentTimeMillis()+"-"+SORT;
			return orderId;
		}
	}

}
