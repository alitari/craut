div( 'class':'right_section') {
	div( 'class':'common_content') {
		h2() { yield('Route Detail')}
		hr()
		def action = model.route ? "edited":"created"
		def name = model.route ? model.route.name : ""
		form( 'action':action, 'method':"get") {
			yield('Name:') 
			input( 'type':"text", 'name':"name", 'value':name)
			if ( model.route ) {
				input( 'type':"hidden", 'name':"id", 'value':model.route.id)
			}
			br()
			input('type':'submit', 'value':" Submit " ,'class':'btn')
			a('href':'init','class':'btn'){yield('Cancel')}
		}
	}
	
    div('class':'map','id':"map-canvas") { }
	
}