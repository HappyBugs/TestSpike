package com.likuncheng.spike.core.common;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import io.netty.util.internal.StringUtil;

@Component
public class BaseRedisService {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	public boolean setString(String key, String value, Long timeout) {
		try {
//			stringRedisTemplate.multi();
			if(StringUtil.isNullOrEmpty(key) == true || StringUtil.isNullOrEmpty(value) == true || timeout == null || timeout <= 0) {
				throw new NullPointerException("redis封装出错，请设置合格的参数!");
			}
			stringRedisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
//			stringRedisTemplate.exec();
		} catch (Exception e) {
			e.printStackTrace();
//			stringRedisTemplate.discard();
			return false;
		}
//		finally {
//			关闭redis连接
//			RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
//		}
		return true;
	}
	

	public Object getString(String key) {
		try {
			return stringRedisTemplate.opsForValue().get(key);
		} catch (Exception e) {
			System.out.println();
			System.err.println(e.getMessage());
			System.out.println();
			return null;
		}
	}

	public void delKey(String key) {
		stringRedisTemplate.delete(key);
	}
	
	//更新redis的缓存 
	public boolean updateSpike(List<String> orderIds,String surplus) {
		boolean isTrue = false;
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name", "秒杀商品");
			String newOrderIds = JSONObject.toJSONString(orderIds);
			jsonObject.put("orderIds",newOrderIds);
			jsonObject.put("surplus", surplus);
			String value = jsonObject.toString();
			//过期时间
			long outTime = 1*60*60*1000;
			//是否存在该值
			if(stringRedisTemplate.hasKey("spike")) {
				//存在就更新
				isTrue = stringRedisTemplate.opsForValue().setIfPresent("spike", value,outTime,TimeUnit.SECONDS);
			}else {
				//否则就新创建
				isTrue = setString("spike", value, outTime);
			}
		} catch (Exception e) {
			return isTrue;
		}
		return isTrue;
	}

}
