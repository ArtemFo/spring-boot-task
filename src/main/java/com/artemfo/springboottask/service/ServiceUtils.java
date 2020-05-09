package com.artemfo.springboottask.service;

import com.artemfo.springboottask.UserRepository;
import com.artemfo.springboottask.model.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceUtils {

    private final UserRepository userRepository;

    public ServiceUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected static String getIPForRegistered(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    protected List<String> validateUniqueFields(User user) {
        List<String> errors = new ArrayList<>();
        if (!validateNameUnique(user.getName())) {
            errors.add("userExists");
        }
        if (!validateEmailUnique(user.getEmail())) {
            errors.add("emailExists");
        }
        return errors;
    }

    private boolean validateNameUnique(String name) {
        return userRepository.findByName(name) == null;
    }

    private boolean validateEmailUnique(String email) {
        return userRepository.findByEmail(email.toLowerCase()) == null;
    }

    protected boolean validateEmailForUpdate(User newUser, User currentUser) {
        return validateEmailUnique(newUser.getEmail()) || newUser.getEmail().equalsIgnoreCase(currentUser.getEmail());
    }
}
