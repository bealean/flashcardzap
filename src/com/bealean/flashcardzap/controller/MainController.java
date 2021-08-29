package com.bealean.flashcardzap.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.servlet.ModelAndView;
import com.bealean.flashcardzap.dao.FlashcardDAO;
import com.bealean.flashcardzap.exceptions.DeleteCardException;
import com.bealean.flashcardzap.model.Flashcard;

@Controller
public class MainController {

	@Autowired
	private FlashcardDAO flashcardDAO;

	@RequestMapping(value = "/")
	public ModelAndView showFlashcard() {
		ModelAndView model = new ModelAndView("index");
		List<String> listCategories = flashcardDAO.listCategories();
		model.addObject("listCategories", listCategories);
		model.addObject("front", "Optionally select a category and click Show Question.");
		// Call getNext to speed up display of first question later.
		flashcardDAO.getNext("all");
		return model;
	}

	@RequestMapping(value = "/showQuestion", method = RequestMethod.GET)
	@ResponseBody
	public Flashcard showQuestion(HttpServletRequest request) {
		String category = request.getParameter("category");
		Flashcard flashcard = flashcardDAO.getNext(category);
		flashcardDAO.updateLast_Viewed(flashcard.getId());
		return flashcard;
	}

	@RequestMapping(value = "/manage", method = RequestMethod.GET)
	public ModelAndView listFlashcard(HttpServletRequest request) {
		String category = request.getParameter("category");
		List<Flashcard> listFlashcard = flashcardDAO.list(category);
		ModelAndView model = new ModelAndView("ManageFlashcards");
		model.addObject("listFlashcard", listFlashcard);
		List<String> listCategories = flashcardDAO.listCategories();
		model.addObject("listCategories", listCategories);
		return model;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public ModelAndView newFlashcard(ModelAndView model) {
		Flashcard newFlashcard = new Flashcard();
		model.addObject("flashcard", newFlashcard);
		model.setViewName("FlashcardForm");
		return model;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editFlashcard(HttpServletRequest request) {
		long id = Long.parseLong(request.getParameter("id"));
		Flashcard flashcard = flashcardDAO.get(id);
		ModelAndView model = new ModelAndView("FlashcardForm");
		model.addObject("flashcard", flashcard);
		return model;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView saveFlashcard(@Valid @ModelAttribute Flashcard flashcard, BindingResult result) {
		if (result.hasErrors()) {
			throw new ServerWebInputException(result.getFieldError().getDefaultMessage());
		}
		if (flashcard.getId() == 0) {
			flashcardDAO.save(flashcard);
		} else {
			flashcardDAO.update(flashcard);
		}
		return new ModelAndView("redirect:/manage?category=all");
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteFlashcard(HttpServletRequest request) {
		long id = Long.parseLong(request.getParameter("id"));
		int result = flashcardDAO.delete(id);
		if (result != 1) {
			throw new DeleteCardException();
		}
	}

}
