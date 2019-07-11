package com.codeup.d2d.services;

import com.codeup.d2d.models.User;
import com.codeup.d2d.models.UserWithRoles;
import com.codeup.d2d.repos.UserRepository;
import com.codeup.d2d.repos.UserRoles;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("userDetailsService")
@Transactional
public class UserDetailsLoader implements UserDetailsService {
    private final UserRepository usersRepository;
    private final UserRoles roles;

    public UserDetailsLoader(UserRepository usersRepository, UserRoles roles) {
        this.usersRepository = usersRepository;
        this.roles = roles;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = usersRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user found for " + username);
        }
        List<String> userRoles = roles.ofUserWith(username);
        return new UserWithRoles(user, userRoles);
    }
}
