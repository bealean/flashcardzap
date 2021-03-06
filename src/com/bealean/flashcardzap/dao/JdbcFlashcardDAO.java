package com.bealean.flashcardzap.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.bealean.flashcardzap.model.Flashcard;

public class JdbcFlashcardDAO implements FlashcardDAO {

	private JdbcTemplate jdbcTemplate;

	public JdbcFlashcardDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public int save(Flashcard f) {
		String sql = "INSERT INTO flashcard (front, back, area, category, subcategory) VALUES (?, ?, ?, ?, ?)";

		// returns number of rows affected
		return jdbcTemplate.update(sql, f.getFront(), f.getBack(), f.getArea(), f.getCategory(), f.getSubcategory());
	}

	@Override
	public int update(Flashcard f) {
		String sql = "UPDATE flashcard SET front = ?, back = ?, area = ?, category = ?, subcategory = ? WHERE id = ?";

		return jdbcTemplate.update(sql, f.getFront(), f.getBack(), f.getArea(), f.getCategory(), f.getSubcategory(),
				f.getId());
	}

	@Override
	public int updateLast_Viewed(long id) {
		String sql = "INSERT INTO flashcard_views (flashcard_id, view_timestamp) VALUES (?, now())";
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public int delete(long id) {
		String sql = "DELETE FROM flashcard WHERE id = ?";
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public Flashcard get(long id) {
		String sql = "SELECT * FROM flashcard f " + "LEFT OUTER JOIN flashcard_views fv ON f.id = fv.flashcard_id "
				+ "WHERE f.id = ? " + "ORDER BY fv.view_timestamp DESC LIMIT 1";

		ResultSetExtractor<Flashcard> extractor = new ResultSetExtractor<Flashcard>() {

			@Override
			public Flashcard extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Flashcard flashcard = new Flashcard();
					flashcard.setId(rs.getLong("id"));
					flashcard.setFront(rs.getString("front"));
					flashcard.setBack(rs.getString("back"));
					flashcard.setArea(rs.getString("area"));
					flashcard.setCategory(rs.getString("category"));
					flashcard.setSubcategory(rs.getString("subcategory"));
					flashcard.setLastViewed(rs.getString("view_timestamp"));
					return flashcard;
				}
				return null;
			}
		};
		return jdbcTemplate.query(sql, extractor, id);
	}

	@Override
	public Flashcard getNext(String area, String category, String subcategory) {

		ResultSetExtractor<Flashcard> extractor = new ResultSetExtractor<Flashcard>() {

			@Override
			public Flashcard extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Flashcard flashcard = new Flashcard();
					flashcard.setId(rs.getLong("id"));
					flashcard.setFront(rs.getString("front"));
					flashcard.setBack(rs.getString("back"));
					flashcard.setArea(rs.getString("area"));
					flashcard.setCategory(rs.getString("category"));
					flashcard.setSubcategory(rs.getString("subcategory"));
					flashcard.setLastViewed(rs.getString("last_viewed"));

					return flashcard;
				}
				return null;
			}
		};

		/*
		 * UI does not allow a category to be selected without an area or a subcategory
		 * to be selected without a category.
		 */
		String sql = "SELECT * FROM flashcard f " + "LEFT OUTER JOIN "
				+ "(SELECT flashcard_id, MAX(view_timestamp) AS \"last_viewed\" " + "FROM flashcard_views "
				+ "GROUP BY flashcard_id) fv " + "ON f.id = fv.flashcard_id ";
		String orderBy = "ORDER BY last_viewed NULLS FIRST LIMIT 1";
		if (area.equals("all") && category.equals("all") && subcategory.equals("all")) {
			sql += orderBy;
			return jdbcTemplate.query(sql, extractor);
		} else if (category.equals("all") && subcategory.equals("all")) {
			sql += "WHERE area = ? ";
			sql += orderBy;
			return jdbcTemplate.query(sql, extractor, area);
		} else if (subcategory.equals("all")) {
			sql += "WHERE area = ? AND category = ? ";
			sql += orderBy;
			return jdbcTemplate.query(sql, extractor, area, category);
		} else {
			sql += "WHERE area = ? AND category = ? AND subcategory = ? ";
			sql += orderBy;
			return jdbcTemplate.query(sql, extractor, area, category, subcategory);
		}
	}

	@Override
	public List<Flashcard> list(String area, String category, String subcategory) {

		RowMapper<Flashcard> rowMapper = new RowMapper<Flashcard>() {

			@Override
			public Flashcard mapRow(ResultSet rs, int rowNum) throws SQLException {
				Integer id = rs.getInt("id");
				String front = rs.getString("front").replace("<", "&lt;");
				String back = rs.getString("back").replace("<", "&lt;");
				String area = rs.getString("area");
				String category = rs.getString("category");
				String subcategory = rs.getString("subcategory");
				String lastViewed = rs.getString("last_viewed");

				Flashcard flashcard = new Flashcard();
				flashcard.setId(id);
				flashcard.setFront(front);
				flashcard.setBack(back);
				flashcard.setArea(area);
				flashcard.setCategory(category);
				flashcard.setSubcategory(subcategory);
				flashcard.setLastViewed(lastViewed);

				return flashcard;
			}
		};

		String sql;
		List<Flashcard> listFlashcards;

		if (area.equals("all")) {
			sql = "SELECT * FROM flashcard f " + "LEFT OUTER JOIN "
					+ "(SELECT flashcard_id, MAX(view_timestamp) AS \"last_viewed\" " + "FROM flashcard_views "
					+ "GROUP BY flashcard_id) fv " + "ON f.id = fv.flashcard_id "
					+ "ORDER BY area, category, subcategory";
			listFlashcards = jdbcTemplate.query(sql, rowMapper);
		} else if (category.equals("all")) {
			sql = "SELECT * FROM flashcard f " + "LEFT OUTER JOIN "
					+ "(SELECT flashcard_id, MAX(view_timestamp) AS \"last_viewed\" " + "FROM flashcard_views "
					+ "GROUP BY flashcard_id) fv " + "ON f.id = fv.flashcard_id " + "WHERE area = ? "
					+ "ORDER BY area, category, subcategory";
			listFlashcards = jdbcTemplate.query(sql, rowMapper, area);
		} else if (subcategory.equals("all")) {
			sql = "SELECT * FROM flashcard f " + "LEFT OUTER JOIN "
					+ "(SELECT flashcard_id, MAX(view_timestamp) AS \"last_viewed\" " + "FROM flashcard_views "
					+ "GROUP BY flashcard_id) fv " + "ON f.id = fv.flashcard_id " + "WHERE area = ? AND category = ? "
					+ "ORDER BY area, category, subcategory";
			listFlashcards = jdbcTemplate.query(sql, rowMapper, area, category);
		} else {
			sql = "SELECT * FROM flashcard f " + "LEFT OUTER JOIN "
					+ "(SELECT flashcard_id, MAX(view_timestamp) AS \"last_viewed\" " + "FROM flashcard_views "
					+ "GROUP BY flashcard_id) fv " + "ON f.id = fv.flashcard_id "
					+ "WHERE area = ? AND category = ? AND subcategory = ? " + "ORDER BY area, category, subcategory";
			listFlashcards = jdbcTemplate.query(sql, rowMapper, area, category, subcategory);
		}

		return listFlashcards;
	}

	@Override
	public List<String> listAreas() {

		RowMapper<String> rowMapper = new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String area = rs.getString("area");
				return area;
			}
		};

		String sql = "SELECT DISTINCT area FROM flashcard ORDER BY area";
		List<String> listAreas = jdbcTemplate.query(sql, rowMapper);

		return listAreas;
	}

	@Override
	public List<String> listCategories() {

		RowMapper<String> rowMapper = new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String category = rs.getString("category");
				return category;
			}
		};

		String sql = "SELECT DISTINCT category FROM flashcard ORDER BY category";
		List<String> listCategories = jdbcTemplate.query(sql, rowMapper);

		return listCategories;
	}

	@Override
	public List<String> listSubcategories() {

		RowMapper<String> rowMapper = new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String subcategory = rs.getString("subcategory");
				return subcategory;
			}
		};

		String sql = "SELECT DISTINCT subcategory FROM flashcard ORDER BY subcategory";
		List<String> listSubcategories = jdbcTemplate.query(sql, rowMapper);

		return listSubcategories;
	}

	@Override
	public List<String> getCategories(String areaName) {
		List<String> categoryList = new ArrayList<>();
		SqlRowSet results;
		String sql;

		/*
		 * UI does not allow a category to be selected without an Area selected.
		 */

		sql = "SELECT DISTINCT category FROM flashcard " + "WHERE area = ? " + "ORDER BY category";
		results = jdbcTemplate.queryForRowSet(sql, areaName);

		while (results.next()) {
			categoryList.add(results.getString("category"));
		}

		return categoryList;
	}

	@Override
	public List<String> getSubcategories(String areaName, String categoryName) {
		List<String> subcategoryList = new ArrayList<>();
		SqlRowSet results;
		String sql;

		/*
		 * UI does not allow a subcategory to be selected without an Area and Category
		 * selected.
		 */

		sql = "SELECT DISTINCT subcategory FROM flashcard " + "WHERE area = ? AND category = ? "
				+ "ORDER BY subcategory";
		results = jdbcTemplate.queryForRowSet(sql, areaName, categoryName);

		while (results.next()) {
			subcategoryList.add(results.getString("subcategory"));
		}

		return subcategoryList;
	}

	@Override
	public int exportFlashcards() {
		int result = 0;
		List<Flashcard> cardList = list("all", "all", "all");
		String path = System.getenv("ENV_CONFIG");
		File output = new File(path + "Flashcards.csv");
		try (PrintWriter writer = new PrintWriter(output)) {
			writer.println("Row,Front,Back,Area,Category,Subcategory");
			for (int row = 1; row <= cardList.size(); row++) {
				/*
				 * Replace double quotes in Front and Back values with two double quotes before
				 * exporting to CSV. Model doesn't allow double quotes for other fields. The
				 * list method replaces < with &lt; for display on Manage Flashcards screen.
				 * Revert that replace for CSV. Full values are also enclosed in double quotes
				 * in the println before exporting to CSV.
				 */
				Flashcard card = cardList.get(row - 1);
				String front = card.getFront().replace("\"", "\"\"").replace("&lt;", "<");
				String back = card.getBack().replace("\"", "\"\"").replace("&lt;", "<");
				writer.println("\"" + row + "\",\"" + front + "\",\"" + back + "\",\"" + card.getArea() + "\",\""
						+ card.getCategory() + "\",\"" + card.getSubcategory() + "\"");
			}
			result++;
		} catch (FileNotFoundException e) {
			System.out.println("Caught exception: " + e.getMessage());
		}
		return result;
	}
}
