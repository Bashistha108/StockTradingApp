package com.springapp.springapp.controller;

import com.springapp.springapp.entity.User;
import com.springapp.springapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;

@Controller
public class HomeController {

    private final UserService userService;

    @Autowired
    public HomeController(UserService userService){
        this.userService = userService;
    }


    @GetMapping("/")
    public String home(Principal principal, Model model){
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        if(email == null){
            System.out.println("User not found ..........."+email);
        }
        model.addAttribute("user", user);
        return "home";
    }




}
