package com.likuncheng.spike.core.serverImpl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.likuncheng.spike.core.common.BaseApiService;
import com.likuncheng.spike.core.common.ResponseBase;
import com.likuncheng.spike.core.entity.Order;
import com.likuncheng.spike.core.mapper.OrderMapper;
import com.likuncheng.spike.core.server.OrderServer;

@Service
public class OrderServerImpl extends BaseApiService implements OrderServer{
	
	@Autowired
	private OrderMapper orderMapper;

	@Transactional
	@Override
	public ResponseBase createOrder(Order order) {
		Integer createOrder = orderMapper.createOrder(order);
		if(createOrder <= 0) {
			return setResultError("新增订单失败");
		}
		return setResultSuccess("新增用户成功");
	}

	@Transactional
	@Override
	public ResponseBase updateOrderByOrderId(String oId, String userId, String updateTime, String payToken,Integer version) {
		Integer updateOrderByOrderId = orderMapper.updateOrderByOrderId(oId, userId, updateTime, payToken,version.toString());
		if(updateOrderByOrderId <= 0) {
			return setResultError("订单添加用户失败");
		}
		return setResultSuccess("订单添加用户成功");
	}

	@Transactional
	@Override
	public ResponseBase updateOrderPayByOrderId(String oId, Integer isPay, Integer payState,Integer version) {
		Integer updateOrderPayByOrderId = orderMapper.updateOrderPayByOrderId(oId, isPay, payState,version.toString());
		if(updateOrderPayByOrderId <= 0) {
			return setResultError("修改支付记录失败");
		}
		return setResultSuccess("修改支付记录成功");
	}

	@Override
	public ResponseBase getMoneyByOutTradeNo(String oId, String oUnitPrioe) {
		String moneyByOutTradeNo = orderMapper.getMoneyByOutTradeNo(oId, oUnitPrioe);
		if(StringUtils.isEmpty(moneyByOutTradeNo)) {
			return setResultError("用户实际支付金额异常");
		}
		return setResultSuccessData(moneyByOutTradeNo, "用户实际支付金额正常");
	}

	@Override
	public ResponseBase getOrderByOId(String oId) {
		Order orderByOId = orderMapper.getOrderByOId(oId);
		if(orderByOId == null) {
			return setResultError("查找用户异常");
		}
		return setResultSuccessData(orderByOId, "查找用户成功");
	}
	

}
