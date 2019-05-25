package com.likuncheng.spike.core.entity;


import com.likuncheng.spike.core.common.CreateUserId;

import lombok.Data;

@Data
public class User {
	
	private String userId;
	
	public User() {
		this.userId = CreateUserId.createUserId();
	}
	
	public User(String userId) {
		this.userId = userId;
	}

}
