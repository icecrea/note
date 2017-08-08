package com.icecrea.note;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.icecrea.note.dao.NotebookDao;

public class NotebookDaoTest extends BaseTest {
	NotebookDao dao;
	
	@Before
	public void initDao(){
		dao=ctx.getBean("notebookDao",NotebookDao.class);
	}
	
	@Test
	public void testfindnotebooksbyid(){
        String userId="52f9b276-38ee-447f-a3aa-0d54e7a736e4";
        List<Map<String, Object>> list=
            dao.findNotebooksByUserId(userId);
        for (Map<String, Object> map : list) {
            System.out.println(map); 
        }
	}
}
