package com.artemfo.springboottask.service;

import com.artemfo.springboottask.UserRepository;
import com.artemfo.springboottask.model.Role;
import com.artemfo.springboottask.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username);
    }

    public boolean addUser(User user, HttpServletRequest request) {
        User userFromDb = userRepository.findByName(user.getName());
        log.info("findByName " + userFromDb);
        if (userFromDb != null) {
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setIp(getIPForRegistered(request));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.info("addUser " + user);
        return true;
    }

    public User update(User updUser, User authUser) {
        authUser.setEmail(updUser.getEmail().toLowerCase());
        authUser.setPassword(passwordEncoder.encode(updUser.getPassword()));
        authUser.setTimezone(updUser.getTimezone());
        userRepository.save(authUser);
        log.info("save " + updUser);
        return authUser;
    }

    public static String getIPForRegistered(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}
