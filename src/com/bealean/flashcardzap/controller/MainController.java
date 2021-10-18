package com.bealean.flashcardzap.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.servlet.ModelAndView;
import com.bealean.flashcardzap.dao.FlashcardDAO;
import com.bealean.flashcardzap.exceptions.DeleteCardException;
import com.bealean.flashcardzap.model.Flashcard;
import com.google.gson.Gson;

@Controller
public class MainController {

	@Autowired
	private FlashcardDAO flashcardDAO;

	@RequestMapping(value = "/")
	public ModelAndView showFlashcard() {
		ModelAndView model = new ModelAndView("index");
		List<String> listAreas = flashcardDAO.listAreas();
		model.addObject("listAreas", listAreas);
		model.addObject("front", "Optionally select a category and click Show Question.");
		// Call getNext to speed up display of first question later.
		flashcardDAO.getNext("all", "all", "all");
		return model;
	}

	@RequestMapping(value = "/categories", method = RequestMethod.GET)
	public void getCategories(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String areaName = request.getParameter("area");
		List<String> listCategories = flashcardDAO.getCategories(areaName);
		String json = new Gson().toJson(listCategories);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}

	@RequestMapping(value = "/subcategories", method = RequestMethod.GET)
	public void getSubcategories(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String areaName = request.getParameter("area");
		String categoryName = request.getParameter("category");
		List<String> listSubcategories = flashcardDAO.getSubcategories(areaName, categoryName);
		String json = new Gson().toJson(listSubcategories);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}

	@RequestMapping(value = "/showQuestion", method = RequestMethod.GET)
	@ResponseBody
	public Flashcard showQuestion(HttpServletRequest request) {
		String area = request.getParameter("area");
		String category = request.getParameter("category");
		String subcategory = request.getParameter("subcategory");
		Flashcard flashcard = flashcardDAO.getNext(area, category, subcategory);
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
		List<String> listAreas = flashcardDAO.listAreas();
		model.addObject("listAreas", listAreas);
		List<String> listCategories = flashcardDAO.listCategories();
		model.addObject("listCategories", listCategories);
		List<String> listSubcategories = flashcardDAO.listSubcategories();
		model.addObject("listSubcategories", listSubcategories);
		model.setViewName("FlashcardForm");
		return model;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editFlashcard(HttpServletRequest request) {
		long id = Long.parseLong(request.getParameter("id"));
		Flashcard flashcard = flashcardDAO.get(id);
		ModelAndView model = new ModelAndView("FlashcardForm");
		model.addObject("flashcard", flashcard);
		List<String> listAreas = flashcardDAO.listAreas();
		model.addObject("listAreas", listAreas);
		List<String> listCategories = flashcardDAO.listCategories();
		model.addObject("listCategories", listCategories);
		List<String> listSubcategories = flashcardDAO.listSubcategories();
		model.addObject("listSubcategories", listSubcategories);
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

	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public void downloadFlashcards(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int result = flashcardDAO.exportFlashcards();
		if (result < 1) {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Export failed.");
		}
		String sourceFilePath = System.getenv("ENV_CONFIG") + "Flashcards.csv";
		File file = new File(sourceFilePath);
		String mimeType = "application/octet-stream";
		response.setContentType(mimeType);
		response.setContentLength((int) file.length());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=\"Flashcards.csv\"";
		response.setHeader(headerKey, headerValue);
		try (InputStream inputStream = new FileInputStream(file);
				OutputStream outputStream = response.getOutputStream()) {
			final int BUFFER_SIZE = 4096;
			byte[] buffer = new byte[BUFFER_SIZE];
			// Returns bytes read or -1 at end of stream
			int bytesRead = inputStream.read(buffer);
			while (bytesRead != -1) {
				outputStream.write(buffer, 0, bytesRead);
				bytesRead = inputStream.read(buffer);
			}
		} catch (IOException e) {
			System.out.println("Caught exception: " + e.getMessage());
		}
	}

}
