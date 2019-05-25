package com.likuncheng.spike.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.likuncheng.spike.core.entity.Order;

@Mapper
public interface OrderMapper {

	// 创建订单
	public Integer createOrder(Order order);

	// 用户拿到订单的时候进行修改的数据
	public Integer updateOrderByOrderId(@Param("oId") String oId, @Param("userId") String userId,
			@Param("updateTime") String updateTime, @Param("payToken") String payToken,String version);

	// 用户支付之后修改的数据
	public Integer updateOrderPayByOrderId(@Param("oId") String oId, @Param("isPay") Integer isPay,
			@Param("payState") Integer payState,String version);

	// 根据订单号进行查询用户支付的金额 用于判断用户支付金额 是否与实际该支付金额一致
	public String getMoneyByOutTradeNo(@Param("oId") String oId, @Param("oUnitPrioe") String oUnitPrioe);

	// 得到order信息
	public Order getOrderByOId(@Param("oId") String oId);

}
