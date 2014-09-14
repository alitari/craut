package de.craut.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface RouteRepository extends CrudRepository<Route, Long> {

	List<Route> findByName(String name);

}
