package de.craut.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {

	List<Route> findByName(String name);

	// @Query("select r from Route where route.startLatitude < ( :#{#latitude}+ :#{#distance}) and route.startLongitude < ( :#{#longitude} + :#{#distance}) and route.startLatitude >( :#{#latitude} -:#{#distance}) and (route.startLongitude > ( :#{#longitude}-:#{#distance})")
	// List<Route> findInArea(@Param("latitude") double latitude,
	// @Param("longitude") double longitude, @Param("distance") double
	// distance);

	// @Query("select r from Route r where r.startLatitude <  ?#{[0]}")
	// List<Route> findInArea(double latitude);

	List<Route> findByStartLatitudeLessThanAndStartLongitudeLessThanAndStartLatitudeGreaterThanAndStartLongitudeGreaterThan(double upperBoundLatitude,
	        double upperBoundLongitude, double lowerBoundLatitude, double lowerBoundLongitude);

}
