package de.craut.domain;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface FileUploadRepository extends CrudRepository<FileUpload, Long> {

	List<FileUpload> findByInsertDateGreaterThan(Date fromDate);

}
