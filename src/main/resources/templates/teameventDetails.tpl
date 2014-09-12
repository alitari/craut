div( 'class':'right_section') {
	div( 'class':'common_content') {
		h2() { yield('Event Detail')}
		hr()
		def action = model.event ? "edited":"created"
		def name = model.event ? model.event.name : ""
		form( 'action':action, 'method':"get") {
			yield('Name:') 
			input( 'type':"text", 'name':"name", 'value':name)
			if ( model.event ) {
				input( 'type':"hidden", 'name':"id", 'value':model.event.id)
			}
			br()
			input('type':'submit', 'value':" Submit " ,'class':'btn')
			a('href':'init','class':'btn'){yield('Cancel')}
		}
	}
	
	
}