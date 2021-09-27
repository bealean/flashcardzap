/* JS for Add/Edit screen. */

document.addEventListener("DOMContentLoaded", () => {
	const textAreaElements = document.querySelectorAll("textarea");
	/* Get max-height for front textarea element from CSS file. 
	 * Back textarea should use same max-height because both display in the same div when playing cards. */
	let maxHeight = window.getComputedStyle(textAreaElements[0]).getPropertyValue('max-height');
	//Remove px after max-height number
	maxHeight = Number(maxHeight.substring(0,maxHeight.length-2));
	 /* Limit text entry to the textarea max-height, which is equal to the height of the div
	  * used for playing cards. The maxlength attribute is set for the textarea fields to limit the total characters, 
	 * but not height. */
	textAreaElements.forEach((element) => {
		element.addEventListener('keyup', (event) => {
			limitRow(event.currentTarget);
		});
	});
	
	function limitRow(textAreaElement) {
		/* The max-height property for the textarea fields controls how 
		 * large the textarea box can be on the page. If more rows are entered 
		 * than can be displayed in the box, a vertical scrollbar appears.
		 * When a scrollbar is present, the scrollHeight represents the height 
		 * of all of the element's content, not just the portion displayed. */
		if (textAreaElement.scrollHeight > maxHeight){
			alert("The entry exceeds height of card. Entry will be truncated to fit card.");
			let entryText = textAreaElement.value;
			while (textAreaElement.scrollHeight > maxHeight) {
				textAreaElement.value = entryText.substring(0, entryText.length-1);
				entryText = textAreaElement.value;
			}
		}
	}
	
});

