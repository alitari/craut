head {                                                                          
        meta('charset':'UTF-8')      
        title('My eepage')                                                            
        link ('href':"/css/style.css", 'rel':"stylesheet", 'type':"text/css")

        script('src':"https://maps.googleapis.com/maps/api/js?v=3.exp"){}
        script('src':'/js/routeDetailsMap.js'){}
        script() {
        	yieldUnescaped ' var flightPlanCoordinates = ['
        	for (routePoint in model.routePoints) { 
        		if ( routePoint != model.routePoints.get(0)) {
        			yieldUnescaped ","
        		}

        		yieldUnescaped "new google.maps.LatLng( ${routePoint.latitude},${routePoint.longitude})"
        	}
        	yieldUnescaped ' ];'
        	yieldUnescaped "google.maps.event.addDomListener(window, 'load', initialize)";
        }

}
        
        