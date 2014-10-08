package de.craut.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface RoutePointRepository extends CrudRepository<RoutePoint, Long> {

	List<RoutePoint> findByRouteId(long routeId);

}
