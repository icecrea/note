package com.icecrea.note.dao;

import java.util.List;
import java.util.Map;

import com.icecrea.note.entity.Note;

public interface NoteDao {
	List<Map<String,Object>> findNotesByNotebookId(String notebookId);
	
	Note findNoteById(String noteId);

	int addNote(Note note);
	
	int updateNote(Note note);

}
