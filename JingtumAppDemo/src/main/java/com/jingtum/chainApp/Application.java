package com.jingtum.chainApp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.annotation.EnableTransactionManagement;
@EnableTransactionManagement
@SpringBootApplication
@ComponentScan(basePackages = { "com.jingtum.chainApp"})
@MapperScan("com.jingtum.chainApp.mapper")
@ServletComponentScan
public class Application implements ApplicationListener<ContextRefreshedEvent> {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	

}