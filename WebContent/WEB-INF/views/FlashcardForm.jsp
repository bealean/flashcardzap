<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css"
	integrity="sha384-B0vP5xmATw1+K9KRQjQERJvTumQW0nPEzvF6L/Z6nronJ3oUOFUFpCjEUQouq2+l"
	crossorigin="anonymous">
<link href="<c:url value="/flashcardzap/resources/style.css" />"
	rel="stylesheet">

<title>Add/Edit Flashcard</title>
</head>
<body>
	<header style="text-align: center;">

		<div class="row no-gutters">
			<div class="col-sm-2"></div>
			<div class="col-sm-8">
				<img alt="Add/Edit Card Header"
					src="<c:url value="/flashcardzap/resources/addeditcard.png"/>"
					class=".img-responsive margin-bottom">
			</div>
			<div class="col-sm-2"></div>
		</div>

	</header>
	<div align="center">

		<form:form action="save" method="post" modelAttribute="flashcard">
			<table class="table-border" id="edit-form-table">
				<form:hidden path="id" />
				<tbody class="tbody-padding">
					<tr>
						<th valign="top">Front:</th>
						<td><form:textarea maxlength="1000" path="front" /></td>
					</tr>
					<tr>
						<th valign="top">Back:</th>
						<td><form:textarea maxlength="1000" path="back" /></td>
					</tr>
					<tr>
						<th>Area:</th>
						<td>
							<form:input path="area" class="add-edit-input" maxlength="30" type="text" list="areas"/>
							<datalist id="areas">
								<c:forEach items="${listAreas}" var="area">
									<option value="${area}">${area}</option>
								</c:forEach>
							</datalist>
						</td>
					</tr>
					<tr>
						<th>Category:</th>
						<td>
						<form:input path="category" class="add-edit-input" maxlength="30" type="text" list="categories"/>
						<datalist id="categories">
							<c:forEach items="${listCategories}" var="category">
								<option value="${category}">${category}</option>
							</c:forEach>
						</datalist>
						</td>
					</tr>
					<tr>
						<th>Subcategory:</th>
						<td>
						<form:input path="subcategory" class="add-edit-input" maxlength="30" type="text" list="subcategories"/>
							<datalist id="subcategories">
								<c:forEach items="${listSubcategories}" var="subcategory">
									<option value="${subcategory}">${subcategory}</option>
								</c:forEach>
							</datalist>
						</td>
					</tr>
					<tr>
						<td colspan="2" align="center"><input id="save-card-button"
							type="submit" value="Save" class="btn"> <a
							href="/flashcardzap/manage?category=all"
							id="add-edit-cancel-button" class="btn">Cancel</a></td>
					</tr>
				</tbody>
			</table>
		</form:form>

	</div>
	<script src="<c:url value="/flashcardzap/resources/add-edit.js"/>"></script>
</body>
</html>