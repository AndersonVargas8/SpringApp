package com.app.springapp.controller;

import javax.validation.Valid;

import com.app.springapp.entity.User;
import com.app.springapp.repository.RoleRepository;
import com.app.springapp.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String userForm(Model model) {
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
        return "user-form/user-view";
    }

    @PostMapping("/editUser")
    public String postEditUserForm(@Valid @ModelAttribute("userForm") User user, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("userForm", user);
            model.addAttribute("formTab", "active");
            model.addAttribute("editMode", true);
        } else {
            try {
                serUser.updateUser(user);
                model.addAttribute("userForm", new User());
                model.addAttribute("listTab", "active");
            }catch(DataIntegrityViolationException e){
                model.addAttribute("formErrorMessage", "Nombre de usuario no disponible");
                model.addAttribute("userForm", user);
                model.addAttribute("formTab", "active");
                model.addAttribute("userList", serUser.getAllUsers());
                model.addAttribute("roles", repRole.findAll());
                model.addAttribute("editMode", true);
            }catch (Exception e) {
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("userForm", user);
                model.addAttribute("formTab", "active");
                model.addAttribute("userList", serUser.getAllUsers());
                model.addAttribute("roles", repRole.findAll());
                model.addAttribute("editMode", true);
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
}