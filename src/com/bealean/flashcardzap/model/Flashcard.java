package com.bealean.flashcardzap.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Flashcard {

	private long id;
    @NotBlank(message="Front must not be blank.")
    @Size( max=1000, message="Front cannot be more than 1000 characters.")
    private String front;
    @NotBlank(message="Back must not be blank.")
    @Size( max=1000, message="Back cannot be more than 1000 characters.")
    private String back;
    @Size( max=30, message="Area cannot be more than 30 characters.")
    @Pattern( regexp="^[a-zA-Z0-9\\._~ -]*$", message="Area includes invalid characters. Allowed characters are letters: A-Z a-z, numbers, and: -_ .~")
	private String area;
    @Size( max=30, message="Category cannot be more than 30 characters.")
    @Pattern( regexp="^[a-zA-Z0-9\\._~ -]*$", message="Category includes invalid characters. Allowed characters are letters: A-Z a-z, numbers, and: -_ .~")
	private String category;
    @Size( max=30, message="Subcategory cannot be more than 30 characters.")
    @Pattern( regexp="^[a-zA-Z0-9\\._~ -]*$", message="Subcategory includes invalid characters. Allowed characters are letters: A-Z a-z, numbers, and: -_ .~")
	private String subcategory;
	private String lastViewed;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFront() {
		return front;
	}

	public void setFront(String front) {
		this.front = front;
	}

	public String getBack() {
		return back;
	}

	public void setBack(String back) {
		this.back = back;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}

	public String getLastViewed() {
		return lastViewed;
	}

	public void setLastViewed(String lastViewed) {
		this.lastViewed = lastViewed;
	}

	@Override
	public String toString() {
		return "Flashcard [id=" + id + ", front=" + front + ", back=" + back + ", area=" + area + ", category="
				+ category + ", subcategory=" + subcategory + ", lastViewed=" + lastViewed + "]";
	}

}
