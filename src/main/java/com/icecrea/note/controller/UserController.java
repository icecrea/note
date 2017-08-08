package com.icecrea.note.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icecrea.note.entity.User;
import com.icecrea.note.service.PasswordException;
import com.icecrea.note.service.UserNameException;
import com.icecrea.note.service.UserNotFoundException;
import com.icecrea.note.service.UserService;
import com.icecrea.note.util.JsonResult;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractController  {
	@Autowired
	private UserService userService;
	
	@RequestMapping("/login.do")
	@ResponseBody
	public Object login(String name,String password){
		User user=userService.login(name, password);
		return new JsonResult(user);
	}
	
	@RequestMapping("/regist.do")
	@ResponseBody
	public JsonResult regist(String name, String nick, String password, String confirm){
	    User user = userService.regist(name, nick, password, confirm);
	    return new JsonResult(user);
	}
	
	  /**
     * 在其他控制器方法执行出现异常时候, 执行
     * 异常处理方法 handleException
     */

	
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseBody
	public JsonResult handleUserNotFound(
	        UserNotFoundException e){
	    e.printStackTrace();
	    return new JsonResult(2,e);
	}

	@ExceptionHandler(PasswordException.class)
	@ResponseBody
	public JsonResult handlePassword(
	        PasswordException e){
	    e.printStackTrace();
	    return new JsonResult(3,e);
	}
	
	@ExceptionHandler(UserNameException.class)
	@ResponseBody
	public JsonResult handleUserName(UserNameException e){
	    e.printStackTrace();
	    return new JsonResult(4,e);
	}
}
