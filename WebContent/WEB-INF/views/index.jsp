<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!-- TODO: 
	- Customize Mobile View
 -->

<!DOCTYPE html>
<html>

	<head>
	
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="keywords" content="Flashcards">
	<!-- CSS only -->
	<link
		href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css"
		rel="stylesheet"
		integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We"
		crossorigin="anonymous">
	<link href="<c:url value="/flashcardzap/resources/style.css" />"
		rel="stylesheet">
	
	<title>FlashcardZap</title>
	
	</head>

	<body>
		<header style="text-align: center;">
			<div class="container-fluid">
				<div class="row no-gutters">
					<div class="col-sm-2"></div>
					<div class="col-sm-8">
						<img id=header-image alt="flashcardzap Header"
							src="<c:url value="/flashcardzap/resources/flashcardzhead_plain.png"/>"
							class="img-responsive margin-bottom">
					</div>
					<div class="col-sm-2">
						<nav>
							<a href="/flashcardzap/manage?area=all&category=all&subcategory=all"
								id="home-page-manage-button"
								class="btn conditionally-hide margin-top-10">Manage Flashcards</a>
						</nav>
					</div>
				</div>
			</div>
		</header>
	
		<div align="center">
	
			<table id="home-page-filter-table"
				class="border-less-table margin-bottom">
				<tr>
					<th>
						<label for="area">Area&nbsp;</label> 
					</th>
					<td>
						<select name="area" id="area">				
							<option value="all">All</option>
							<c:forEach items="${listAreas}" var="area">
								<option value="${area}">${area}</option>
							</c:forEach>
						</select>
					</td>

					<th class="category-header">
						<label for="category">Category&nbsp;</label> 
					</th>
					<td class="category-data">						
						<select name="category" id="category" disabled>				
							<option value="all">All</option>
							<c:forEach items="${listCategories}" var="category">
								<option value="${category}">${category}</option>
							</c:forEach>
						</select>
					</td>

					<th>
						<label for="subcategory">Subcategory&nbsp;</label> 
					</th>
					<td>					
						<select name="subcategory" id="subcategory" disabled>				
							<option value="all">All</option>
							<c:forEach items="${listSubcategories}" var="subcategory">
								<option value="${subcategory}">${subcategory}</option>
							</c:forEach>
						</select>
					</td>

				</tr>
			</table>
			<table class="border-less-table conditionally-hide margin-bottom">
				<tbody id="card-info" style="display: none;">
					<tr>
						<th>Area:</th>
						<td id="area-cell"></td>
						<th class="category-header">Category:</th>
						<td class="category-data" id="category-cell"></td>
						<th>Subcategory:</th>
						<td id="subcategory-cell"></td>
					</tr>
				</tbody>
			</table>
	
			<div id="card-div">${front}</div>
			<div id="card-back" style="display: none;"></div>
			
			<div id="home-flip-card-buttons">
				<input type="button" class="btn" id="showQuestion" value="Show Question"> 
				<input type="button" class="btn" id="showAnswer" style="display: none;" value="Enlighten Me">
			</div>
			
			<hr>
			
			<footer class="margin-bottom">Your Flashcards Web App Since 2021 </footer>
			
		</div>
		
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
		<script src="<c:url value="/flashcardzap/resources/index.js"/>"></script>
		
	</body>
</html>