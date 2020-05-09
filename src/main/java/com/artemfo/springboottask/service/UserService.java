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
import java.util.List;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServiceUtils serviceUtils;

    public UserService(UserRepository repository, @Lazy PasswordEncoder passwordEncoder, ServiceUtils serviceUtils) {
        this.userRepository = repository;
        this.passwordEncoder = passwordEncoder;
        this.serviceUtils = serviceUtils;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username);
    }

    public List<String> addUser(User user, HttpServletRequest request) {
        List<String> errors = serviceUtils.validateUniqueFields(user);
        if (errors.isEmpty()) {
            user.setEmail(user.getEmail().toLowerCase());
            user.setActive(true);
            user.setRoles(Collections.singleton(Role.USER));
            user.setIp(ServiceUtils.getIPForRegistered(request));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            log.info("addUser " + user);
            return null;
        }
        return errors;
    }

    public String update(User updUser, User authUser) {
        if (serviceUtils.validateEmailForUpdate(updUser, authUser)) {
            authUser.setEmail(updUser.getEmail().toLowerCase());
            authUser.setPassword(passwordEncoder.encode(updUser.getPassword()));
            authUser.setTimezone(updUser.getTimezone());
            userRepository.save(authUser);
            log.info("save " + updUser);
            return null;
        }
        return "emailExists";
    }

}
