/**
 * JS for main index page.
 */

$(document).ready(function(){	
	$("#showQuestion").click(function(){
		$(this).hide();
		let category = $("#category").val();
		let url = '/flashcardzap/showQuestion?category=' + category;
		fetch(url).then (response => {
			return response.json();
		}).then (data => {
			console.log(data);
			$('#card-div').text(data.front);
			$('#card-back').text(data.back);
			$('#area-cell').text(data.area);
			$('#category-cell').text(data.category);
			$('#subcategory-cell').text(data.subcategory);
			$('#card-info').show();			
		});
		$("#showAnswer").show();
	});

	$("#showAnswer").click(function(){	
		var audio = new Audio("/flashcardzap/flashcardzap/resources/530356__danielpodlovics__electricity.wav");
        audio.play();
        let cardBack = $('#card-back').text();
        $('#card-div').text(cardBack);        	
		$(this).hide();
		$("#showQuestion").show();
		
		let headerImage = document.getElementById("header-image");
		
		setTimeout(()=>{
			headerImage.src = "/flashcardzap/flashcardzap/resources/flashcardzhead_bolts_m3.png";
		},100);
		setTimeout(()=>{
			headerImage.src = "/flashcardzap/flashcardzap/resources/flashcardzhead_bolts_m2.png";
		},200);
		setTimeout(()=>{
			headerImage.src = "/flashcardzap/flashcardzap/resources/flashcardzhead_bolts_m1.png";
		},300);
		setTimeout(()=>{
			headerImage.src = "/flashcardzap/flashcardzap/resources/flashcardzhead_bolts.png";
			headerImage.className = "img-responsive shake";
		},400);
		setTimeout(()=>{
			headerImage.className = "img-responsive";
			headerImage.src = "/flashcardzap/flashcardzap/resources/flashcardzhead_plain.png";
		},1000);
		
	});
});