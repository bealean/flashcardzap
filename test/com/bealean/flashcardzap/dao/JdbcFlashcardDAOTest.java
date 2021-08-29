package com.bealean.flashcardzap.dao;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import com.bealean.flashcardzap.config.SpringMvcConfig;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)

class JdbcFlashcardDAOTest {
	
	private SingleConnectionDataSource dataSource;
	private FlashcardDAO dao;
	private JdbcTemplate jdbcTemplate;
	
    @BeforeAll
    void setup() {
		SpringMvcConfig dConfig = new SpringMvcConfig();
		Map<String, String> databaseProperties = dConfig.getDatabaseProperties();
		String url = databaseProperties.get("url");
		String user = databaseProperties.get("user");
		String password = databaseProperties.get("password");
		
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl(url);
		dataSource.setUsername(user);
		dataSource.setPassword(password);
        dataSource.setAutoCommit(false);	
        
        jdbcTemplate = new JdbcTemplate(dataSource);
		dao = new JdbcFlashcardDAO(jdbcTemplate);
	}
    
    @AfterEach
    void tearDown() throws SQLException {
        dataSource.getConnection().rollback();
    }
    
    @AfterAll
    void cleanup() throws SQLException {
    	dataSource.destroy();
    }
	
	@Test
	void getNext_allCategoriesUnviewedCard_returnsCardWithNullLastViewed() {
		/* Insert a new card to ensure there is an unviewed card 
		that does not have a record in flashcard_views. */
		String sql = "INSERT INTO flashcard (front, back) VALUES ('test','test')";
		jdbcTemplate.update(sql);
		String lastViewedOfNextCard = dao.getNext("all").getLastViewed();		
		assertTrue(lastViewedOfNextCard == null, "getNext with unviewed cards returns a card with null lastViewed");
	}
	
	@Test
	void getNext_allCategoriesNoUnviewedCards_returnsCardWithMinLastViewed() {
		String sql = "DELETE FROM flashcard f WHERE NOT EXISTS (SELECT 1 FROM flashcard_views fv WHERE fv.flashcard_id = f.id)";
		jdbcTemplate.update(sql);
		String lastViewedOfNextCard = dao.getNext("all").getLastViewed();	
		/* Record in flashcard_views for each time card is viewed. Null view_timestamp not allowed in flashcard_views.
		 Get Max last viewed for each individual card and then sort ascending to get the least recently viewed card. */
		sql = "SELECT MAX(view_timestamp) " + 
				"FROM flashcard_views " + 
				"GROUP BY flashcard_id " + 
				"ORDER BY MAX(view_timestamp) " + 
				"LIMIT 1";
		
		String minLastViewed =  jdbcTemplate.queryForObject(sql, String.class);
		assertTrue(lastViewedOfNextCard.equals(minLastViewed));
	}

}
