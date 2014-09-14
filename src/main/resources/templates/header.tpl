div('id':'header') { 
	div('class':'top_banner') {
		h1() { yield('Route Board') }
		p(){ yield("District:")}
	}

	div( 'class':'navigation') {
		ul() {
			model.menuPage.each {nav->
				li() { a('href':"${nav.href}"){ yield( "${nav.text}") } }
			}
		}
	}
}