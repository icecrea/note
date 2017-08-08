package com.icecrea.note;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.icecrea.note.entity.Note;
import com.icecrea.note.service.NoteService;

public class NoteServiceTest extends BaseTest{
	 NoteService service;

	    @Before
	    public void initService(){
	        service = ctx.getBean("noteService",
	                NoteService.class);
	    }

	    @Test
	    public void testListNotes(){
	        String id="fa8d3d9d-2de5-4cfe-845f-951041bcc461";
	        List<Map<String, Object>> list=
	            service.listNotes(id);
	        for (Map<String, Object> map : list) {
	            System.out.println(map); 
	        }
	    }

	    @Test
	    public void testGetNote(){
	        String noteId = "019cd9e1-b629-4d8d-afd7-2aa9e2d6afe0";
	        Note note = service.getNote(noteId);
	        System.out.println(note);
	    }
}
