package com.likuncheng.spike.core.serverImpl;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.likuncheng.spike.core.common.BaseApiService;
import com.likuncheng.spike.core.common.BaseRedisService;
import com.likuncheng.spike.core.common.CreatePayToken;
import com.likuncheng.spike.core.common.RequestUtils;
import com.likuncheng.spike.core.common.ResponseBase;
import com.likuncheng.spike.core.entity.Order;
import com.likuncheng.spike.core.entity.User;
import com.likuncheng.spike.core.mapper.OrderMapper;
import com.likuncheng.spike.core.server.CoreServer;
import com.likuncheng.spike.core.thread.SpikeCallable;

@Service
public class CoreServerImpl extends BaseApiService implements CoreServer {

	// 创建一个容量为100的队列
	public static LinkedBlockingQueue<RequestUtils> linkedBlockingQueue;

	@Autowired
	private BaseRedisService baseRedisService;

	@Autowired
	private OrderMapper orderMapper;
	
	//支付token过期时间
	private static final long PAYTOKENOUTTIME = 1 * 60 * 1000;

	//自定义线程池 空闲线程数2 最大线程数 8 如果线程超过3秒没有进入队列处理就报错 创建阻塞队列 缓存多余的线程最大缓存100个线程
	public static ThreadPoolExecutor ioThreadPoolExecutor = new ThreadPoolExecutor(2, 8, 3, TimeUnit.SECONDS,
			new LinkedBlockingQueue<>(100));

	@Override
	public ResponseBase queueAdd(RequestUtils requestUtils) {
		try {
			//第一次进来 队列实例化
			if (linkedBlockingQueue == null) {
				linkedBlockingQueue = new LinkedBlockingQueue<RequestUtils>(100);
			}
			// 保存到队列 如果三秒都没有入队 那就
			boolean offer = linkedBlockingQueue.offer(requestUtils, 3, TimeUnit.SECONDS);
			if (!offer) {
				throw new Exception("入队失败");
			}
		} catch (Exception e) {
			return setResultSuccessData(false, "入队失败");
		}
		return setResultSuccessData(true, "入队成功");
	}

	@Override
	public ResponseBase getQueue() {
		ResponseBase responseBase = null;
		try {
			//判断当前队列是否存在数据
			if (linkedBlockingQueue.size() <= 0) {
				throw new Exception("当前没有请求");
			}
			//如果 没有了 会抛出错误 
			Integer surplus = getSurplus();
			if(surplus <= 0) {
				//先把队列里面的数据消费了然后抛出异常  避免后面的请求加入不进去
				linkedBlockingQueue.poll(3, TimeUnit.SECONDS);
				throw new Exception("已经被抢光了===========");
			}
			RequestUtils poll = linkedBlockingQueue.poll(3, TimeUnit.SECONDS);
			User user = poll.getUser();
			String updateTime = poll.getCreateTime();
			//获得当前类的代理对象
			CoreServerImpl currentProxy = (CoreServerImpl) AopContext.currentProxy();
			//线程池处理秒杀
			Future<ResponseBase> submit = ioThreadPoolExecutor
					.submit(new SpikeCallable(currentProxy, user.getUserId(), updateTime));
			//返回结果
			responseBase = submit.get();
		} catch (Exception e) {
			return setResultError("失败：" + e.getMessage());
		}
		return responseBase;
	}

	//得到剩余的商品数
	public Integer getSurplus(){
		String string = (String) baseRedisService.getString("spike");
		// 类型转换
		JSONObject parseObject = JSONObject.parseObject(string);
		// 获得剩余
		Integer integer = parseObject.getInteger("surplus");
		return integer;
	}
	
	// 得到redis中的数据
	public List<String> getOrderIds() throws Exception {
		String string = (String) baseRedisService.getString("spike");
		// 类型转换
		JSONObject parseObject = JSONObject.parseObject(string);
		// 获得剩余
		Integer integer = parseObject.getInteger("surplus");
		// 表示已经秒杀成功没有了
		if (integer <= 0) {
			throw new Exception("已经被抢光了");
		}
		JSONArray jsonArray = parseObject.getJSONArray("orderIds");
		String jsonString = JSONObject.toJSONString(jsonArray);
		List<String> parseArray = JSONObject.parseArray(jsonString, String.class);
		return parseArray;
	}

	//处理秒杀的核心方法
	@Override
	//如果是别的内部调用的话 就新创建一个事务 不受外部事物影响
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ResponseBase spikeCore(String userId, String updateTime) {
		try {
			//创建paytoken
			String payToken = CreatePayToken.createPayToekn();
			List<String> parseArray = getOrderIds();
			String orderId = parseArray.get((parseArray.size() - 1));
			Order orderByOId = orderMapper.getOrderByOId(orderId);
			//进行订单的修改 
			Integer updateOrderByOrderId = orderMapper.updateOrderByOrderId(orderId, userId, updateTime, payToken,
					orderByOId.getVersion().toString());
			// 如果修改订单失败
			if (updateOrderByOrderId <= 0) {
				throw new Exception("订单修改失败");
			}
			// 如果成功修改的时候就把数据从集合中删除
			parseArray.remove((parseArray.size() - 1));
			// 从新封装json并且进行保存到reids 如果这里面报错 那么会回滚 然后前面的 updateOrderByOrderId也会回滚
			boolean isTrue = baseRedisService.updateSpike(parseArray, parseArray.size() + "");
			if (!isTrue) {
				throw new Exception("修改剩余缓存失败");
			}
			// 保存支付token到redis中
			baseRedisService.setString("payToken" + userId, payToken, PAYTOKENOUTTIME);
		} catch (Exception e) {
			// 手动回滚
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return setResultError("失败:"+e.getMessage());
		} finally {
		}
		return setResultSuccess("秒杀成功");
	}

}
