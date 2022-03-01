package com.app.springapp.controller;

import java.util.stream.Collectors;

import javax.validation.Valid;

import com.app.springapp.dto.ChangePasswordForm;
import com.app.springapp.entity.User;
import com.app.springapp.repository.RoleRepository;
import com.app.springapp.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {

    @Autowired
    UserService serUser;

    @Autowired
    RoleRepository repRole;

    @GetMapping({ "/", "/login" })
    public String index() {
        return "index";
    }

    @GetMapping("/userForm")
    public String getUserForm(Model model) {
        model.addAttribute("userForm", new User());
        model.addAttribute("userList", serUser.getAllUsers());
        model.addAttribute("roles", repRole.findAll());
        model.addAttribute("listTab", "active");
        return "user-form/user-view";
    }

    @PostMapping("/userForm")
    public String createUser(@Valid @ModelAttribute("userForm") User user, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("userForm", user);
            model.addAttribute("formTab", "active");
        } else {
            try {
                serUser.createUser(user);
                model.addAttribute("userForm", new User());
                model.addAttribute("listTab", "active");
            } catch (Exception e) {
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("userForm", user);
                model.addAttribute("formTab", "active");
                model.addAttribute("userList", serUser.getAllUsers());
                model.addAttribute("roles", repRole.findAll());
            }
        }

        model.addAttribute("userList", serUser.getAllUsers());
        model.addAttribute("roles", repRole.findAll());
        return "user-form/user-view";
    }

    @GetMapping("/editUser/{id}")
    public String getEditUserForm(Model model, @PathVariable(name = "id") Long id) throws Exception {
        User userToEdit = serUser.getUserById(id);

        model.addAttribute("userForm", userToEdit);
        model.addAttribute("userList", serUser.getAllUsers());
        model.addAttribute("roles", repRole.findAll());
        model.addAttribute("formTab", "active");
        model.addAttribute("editMode", true);
        model.addAttribute("passwordForm", new ChangePasswordForm(userToEdit.getId()));
        return "user-form/user-view";
    }

    @PostMapping("/editUser")
    public String postEditUserForm(@Valid @ModelAttribute("userForm") User user, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("userForm", user);
            model.addAttribute("formTab", "active");
            model.addAttribute("editMode", true);
            model.addAttribute("passwordForm", new ChangePasswordForm(user.getId()));
        } else {
            try {
                serUser.updateUser(user);
                model.addAttribute("userForm", new User());
                model.addAttribute("listTab", "active");
            } catch (DataIntegrityViolationException e) {
                model.addAttribute("formErrorMessage", "Nombre de usuario no disponible");
                model.addAttribute("userForm", user);
                model.addAttribute("formTab", "active");
                model.addAttribute("userList", serUser.getAllUsers());
                model.addAttribute("roles", repRole.findAll());
                model.addAttribute("editMode", true);
                model.addAttribute("passwordForm", new ChangePasswordForm(user.getId()));
            } catch (Exception e) {
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("userForm", user);
                model.addAttribute("formTab", "active");
                model.addAttribute("userList", serUser.getAllUsers());
                model.addAttribute("roles", repRole.findAll());
                model.addAttribute("editMode", true);
                model.addAttribute("passwordForm", new ChangePasswordForm(user.getId()));
            }
        }

        model.addAttribute("userList", serUser.getAllUsers());
        model.addAttribute("roles", repRole.findAll());
        return "user-form/user-view";
    }

    @GetMapping("/userForm/cancel")
    public String cancelEditUser(ModelMap model) {
        return "redirect:/userForm";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(Model model, @PathVariable(name = "id") Long id) {
        try {
            serUser.deleteUser(id);
        } catch (Exception e) {
            model.addAttribute("listErrorMessage", e.getMessage());
        }
        return getUserForm(model);
    }

    @PostMapping("/editUser/changePassword")
    public ResponseEntity<String> postEditUseChangePassword(@Valid @RequestBody ChangePasswordForm form, Errors errors) {
        try {
            // If error, just return a 400 bad request, along with the error message
            if (errors.hasErrors()) {
                String result = errors.getAllErrors()
                        .stream().map(x -> x.getDefaultMessage())
                        .collect(Collectors.joining(""));

                throw new Exception(result);
            }
            serUser.changePassword(form);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("success");
    }
}