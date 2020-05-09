package com.artemfo.springboottask.controller;

import com.artemfo.springboottask.model.Role;
import com.artemfo.springboottask.model.User;
import com.artemfo.springboottask.service.AdminService;
import com.artemfo.springboottask.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/profiles")
@PreAuthorize("hasAuthority('ADMIN')")
@Slf4j
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    public AdminController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping
    public String getProfiles(Model model, User user) {
        model.addAttribute("users", adminService.getProfiles());
        model.addAttribute("roles", Role.values());
        return "profiles";
    }

    @GetMapping("/{id}")
    public String userProfile(@PathVariable User id, Model model) {
        model.addAttribute("user", id);
        model.addAttribute("roles", Role.values());
        return "profileAdmin";
    }

    @PostMapping("/{id}")
    public String update(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", Role.values());
            return "profileAdmin";
        }
        if (user.getId() <= 2) {
            model.addAttribute("notEditable", "");
            model.addAttribute("roles", Role.values());
            return "profileAdmin";
        }
        String error = adminService.update(user);
        if (error != null) {
            model.addAttribute(error, "");
            return "profileAdmin";
        }
        return "redirect:/profiles";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        if (id <= 2) {
            model.addAttribute("notEditable", "");
            model.addAttribute("roles", Role.values());
            return "profiles";
        }
        adminService.delete(id);
        return "redirect:/profiles";
    }

    @PostMapping
    public String addUser(@Valid User user, BindingResult result, Model model, HttpServletRequest request, @AuthenticationPrincipal User authUser) {
        if (result.hasErrors()) {
            model.addAttribute("users", adminService.getProfiles());
            model.addAttribute("roles", Role.values());
            return "profiles";
        }
        List<String> errors = userService.addUser(user, request);
        if (errors != null) {
            errors.forEach(error -> model.addAttribute(error, ""));
            model.addAttribute("users", adminService.getProfiles());
            model.addAttribute("roles", Role.values());
            return "profiles";
        }
        return "redirect:/profiles";
    }
}
