# FlashcardZap

## Introduction
Java web application to view and manage flashcards. Plays an electric buzzing noise and displays a lightning strike animation when the back of the card is revealed.

---
## Architecture

Java web application uses:  
- Spring MVC,
- Spring JdbcTemplate, 
- a PostgreSQL database,
- JavaServer Pages,
- HTML,
- CSS,
- JavaScript,
- Fetch,
- jQuery, and
- Bootstrap.

### Details

#### Database Properties

- Database properties are read from a file called db.cfg, which is stored in the path specified in the ENV_CONFIG system environment variable. The path should include the trailing directory separator.
- The file is expected to have a line for each configuration variable and value, 
 separated by an equals sign (e.g. user=dbUserValue).
 - Example_db.cfg is supplied in the database folder of this project.

#### Export Flashcards to CSV

- Path for saving the file on the server is retrieved from the ENV_CONFIG environment variable. 
- File is saved on server with name Flashcards.csv.
- All field values are enclosed in double quotes before being exported to CSV because they may include special characters, such as commas.
- Because each field is surrounded by double quotes, double quotes within Front and Back values are replaced with two double quotes before exporting to CSV. Model doesn't allow double quotes for other fields.
- FileInputStream is used to read portions of the CSV from the server and write them to the OutputStream of the HttpServletResponse.

#### Bean Validation

Submitted form fields are validated using the @Valid annotation on the controller method and Bean Validation on the corresponding Java model classes, including @Pattern annotations to check that values match expected regular expression patterns. 














