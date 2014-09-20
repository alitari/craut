yieldUnescaped '<!DOCTYPE html>'                                                    
html(lang:'en') {                                                                   
    include template: 'head.tpl' 
    body {
		div('id':'wrapper') { 
			include template: 'header.tpl'
			div( 'id':'page_content') {
				include template: 'leftSideBar.tpl'
				include template: 'rightSection.tpl'
				div('class':'clear')
				include template: 'footer.tpl'
			}
		}
	}
}
