div('id':'header') { 
	div('class':'top_banner') {
		h1() { yield('The Super great Team') }
		p(){ yield("Hello ${model.name}")}
	}

	div( 'class':'navigation') {
		ul() {
			model.menuPage.each {nav->
				li() { a('href':"${nav.href}"){ yield( "${nav.text}") } }
			}
		}
	}
}