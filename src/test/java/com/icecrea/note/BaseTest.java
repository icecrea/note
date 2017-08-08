package com.icecrea.note;

import org.junit.After;
import org.junit.Before;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class BaseTest {
	protected ClassPathXmlApplicationContext ctx;
	@Before
	public void initCtx() {
		ctx = new ClassPathXmlApplicationContext(
				"conf/spring-mybatis.xml", 
				"conf/spring-mvc.xml");
	}

	@After
	public void destory() {
		ctx.close();
	}
}
