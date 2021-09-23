package com.bealean.flashcardzap.dao;

import java.util.List;

import com.bealean.flashcardzap.model.Flashcard;

public interface FlashcardDAO {

	int save(Flashcard flashcard);
	
	int update(Flashcard flashcard);
	
	int updateLast_Viewed(long id);
	
	int delete(long id);
	
	Flashcard get(long id);
	
	Flashcard getNext(String category);
	
	List<Flashcard> list(String category);
	
	List<String> listAreas();
	
	List<String> listCategories();
	
	List<String> listSubcategories();
	
	int exportFlashcards();
	
}
