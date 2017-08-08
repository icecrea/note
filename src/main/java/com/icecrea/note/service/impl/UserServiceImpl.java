package com.icecrea.note.service.impl;

import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import com.icecrea.note.dao.UserDao;
import com.icecrea.note.entity.User;
import com.icecrea.note.service.PasswordException;
import com.icecrea.note.service.UserNameException;
import com.icecrea.note.service.UserNotFoundException;
import com.icecrea.note.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService{

	@Resource
	private UserDao userDao;
	
	private String salt="icecrea233";
    public User login(String name, String password) 
             throws UserNotFoundException, PasswordException {
         if(password==null || password.trim().isEmpty()){
             throw new PasswordException("密码空");
         }
         if(name==null || name.trim().isEmpty()){
             throw new UserNotFoundException("用户名空");
         }
         User user = userDao.findUserByName( name.trim());
         if(user==null){
             throw new UserNotFoundException("用户名错误");
         }
         String pwd=DigestUtils.md5Hex(salt+password.trim());
         if(pwd.equals(user.getPassword())){
        	 return user;
         }
         if(password.trim().equals(user.getPassword())){
             return user;
         }
         throw new PasswordException("密码错误");
     }

    public User regist(String name, String nick, String password, String confirm)
            throws UserNameException, PasswordException {
        //检查name, 不能重复
        if(name==null || name.trim().isEmpty()){
            throw new UserNameException("不能空");
        }
        User one = userDao.findUserByName(name);
        if(one!=null){
            throw new UserNameException("已注册");
        }
        //检查密码
        if(password==null || password.trim().isEmpty()){
            throw new PasswordException("不能空");
        }
        if(! password.equals(confirm)){
            throw new PasswordException("确认密码不一致");
        }
        //检查nick
        if(nick ==null || nick.trim().isEmpty()){
            nick = name;
        }
        String id = UUID.randomUUID().toString();
        String token = "";

        password = DigestUtils.md5Hex(salt+password);
        User user = new User(id, name, password, token, nick);
        int n = userDao.addUser(user);
        if(n!=1){
            throw new RuntimeException("添加失败!");
        }
        return user;
    }
	
}
