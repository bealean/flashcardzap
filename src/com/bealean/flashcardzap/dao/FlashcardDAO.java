package com.bealean.flashcardzap.dao;

import java.util.List;

import com.bealean.flashcardzap.model.Flashcard;

public interface FlashcardDAO {

	int save(Flashcard flashcard);
	
	int update(Flashcard flashcard);
	
	int updateLast_Viewed(long id);
	
	int delete(long id);
	
	Flashcard get(long id);
	
	Flashcard getNext(String area, String category, String subcategory);
	
	List<Flashcard> list(String area, String category, String subcategory);
	
	List<String> listAreas();
	
	List<String> listCategories();
	
	List<String> listSubcategories();
	
	List<String> getCategories(String areaName);
	
	List<String> getSubcategories(String areaName, String categoryName);
	
	int exportFlashcards();
	
}
