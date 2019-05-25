package com.likuncheng.spike.core.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.likuncheng.spike.core.common.CreateOrderId;

import lombok.Data;

//商品
@Data
public class Order {
	
	//商品id
	private String oId;
	//商品名称
	private String oName;
	//商品描述
	private String oDesoribe;
	//单价
	private String oUnitPrioe;
	//创建时间
	private String createTime;
	//修改时间
	private String updateTime;
	//用户id
	private String userId;
	//是否支付(0:未支付  1:已支付)
	private Integer isPay;
	//支付状态(0:支付失败  1:支付成功  2:支付异常)
	private Integer payState;                      
	//支付token
	private String payToken;                      
	//版本号 默认为0
	private Integer version;
	
	public Order() {
		this.oId = CreateOrderId.createOrderId();
		this.oName = "测试商品";
		this.oDesoribe = "用于测试秒杀专业商品";
		this.oUnitPrioe = "99.5";
		Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
		String format = sf.format(date);
		this.createTime = format;
	}
	
	
	

}
