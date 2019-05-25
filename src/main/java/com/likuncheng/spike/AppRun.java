package com.likuncheng.spike;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.likuncheng.spike.core.common.BaseRedisService;
import com.likuncheng.spike.core.common.ResponseBase;
import com.likuncheng.spike.core.entity.Order;
import com.likuncheng.spike.core.server.OrderServer;

//程序启动之后执行
@Component
public class AppRun implements ApplicationRunner {
	
	@Autowired
	private OrderServer orderServer;

	@Autowired
	private BaseRedisService baseRedisService;

	// 程序启动之后的代码
	@Override
	@Transactional
	public void run(ApplicationArguments args) throws Exception {
		try {
			Order order = null;
			//用于保存所有的订单id
			ArrayList<String> arrayList = new ArrayList<String>();
			// 保存一百个订单
			int sign = 0;
			do {
				//实例化
				order = new Order();
				//创建订单
				ResponseBase createOrder = orderServer.createOrder(order);
				if (createOrder.getRtnCode() == 500) {
					sign++;
				}
				arrayList.add(order.getOId());
			} while (arrayList.size() < 100 || sign >= 10);
			// 2.缓存到redis中
			boolean isTrue = baseRedisService.updateSpike(arrayList, arrayList.size()+"");
			if (!isTrue) {
				throw new Exception("订单缓存失败");
			}
			//保存余额
			System.out.println("新增订单成功");
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}

	}
}
