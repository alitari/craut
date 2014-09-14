//p() { yield("eventList") }
table ('border':'1') {
	tr {
	  th ""
	  th "name"
	  th "start Date"
	}
	for (event in model.events) {
		tr {
		  td  
		  {
			  a('href':"delete?id=${event.id}") { 
				  img( 'style':"border:0;", 'src':'/images/Trash.png',  'width':"16", 'height':"16")
			  }
			  
		  }
		  td { a('href':"edit?id=${event.id}") {yield(event.name)} }   
		  td   event.startFormatted 
		  // 
        }
	}
}