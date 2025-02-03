package com.springapp.springapp.controller;

import com.springapp.springapp.entity.User;
import com.springapp.springapp.entity.UserType;
import com.springapp.springapp.service.UserService;
import com.springapp.springapp.service.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {
    private final UserService userService;
    private final UserTypeService userTypeService;

    @Autowired
    public UserController(UserService userService, UserTypeService userTypeService){
        this.userService = userService;
        this.userTypeService = userTypeService;
    }

    @GetMapping("/admin-home")
    public String adminHome(){
        return "user/admin-home";
    }

    @GetMapping("/manage-users")
    public String getAllUsers(Model model){
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user/admin-users-manage";
    }

    @GetMapping("/trader-home")
    public String traderHome(){
        return "home";
    }

    @PostMapping("/save-user")
    public String saveUser(@ModelAttribute("userForm") UserForm userForm, Model model) {
        User user = userForm.getUser();
        int userTypeId = userForm.getUserType().getUserTypeId();
        UserType userType = userTypeService.getUserTypeById(userTypeId);
        user.setUserType(userType);
        userService.saveUser(user);
        return "redirect:/manage-users";
    }

    @GetMapping("/add-user-form")
    public String showFormForAddUpdate(Model model){
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        return "user/admin-users-add-update";
    }

    @GetMapping("/update-user-form")
    public String showFormForUpdate(Model model, @RequestParam("userId") int id){
        User user = userService.getUserById(id);
        UserType userType = user.getUserType();
        UserForm userForm = new UserForm();
        userForm.setUser(user);
        userForm.setUserType(userType);
        model.addAttribute("userForm", userForm);
        return "user/admin-users-add-update";
    }

    @GetMapping(value = "/delete-user/{id}")
    public String deleteUser(@PathVariable("id") int id){
        System.out.println("Deleting user ....... ....... .......");
        userService.deleteUser(id);
        System.out.println("User deleted with id: "+id);
        return "redirect:/manage-users";
    }


    public class UserForm{
        private User user;
        private UserType userType;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public UserType getUserType() {
            return userType;
        }

        public void setUserType(UserType userType) {
            this.userType = userType;
        }
    }

}
