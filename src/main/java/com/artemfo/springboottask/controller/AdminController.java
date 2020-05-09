package com.artemfo.springboottask.controller;

import com.artemfo.springboottask.model.Role;
import com.artemfo.springboottask.model.User;
import com.artemfo.springboottask.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/profiles")
@PreAuthorize("hasAuthority('ADMIN')")
@Slf4j
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public String getProfiles(Model model) {
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
        adminService.update(user);
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
}
