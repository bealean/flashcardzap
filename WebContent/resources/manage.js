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
			  let url = "/flashcardzap/manage?category=all";
			  window.location.href = url;
			}		  
	  }).catch(error => {
		  alert(error);
	  });		 	
}

$(document).ready(function() {
	// Set Category filter based on category parameter in URL
	// URL object not supported by IE
	let pageUrl = new URL(document.location);
	let params = pageUrl.searchParams;
	let category = params.get("category");
	$("#category").val(category);

	$("#category").on('change', function() {
		// alert( this.value );
		let url = "/flashcardzap/manage?category=" + this.value
		window.location.href = url;
	});
});