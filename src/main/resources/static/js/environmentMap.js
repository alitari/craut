        var map;
        function initialize() {
          var mapOptions = {
            zoom: 8,
            center: new google.maps.LatLng(-34.397, 150.644)
          };
          var mapEle = document.getElementById('map-canvas')
          map = new google.maps.Map(mapEle,
              mapOptions);
          
        }

        google.maps.event.addDomListener(window, 'load', initialize);

