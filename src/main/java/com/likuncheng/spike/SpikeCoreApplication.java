package com.likuncheng.spike;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@EnableAspectJAutoProxy(exposeProxy=true)
@SpringBootApplication
public class SpikeCoreApplication{

	public static void main(String[] args) {
		SpringApplication.run(SpikeCoreApplication.class, args);
	}


}
