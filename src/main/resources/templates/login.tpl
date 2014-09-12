yieldUnescaped '<!DOCTYPE html>'                                                    
html(lang:'en') {                                                                   
    head {                                                                          
        meta('http-equiv':'"Content-Type" content="text/html; charset=utf-8"')      
        title('My page')                                                            
    }                                                                               
    body {                                                                          
        p('Hello!')
		form('action':'greeting') {
			p() {
				yield 'Input your name:' 
				input('name':'name', 'type':'"text"', 'size':'"30"', 'maxlength':'"30"') 
			}
        }
    }                                                                               
}        