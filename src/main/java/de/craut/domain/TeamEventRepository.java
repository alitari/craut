package de.craut.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface TeamEventRepository extends CrudRepository<TeamEvent, Long> {

	List<TeamEvent> findByName(String name);

}
