package com.bealean.flashcardzap.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import com.bealean.flashcardzap.dao.FlashcardDAO;
import com.bealean.flashcardzap.dao.JdbcFlashcardDAO;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.bealean.flashcardzap")
public class SpringMvcConfig implements WebMvcConfigurer {
	
	@Bean
	public Map<String, String> getDatabaseProperties() {
		Map<String, String> databasePropertiesMap = new HashMap<>();
		
		/* System Environment Variable ENV_CONFIG is configured with the path to the db.cfg file
		 * (including trailing directory separator - e.g. "\" for Windows).
		 * File has a line for each configuration variable and value, 
		 * separated by an equals sign (e.g. user=dbUserValue).*/
		
		String path = System.getenv("ENV_CONFIG");
		File configFile = new File(path + "db.cfg");
		
		try {
			FileReader reader = new FileReader(configFile);
		    Properties props = new Properties();
		    props.load(reader);
		    databasePropertiesMap.put("password", props.getProperty("pass"));
		    databasePropertiesMap.put("user", props.getProperty("user"));
		    databasePropertiesMap.put("url", props.getProperty("url"));
		    reader.close();
		} catch (FileNotFoundException ex) {
		    System.out.println(ex);
		} catch (IOException ex) {
			System.out.println(ex);
		}
		
		return databasePropertiesMap;
	}
	
	@Bean
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		Map<String, String> databaseProperties = getDatabaseProperties();
		//DriverClassName is needed for this data source
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl(databaseProperties.get("url"));
		dataSource.setUsername(databaseProperties.get("user"));
		dataSource.setPassword(databaseProperties.get("password"));
		
		return dataSource;
	}

	@Bean
	public ViewResolver getViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		
		return resolver;
	}
	
	@Bean
	public FlashcardDAO getFlashcardDAO() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
		return new JdbcFlashcardDAO(jdbcTemplate);
	}
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
          .addResourceHandler("/flashcardzap/resources/**")
          .addResourceLocations("/resources/");
    }
	
}
