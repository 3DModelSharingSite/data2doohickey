package com.codeup.d2d.repos;

import com.codeup.d2d.models.File;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends CrudRepository<File, Long> {
    File findByDoohickey_Id(Long Id);
}
