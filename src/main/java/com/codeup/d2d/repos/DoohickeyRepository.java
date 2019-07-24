package com.codeup.d2d.repos;

import com.codeup.d2d.models.Doohickey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface DoohickeyRepository extends CrudRepository<Doohickey, Long> {
    List<Doohickey> findByTitle(String findTitle);
    List<Doohickey> findByDescription(String findDescription);
    Page<Doohickey> findByTitleIsLikeOrDescriptionIsLike(String term,String term2,Pageable pageable);
    Page<Doohickey> findAll(Pageable pageable);
}
