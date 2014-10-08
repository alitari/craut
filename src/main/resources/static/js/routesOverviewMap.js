function initialize() {
	var mapOptions = {
		zoom : 8,
		center : new google.maps.LatLng(routeCoordinates[0][1],
				routeCoordinates[0][2])

	};

	var map = new google.maps.Map(document.getElementById('map-canvas'),
			mapOptions);

	setMarkers(map, routeCoordinates);

}

// The five markers show a secret message when clicked
// but that message is not within the marker's instance data
function attachSecretMessage(marker, num) {
	var message = routeCoordinates;
	var infowindow = new google.maps.InfoWindow({
		content :' <article>'
			     + '<h4> <a href=\'edit?id='+message[num][6]+'\'>' +message[num][0]+'</a></h4>'
			     +'<fieldset>'
			     +'<fieldset>'
			     +'</fieldset>'
			     +'</article>',
		minWidth: 400
	});

	google.maps.event.addListener(marker, 'click', function() {
		infowindow.open(marker.get('map'), marker);
	});
}

function setMarkers(map, locations) {
	// Add markers to the map

	// Marker sizes are expressed as a Size of X,Y
	// where the origin of the image (0,0) is located
	// in the top left of the image.

	// Origins, anchor positions and coordinates of the marker
	// increase in the X direction to the right and in
	// the Y direction down.
	var image = {
		url : '/images/beachflag_with_shadow.png',
		// This marker is 20 pixels wide by 32 pixels tall.
		size : new google.maps.Size(20, 32),
		// The origin for this image is 0,0.
		origin : new google.maps.Point(0, 0),
		// The anchor for this image is the base of the flagpole at 0,32.
		anchor : new google.maps.Point(0, 32)
	};
	// Shapes define the clickable region of the icon.
	// The type defines an HTML &lt;area&gt; element 'poly' which
	// traces out a polygon as a series of X,Y points. The final
	// coordinate closes the poly by connecting to the first
	// coordinate.
	var shape = {
		coords : [ 1, 1, 1, 20, 18, 20, 18, 1 ],
		type : 'poly'
	};
	for (var i = 0; i < locations.length; i++) {
		var beach = locations[i];
		var myLatLng = new google.maps.LatLng(beach[1], beach[2]);
		var marker = new google.maps.Marker({
			position : myLatLng,
			map : map,
			icon : image,
			shape : shape,
			title : beach[0],
			zIndex : beach[3]
		});
		attachSecretMessage(marker, i);
	}
}