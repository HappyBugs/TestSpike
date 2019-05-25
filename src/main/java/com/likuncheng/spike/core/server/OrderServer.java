package com.likuncheng.spike.core.server;


import com.likuncheng.spike.core.common.ResponseBase;
import com.likuncheng.spike.core.entity.Order;

public interface OrderServer {

	// 创建订单
	public ResponseBase createOrder(Order order);

	// 用户拿到订单的时候进行修改的数据
	public ResponseBase updateOrderByOrderId(String oId, String userId, String updateTime, String payToken,Integer version);

	// 用户支付之后修改的数据
	public ResponseBase updateOrderPayByOrderId(String oId, Integer isPay, Integer payState,Integer version);

	// 根据订单号进行查询用户支付的金额 用于判断用户支付金额 是否与实际该支付金额一致
	public ResponseBase getMoneyByOutTradeNo(String oId, String oUnitPrioe);

	// 得到order信息
	public ResponseBase getOrderByOId(String oId);

}
