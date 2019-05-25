package com.likuncheng.spike.core.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.likuncheng.spike.core.common.BaseApiService;
import com.likuncheng.spike.core.common.RequestUtils;
import com.likuncheng.spike.core.common.ResponseBase;
import com.likuncheng.spike.core.entity.User;
import com.likuncheng.spike.core.server.CoreServer;

@RestController
public class SpikeCoreController extends BaseApiService {

	@Autowired
	private CoreServer orderServer;


	// 1.去到抢购页面的地址
	// 2.用户点击抢购
	@RequestMapping(value = "/spike")
	public String spike() {
		try {
			// 上锁把锁
			synchronized (orderServer) {
				//保存到队列中
				ResponseBase queueAdd = orderServer.queueAdd(
						new RequestUtils(new User(), new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date())));
				boolean data = (boolean) queueAdd.getData();
				if (!data) {
					return "秒杀失败："+queueAdd.getMsg();
				}
				//获取队列
				ResponseBase queue = orderServer.getQueue();
				//控制台打印 查看结果
				System.out.println(queue.getMsg());
				return queue.getMsg();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "秒杀失败:" + e.getMessage();
		}
	}

}
