table ('border':'1') {
	tr {
	  th "id"
	  th "Route name"
	  th "start Date"
//	  th "Map"
	  
	}
	for (activity in model.activities) {
		tr {
		  td { yield(activity.id)  }
		  td { yield(activity.name)}    
		  td { yield(activity.start)}
        }
	}
}