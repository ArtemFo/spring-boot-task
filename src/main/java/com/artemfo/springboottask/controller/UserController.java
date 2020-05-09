package com.artemfo.springboottask.controller;

import com.artemfo.springboottask.model.User;
import com.artemfo.springboottask.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signUp(User user) {
        return "signUp";
    }

    @PostMapping("/signup")
    public String addUser(@Valid User user, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "signUp";
        }
        if (!userService.addUser(user, request)) {
            model.addAttribute("userExists", "");
            return "signUp";
        }
        model.addAttribute("userAdded", "");
        return "redirect:/profile";
    }

    @GetMapping("/profileuser")
    public String profile(ModelMap model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        return "profileUser";
    }

    @PostMapping("/profileuser")
    public String update(@Valid User user, BindingResult result, Model model, @AuthenticationPrincipal User authUser) {
        if (result.hasErrors()) {
            return "profileUser";
        }
        if (authUser.getId() <= 2) {
            model.addAttribute("notEditable", "");
            return "profileUser";
        }
        userService.update(user, authUser);
        return "redirect:/profile";
    }
}
