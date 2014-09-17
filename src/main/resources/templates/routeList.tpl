table ('border':'1') {
	tr {
	  th ""
	  th "name"
	  th "start Date"
//	  th "Map"
	  
	}
	for (route in model.routes) {
		tr {
		  td  
		  {
			  a('href':"delete?id=${route.id}") { 
				  img( 'style':"border:0;", 'src':'/images/Trash.png',  'width':"16", 'height':"16")
			  }
			  
		  }
		  td { a('href':"edit?id=${route.id}") {yield(route.name)} }   
		  td   route.startFormatted
        }
	}
}