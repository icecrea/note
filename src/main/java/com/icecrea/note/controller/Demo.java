package com.icecrea.note.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class Demo {
	@Test
	public void test(){
		String salt="icecrea233";
	 	String pwd=DigestUtils.md5Hex(salt+"123456");
	 	System.out.println(pwd);
	}
}
