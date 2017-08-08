package com.icecrea.note.service;

import com.icecrea.note.entity.User;

public interface UserService {
	User login(String name,String password)
		throws UserNotFoundException,PasswordException;

	User regist(String name, String nick, String password, String confirm)
		    throws UserNameException, PasswordException;
}
