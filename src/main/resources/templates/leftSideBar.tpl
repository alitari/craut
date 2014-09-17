div( 'class':'left_side_bar') {
	div('class':'col_1') {
		h1() { yield('Main Menu')}
		div('class':'box') {
			ul() {
				model.menuMain.each {nav->
					li() { a('href':"${nav.href}"){ yield( "${nav.text}") } }
				}
			}
		}
	}

	div('class':'col_1') {
		h1() { yield('Environment')}
		div('class':'box') {
//			div('class':'map','id':"map-canvas") { }
		}
	}
}