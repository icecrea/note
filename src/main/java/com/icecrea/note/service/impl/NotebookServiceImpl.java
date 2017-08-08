package com.icecrea.note.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.icecrea.note.dao.NotebookDao;
import com.icecrea.note.dao.UserDao;
import com.icecrea.note.service.NotebookService;
import com.icecrea.note.service.UserNotFoundException;

@Service("notebookService")
public class NotebookServiceImpl implements NotebookService{
	    @Resource
	    private NotebookDao notebookDao;

	    @Resource 
	    private UserDao userDao;

	    public List<Map<String, Object>> listNotebooks(String userId) throws UserNotFoundException {

	        if(userId==null || userId.trim().isEmpty()){
	            throw new UserNotFoundException("ID不能空");
	        }

	        int n = userDao.countUserById(userId);
	        if(n!=1){
	            throw new UserNotFoundException("用户不存在");
	        }

	        return notebookDao.findNotebooksByUserId(userId);

	    }
}
