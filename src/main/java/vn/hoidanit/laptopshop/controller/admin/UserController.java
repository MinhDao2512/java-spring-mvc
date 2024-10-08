package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import vn.hoidanit.laptopshop.domain.Role;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.RoleService;
import vn.hoidanit.laptopshop.service.FileService;
import vn.hoidanit.laptopshop.service.UserService;

@Controller
public class UserController {

    // DI: Dependency Injection
    private final UserService userService;
    private final FileService fileService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, FileService fileService, RoleService roleService,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.fileService = fileService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/admin/user")
    public String getUserPage(Model model) {
        List<User> users = this.userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/user/show";
    }

    @GetMapping("/admin/user/{id}")
    public String getUserDetailPage(Model model, @PathVariable long id) {
        User user = this.userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("id", id);
        return "admin/user/detail";
    }

    @GetMapping("/admin/user/create")
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @PostMapping("/admin/user/create")
    public String createUserPage(Model model,
            @ModelAttribute("newUser") @Validated(User.CreateUser.class) User toilamdev,
            BindingResult newUserBindingResult,
            @RequestParam("avatarFile") MultipartFile avatarFile) {

        // validation Start
        if (newUserBindingResult.hasFieldErrors()) {
            return "admin/user/create";
        }
        // validation End
        String hashPassword = this.passwordEncoder.encode(toilamdev.getPassword());

        toilamdev.setAvatar(this.fileService.handleSaveUploadFile(avatarFile, "avatar"));
        toilamdev.setPassword(hashPassword);
        toilamdev.setRole(roleService.getByName(toilamdev.getRole().getName()));
        this.userService.createOrUpdateUser(toilamdev);
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/update/{id}")
    public String getUserUpdatePage(Model model, @PathVariable long id) {
        User currentUser = this.userService.getUserById(id);
        model.addAttribute("newUser", currentUser);
        return "admin/user/update";
    }

    @PostMapping("/admin/user/update")
    public String postUpdateUser(Model model, @ModelAttribute("newUser") @Validated(User.UpdateUser.class) User newUser,
            BindingResult newUserBindingResult,
            @RequestParam("avatarFile") MultipartFile avatarFile) {

        User currentUser = this.userService.getUserById(newUser.getId());
        // Validation
        if (newUserBindingResult.hasFieldErrors()) {
            newUser.setAvatar(currentUser.getAvatar());
            return "admin/user/update";
        }
        // Validation
        Role currentRole = this.roleService.getByName(newUser.getRole().getName());
        if (currentUser != null) {
            currentUser.setAddress(newUser.getAddress());
            currentUser.setPhone(newUser.getPhone());
            currentUser.setFullName(newUser.getFullName());
            currentUser.setRole(currentRole);
            if (avatarFile.getOriginalFilename() != "") {
                this.fileService.handleDeleteUploadFile(currentUser.getAvatar(), "avatar");
                currentUser.setAvatar(this.fileService.handleSaveUploadFile(avatarFile, "avatar"));
            }
            this.userService.createOrUpdateUser(currentUser);
        }
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/delete/{id}")
    public String getDeleteUserPage(Model model, @PathVariable long id) {
        User currentUser = this.userService.getUserById(id);
        model.addAttribute("currentUser", currentUser);
        return "admin/user/delete";
    }

    @PostMapping("/admin/user/delete")
    public String postDeleteUser(Model model, @ModelAttribute("currentUser") User currentUser) {
        User user = this.userService.getUserById(currentUser.getId());
        fileService.handleDeleteUploadFile(user.getAvatar(), "avatar");
        this.userService.deleteUserById(currentUser.getId());
        return "redirect:/admin/user";
    }
}
