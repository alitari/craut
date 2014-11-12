package de.craut.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ActivityRepository extends CrudRepository<Activity, Long> {

	List<Activity> findByRoute(Route route);

	List<Activity> findByUser(User user);

}
