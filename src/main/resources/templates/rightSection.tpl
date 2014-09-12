div( 'class':'right_section') {
	div( 'class':'common_content') {
		h2() { yield('Events')}
		hr()
		include template: 'eventList.tpl'
	}
	
	div( 'class':'top_content') {
		div( 'class':'column_one') {
			h2() { yield('Subscription')}
			p() { yield("${model.subscriptionContent}") }
			br()
			p() {
				a('href':'#','class':'btn'){yield('Read more')}
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
}