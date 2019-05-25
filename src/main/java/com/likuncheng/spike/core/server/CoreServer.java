package com.likuncheng.spike.core.server;


import com.likuncheng.spike.core.common.RequestUtils;
import com.likuncheng.spike.core.common.ResponseBase;

public interface CoreServer {
	
	// 秒杀核心步骤
	public ResponseBase spikeCore(String userId, String updateTime);
	
	//队列添加方法
	public ResponseBase queueAdd(RequestUtils requestUtils);
	
	//出队方法
	public ResponseBase getQueue();

}
