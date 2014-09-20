div( 'class':'right_section') {
	div( 'class':'common_content') {
		if ( model.routes) {
			h2() { yield('Routes')}
			hr()
			include template: 'routeList.tpl'
		} else {
			h2() { yield('Activities')}
			hr()
			include template: 'activityList.tpl'
		}
	}
	
	div( 'class':'top_content') {
		div( 'class':'column_one') {
			h2() { yield('Upload Activity') }
			form('method':'post', 'enctype':'multipart/form-data','action':'/routes/upload' ) {
				yield('Datei:') input( 'type':'file', 'name':'file')  br()    
				input('type':'submit', 'value':'upload') 
				if ( !model.uploadMessage) {
				   yield('Press here to upload the file!')
				} else {
					yield(model.uploadMessage)
				}
				
			}
		}
			
	}
	
	div( 'class':'column_two border_left') {
		h2() { yield('Other Services')}
		p() { yield("${model.otherServicesContent}") }
		br()
		p() {
			a('href':'#','class':'btn'){yield('Read more')}
		}
	}
}
