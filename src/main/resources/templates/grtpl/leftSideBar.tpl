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
		h1() { yield('Uploads')}
		div('class':'box') {
			table() { 
				tr {
					th() {yield('Date') }
					th() {yield('Size') }
				}
				model.uploadsFromToday.each { upload->
					tr {
						td() {yield( de.craut.util.AdvancedDateFormat.day(upload.insertDate)) }
						td() {yield(upload.size) }
					}
				}
				

			}
		}
	}
}