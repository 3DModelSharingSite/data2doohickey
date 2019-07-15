package com.codeup.d2d.repos;

import com.codeup.d2d.models.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {
    Tag findByName(String name);
}
