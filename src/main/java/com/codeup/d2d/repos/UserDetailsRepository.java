package com.codeup.d2d.repos;

import com.codeup.d2d.models.UserDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepository extends CrudRepository<UserDetails, Long> {
    UserDetails findByUserId(Long userId);
}
