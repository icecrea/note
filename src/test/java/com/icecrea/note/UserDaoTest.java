package com.icecrea.note;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.icecrea.note.dao.UserDao;
import com.icecrea.note.entity.User;

public class UserDaoTest extends BaseTest{

	UserDao dao;
	

	@Before
	public void initDao(){
		dao = ctx.getBean("userDao", UserDao.class);
	}
	
	
	@Test
	public void test(){
        User user = dao.findUserByName("demo");
        System.out.println(user); 
	}
	
	@Test
	public void testAddUser(){
		String id=UUID.randomUUID().toString();
		String name="icecrea";
		String password="123456";
		String token="";
		String nick="icecrea";
		String salt="icecrea233";
		String pwd=DigestUtils.md5Hex(salt+password);
		User user=new User(id,name,pwd,token,nick);
		int n=dao.addUser(user);
		System.out.println(n);
	}
}
