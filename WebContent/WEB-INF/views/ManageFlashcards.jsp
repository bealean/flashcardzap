<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!DOCTYPE html>
<!-- TODO: 
	- Limit rows displayed on load using pagination or infinite scroll
	- Sort
	- Save Filters for return from Add/Edit
 -->
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css"
	integrity="sha384-B0vP5xmATw1+K9KRQjQERJvTumQW0nPEzvF6L/Z6nronJ3oUOFUFpCjEUQouq2+l"
	crossorigin="anonymous">
<link href="<c:url value="/flashcardzap/resources/style.css"/>" rel="stylesheet">

<title>Manage Cardz</title>

</head>
<body>
	<div align="center" class="extra-padding-bottom">
		<header style="text-align: center;">
			<div class="row no-gutters">
				<div class="col-sm-2">
					<a href="/flashcardzap/" class="btn margin-top-10">Play
						Flashcards</a>
				</div>
				<div class="col-sm-8">
					<img alt="Manage Flashcardz Header"
						src="<c:url value="/flashcardzap/resources/managecardz.png"/>"
						class=".img-responsive">
				</div>
				<div class="col-sm-2">
					<a href="/flashcardzap/new" class="btn margin-top-10">New
						Flashcard</a>
				</div>
			</div>
		</header>
		<div id="export-div" class="margin-bottom">
			<a class="regular-link" href="/flashcardzap/export">Export All
				Cards</a>
		</div>
		<table class="border-less-table" >
			<tr class="vertical-align-top">
				<th>
					<label for="area">Area</label> 
				</th>
				<td>
					<select name="area" id="manage-area">
						<option value="all">All</option>
						<c:forEach items="${listAreas}" var="area">
							<option value="${area}">${area}</option>
						</c:forEach>
					</select>
				</td>
				<th>
					<label for="category" class="category-header">Category</label> 
				</th>
				<td class="category-data">
					<select name="category" id="manage-category" disabled>
						<option value="all">All</option>
						<c:forEach items="${listCategories}" var="category">
							<option value="${category}">${category}</option>
						</c:forEach>
					</select>
				</td>
				<th>
					<label for="subcategory">Subcategory</label> 
				</th>
				<td>
					<select name="subcategory" id="manage-subcategory" disabled>
						<option value="all">All</option>
						<c:forEach items="${listSubcategories}" var="subcategory">
							<option value="${subcategory}">${subcategory}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
		</table>

		<table id="card-list" class="table-border">
			<tr class="vertical-align-top">
				<th>No.</th>
				<th class="col-width">Front</th>
				<th class="col-width">Back</th>
				<th>Area</th>
				<th>Category</th>
				<th>Subcategory</th>
				<th>Last Viewed</th>
				<th>Action</th>
			</tr>
			<c:forEach items="${listFlashcard}" var="flashcard"
				varStatus="status">
				<tr>
					<td>${status.index + 1}</td>
					<td class="col-wrap">${flashcard.front}</td>
					<td class="col-wrap">${flashcard.back}</td>
					<td>${flashcard.area}</td>
					<td>${flashcard.category}</td>
					<td>${flashcard.subcategory}</td>
					<td>${flashcard.lastViewed}</td>
					<td class="button-column"><a
						href="/flashcardzap/edit?id=${flashcard.id}" class="btn">Edit</a>
						&nbsp; &nbsp;
						<button class="btn" onclick="deleteCard(${flashcard.id})">Delete</button>
					</td>
				</tr>
			</c:forEach>
		</table>
	</div>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	<script src="<c:url value="/flashcardzap/resources/manage.js"/>"></script>
</body>
</html>