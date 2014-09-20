package de.craut.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ActivityPointRepository extends CrudRepository<ActivityPoint, Long> {

	List<ActivityPoint> findByActivity(Activity route);

}
