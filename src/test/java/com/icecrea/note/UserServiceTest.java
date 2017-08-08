package com.icecrea.note;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.icecrea.note.dao.UserDao;
import com.icecrea.note.entity.User;
import com.icecrea.note.service.UserService;

public class UserServiceTest extends BaseTest {
	UserService service;
	
	@Before
	public void initService(){
		service = ctx.getBean("userService", UserService.class);
	}
	
	@Test
	public void test(){
		String name = "demo";
        UserService service = ctx.getBean("userService", UserService.class);
        User user=service.login(name, "123456");
        System.out.println(user); 
	}
	
	@Test
	public void testsalt(){
        UserService service = ctx.getBean("userService", UserService.class);
        User user=service.login("demo", "123456");
        System.out.println(user); 
	}
	
	@Test
	public void test2(){
		User user = service.regist( "Andy", "Andy", "123456", "123456");
	    System.out.println(user); 
	}
}
