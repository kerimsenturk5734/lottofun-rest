package com.lottofun.lottofunrest.security;

import com.lottofun.lottofunrest.exception.NotFoundException;
import com.lottofun.lottofunrest.model.User;
import com.lottofun.lottofunrest.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // Find user by username.
            User user = userService.getUserByUsername(username);

            // create UserDetails and return
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .authorities("USER") // use user role if you implement role-based authorization
                    .build();
        } catch (NotFoundException e) {
            // If user couldn't find throw custom exception
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
