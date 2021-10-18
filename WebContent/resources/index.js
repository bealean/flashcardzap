/**
 * JS for main index page.
 */

$(document).ready(function(){	
	$("#showQuestion").click(function(){
		$(this).hide();
		let area = $("#area").val();
		let category = $("#category").val();
		let subcategory = $("#subcategory").val();
		let url = '/flashcardzap/showQuestion?area=' + area + '&category=' + category + '&subcategory=' + subcategory;
		fetch(url).then (response => {
			return response.json();
		}).then (data => {
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
		const audio = new Audio("/flashcardzap/flashcardzap/resources/530356__danielpodlovics__electricity.wav");
        audio.play();
        const cardBack = $('#card-back').text();
        $('#card-div').text(cardBack);        	
		$(this).hide();
		$("#showQuestion").show();
		
		const headerImage = document.getElementById("header-image");
		
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
	
	$("#area").on('change', function() {
		if ($("#area").val() === "all") {
            $("#category").find("option").remove();  
            $("<option>").val("all").text("All").appendTo($("#category"));
            $("#subcategory").find("option").remove();  
            $("<option>").val("all").text("All").appendTo($("#subcategory"));
			$("#category").prop("disabled", true);
			$("#subcategory").prop("disabled", true);
		} else {
			let url = '/flashcardzap/categories?area=' + $("#area").val();
            $.get(url, function(responseJson) {
                $("#category").find("option").remove();  
                $("<option>").val("all").text("All").appendTo($("#category"));
                $.each(responseJson, function(index, category) {
                    $("<option>").val(category).text(category).appendTo($("#category"));
                });                                   
            });
			$("#subcategory").val("all");
			$("#subcategory").prop("disabled", true);
			$("#category").prop("disabled", false);

		}
	});

	$("#category").on('change', function() {
		if ($("#category").val() === "all") {
            $("#subcategory").find("option").remove();  
            $("<option>").val("all").text("All").appendTo($("#subcategory"));
			$("#subcategory").prop("disabled", true);
		} else {
			let url = '/flashcardzap/subcategories?area=' + $("#area").val() + '&category=' + $("#category").val();
            $.get(url, function(responseJson) {
                $("#subcategory").find("option").remove();  
                $("<option>").val("all").text("All").appendTo($("#subcategory"));
                $.each(responseJson, function(index, subcategory) {
                    $("<option>").val(subcategory).text(subcategory).appendTo($("#subcategory"));
                });                                   
            });
			$("#subcategory").prop("disabled", false);
		}
	});
});