package vn.hoidanit.laptopshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.laptopshop.service.UserService;

@Controller
public class UserController {

    // DI: Dependency Injection
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public String getHomePage(Model model) {
        String test = this.userService.handelHello();
        model.addAttribute("adam", test);
        model.addAttribute("toilamdev", "From userController");
        return "hello";
    }

    @RequestMapping("/admin/user")
    public String getCreateUserPage(Model model) {
        return "admin/user/create";
    }
}

// @RestController
// public class UserController {

// // DI: Dependency Injection
// private UserService userService;

// public UserController(UserService userService) {
// this.userService = userService;
// }

// @GetMapping("/")
// public String getHomePage() {
// return this.userService.handelHello();
// }
// }
