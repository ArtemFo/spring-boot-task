package com.artemfo.springboottask.service;

import com.artemfo.springboottask.UserRepository;
import com.artemfo.springboottask.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(UserRepository repository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username);
    }

    public Iterable<User> getProfiles() {
        log.info("findAll");
        return userRepository.findAll();
    }

    public User update(User user) {
        User userFromDB = userRepository.findById(user.getId()).get();
        userFromDB.setRoles(user.getRoles());
        userFromDB.setEmail(user.getEmail().toLowerCase());
        userFromDB.setPassword(passwordEncoder.encode(user.getPassword()));
        userFromDB.setTimezone(user.getTimezone());
        userFromDB.setActive(user.isActive());
        userRepository.save(userFromDB);
        log.info("save " + userFromDB);
        return userFromDB;
    }

    public void delete(Long id) {
        log.info("delete " + id);
        userRepository.deleteById(id);
    }
}
