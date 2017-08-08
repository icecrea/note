package com.icecrea.note.dao;

import com.icecrea.note.entity.User;

public interface UserDao {
	User findUserByName(String name);

	int addUser(User user);

	int countUserById(String userId); 
}
