package com.likuncheng.spike.core.common;

import com.likuncheng.spike.core.entity.User;

import lombok.Data;

//保存请求参数 封装到队列里李的工具类
@Data
public class RequestUtils {
	
	private User user;
	private String createTime;

	public RequestUtils(User user,String createTime) {
		//获取user
		this.user = user;
		//这里应该是修改时间 updateTime 也懒得改了
		this.createTime = createTime;
	}
	
}
