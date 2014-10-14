package de.craut.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ActivityRepository extends CrudRepository<Activity, Long> {

	List<Activity> findByRoute(String name);

	List<Activity> findByUser(User user);

}
