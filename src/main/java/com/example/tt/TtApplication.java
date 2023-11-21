package com.example.tt;

import com.example.tt.Controller.SocketTest;
import com.example.tt.dao.ChatBeanMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan(value = "com.example.tt.dao")
public class TtApplication extends SpringBootServletInitializer {

	static ConfigurableApplicationContext context;


	public static ConfigurableApplicationContext getContext() {
		return context;
	}

	public static void main(String[] args) {
		context= SpringApplication.run(TtApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(this.getClass());
	}

}
