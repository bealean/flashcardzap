/**
 * JS for Manage Cardz Page
 */

function deleteCard(cardId) {
	let url = '/flashcardzap/delete?id=' + cardId;
	
	fetch(url, {
	    method: 'DELETE',
	  }).then(response => {
		  if (!response.ok) {
			  throw new Error(response.status + ": " + response.statusText);
		  } else {
			  let url = "/flashcardzap/manage?area=all&category=all&subcategory=all";
			  window.location.href = url;
			}		  
	  }).catch(error => {
		  alert(error);
	  });		 	
}

$(document).ready(function() {
	// Set filters based on parameters in URL
	// URL object not supported by IE
	let pageUrl = new URL(document.location);
	let params = pageUrl.searchParams;
	let area = params.get("area");
	let category = params.get("category");
	let subcategory = params.get("subcategory");
	$("#manage-area").val(area);
	$("#manage-category").val(category);
	$("#manage-subcategory").val(subcategory);
	
	if ($("#manage-area").val() !== "all") {
		$("#manage-category").prop("disabled", false);
	} 
	
	if ($("#manage-category").val() !== "all") {
		$("#manage-subcategory").prop("disabled", false);
	} 
	
	$("#manage-area").on('change', function() {
		$("#manage-category").val("all");
		$("#manage-subcategory").val("all");
		let url = '/flashcardzap/manage?area=' + $("#manage-area").val() + '&category=all&subcategory=all';
		window.location.href = url;
	});

	$("#manage-category").on('change', function() {
		$("#manage-subcategory").val("all");
		url = '/flashcardzap/manage?area=' + $("#manage-area").val() + '&category=' + this.value + '&subcategory=all';
		window.location.href = url;
	});
	
	$("#manage-subcategory").on('change', function() {
		url = '/flashcardzap/manage?area=' + $("#manage-area").val() + '&category=' + $("#manage-category").val() + '&subcategory=' + this.value;
		window.location.href = url;
	});
});