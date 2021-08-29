package com.bealean.flashcardzap.dao;

import java.util.List;

import com.bealean.flashcardzap.model.Flashcard;

public interface FlashcardDAO {

	public int save(Flashcard flashcard);
	
	public int update(Flashcard flashcard);
	
	public int updateLast_Viewed(long id);
	
	public int delete(long id);
	
	public Flashcard get(long id);
	
	public Flashcard getNext(String category);
	
	public List<Flashcard> list(String category);
	
	public List<String> listCategories();
	
}
