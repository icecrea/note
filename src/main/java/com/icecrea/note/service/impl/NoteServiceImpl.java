package com.icecrea.note.service.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icecrea.note.dao.NoteDao;
import com.icecrea.note.dao.NotebookDao;
import com.icecrea.note.dao.UserDao;
import com.icecrea.note.entity.Note;
import com.icecrea.note.entity.User;
import com.icecrea.note.service.NoteNotFoundException;
import com.icecrea.note.service.NoteService;
import com.icecrea.note.service.NotebookNotFoundException;
import com.icecrea.note.service.UserNotFoundException;

@Service("noteService")
public class NoteServiceImpl implements NoteService {
	@Autowired
	private NoteDao noteDao;

	@Resource
	private NotebookDao notebookDao;

	@Autowired
	private UserDao userDao;

	public List<Map<String, Object>> listNotes(String notebookId) throws NotebookNotFoundException {

		if (notebookId == null || notebookId.trim().isEmpty()) {
			throw new NotebookNotFoundException("ID为空");
		}
		// Notebook notebook = notebookDao
		// .findNotebookById(notebookId);
		// if(notebook==null){
		// throw new NotebookNoteFoundExcepotion("没有笔记本");
		// }
		int n = notebookDao.countNotebookById(notebookId);
		if (n != 1) {
			throw new NotebookNotFoundException("没有笔记本");
		}

		return noteDao.findNotesByNotebookId(notebookId);
	}

	public Note getNote(String noteId) throws NoteNotFoundException {
		if (noteId == null || noteId.trim().isEmpty()) {
			throw new NoteNotFoundException("ID空");
		}
		Note note = noteDao.findNoteById(noteId);
		if (note == null) {
			throw new NoteNotFoundException("ID错误");
		}
		return note;
	}

	public Note addNote(String userId, String notebookId, String title)
			throws UserNotFoundException, NotebookNotFoundException {

		if (userId == null || userId.trim().isEmpty()) {
			throw new UserNotFoundException("ID空");
		}
		int number = userDao.countUserById(userId);
		if (number != 1) {
			throw new UserNotFoundException("木有人");
		}
		if (notebookId == null || notebookId.trim().isEmpty()) {
			throw new NotebookNotFoundException("ID空");
		}
		int n = notebookDao.countNotebookById(notebookId);
		if (n != 1) {
			throw new NotebookNotFoundException("没有笔记本");
		}
		if (title == null || title.trim().isEmpty()) {
			title = "葵花宝典";
		}
		String id = UUID.randomUUID().toString();
		String statusId = "0";
		String typeId = "0";
		String body = "";
		long time = System.currentTimeMillis();
		Note note = new Note(id, notebookId, userId, statusId, typeId, title, body, time, time);
		n = noteDao.addNote(note);
		if (n != 1) {
			throw new NoteNotFoundException("保存失败");
		}
		return note;
	}

	public boolean update(String noteId, String title, String body) throws NoteNotFoundException {
		if (noteId == null || noteId.trim().isEmpty()) {
			throw new NoteNotFoundException("ID不能空");
		}
		Note note = noteDao.findNoteById(noteId);
		if (note == null) {
			throw new NoteNotFoundException("没有对应的笔记");
		}
		Note data = new Note();
		if (title != null && !title.equals(note.getTitle())) {
			data.setTitle(title);
		}
		if (body != null && !body.equals(note.getBody())) {
			data.setBody(body);
		}
		data.setId(noteId);
		data.setLastModifyTime(System.currentTimeMillis());
		System.out.println(data);
		int n = noteDao.updateNote(data);
		return n == 1;

	}

	public boolean moveNote(String noteId, String notebookId) throws NotebookNotFoundException, NoteNotFoundException {
		if (noteId == null || noteId.trim().isEmpty()) {
			throw new NoteNotFoundException("ID不能为空");
		}
		Note note = noteDao.findNoteById(noteId);
		if (note == null) {
			throw new NoteNotFoundException("没有对应的笔记");
		}
		if (notebookId == null || notebookId.trim().isEmpty()) {
			throw new NotebookNotFoundException("ID空");
		}
		int n = notebookDao.countNotebookById(notebookId);
		if (n != 1) {
			throw new NotebookNotFoundException("没有笔记本");
		}
		Note data = new Note();
		data.setId(noteId);
		data.setNotebookId(notebookId);
		data.setLastModifyTime(System.currentTimeMillis());

		n = noteDao.updateNote(data);

		return n == 1;
	}
	
	public boolean deleteNote(String noteId) throws NoteNotFoundException {
	    if(noteId==null || noteId.trim().isEmpty()){
	        throw new NoteNotFoundException("ID不能空");
	    }
	    Note note = noteDao.findNoteById(noteId);
	    if(note==null){
	        throw new NoteNotFoundException("没有对应的笔记");
	    } 

	    Note data = new Note();
	    data.setId(noteId);
	    data.setStatusId("0");
	    data.setLastModifyTime(System.currentTimeMillis());

	    int n = noteDao.updateNote(data);

	    return n==1;
	}
}
