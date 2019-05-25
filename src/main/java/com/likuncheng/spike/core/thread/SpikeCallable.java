package com.likuncheng.spike.core.thread;

import java.util.concurrent.Callable;

import org.springframework.transaction.annotation.Transactional;

import com.likuncheng.spike.core.common.ResponseBase;
import com.likuncheng.spike.core.serverImpl.CoreServerImpl;

//处理秒杀的线程
public class SpikeCallable implements Callable<ResponseBase> {
	
	private CoreServerImpl coreServerImpl;
	private String userId;
	private String updateTime;
	
	public SpikeCallable(CoreServerImpl coreServer,String userId,String updateTime) {
		this.coreServerImpl = coreServer;
		this.userId = userId;
		this.updateTime = updateTime;
	}
	
	//调用秒杀的方法 实现秒杀
	@Transactional
	private ResponseBase spikeCore(String userId,String updateTime) {
		ResponseBase spikeCore = coreServerImpl.spikeCore(userId, updateTime);
		return spikeCore;
	}
	

	@Override
	public ResponseBase call() throws Exception {
		return spikeCore(userId,updateTime);
	}

}
