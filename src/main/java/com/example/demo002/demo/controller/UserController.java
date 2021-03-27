package com.example.demo002.demo.controller;

import com.example.demo002.demo.entity.User;
import com.example.demo002.demo.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users/")
public class UserController {
    private final UserRepository userRepository;

    //DI call model step 2
    @Autowired
    public UserController (UserRepository studentRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("list")
    public String showUpdateForm (Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "index";
    }

    @PostMapping("add")
    public String addUser (@Valid User user, BindingResult result, Model model) {
        if ( result.hasErrors() ) {
            return "add-user";
        }
        userRepository.save(user);
        return "redirect:list";
    }

    @GetMapping("edit/{id}")
    public String showUpdate (@PathVariable("id") Integer id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID :" + id));
        model.addAttribute("user", user);
        return "update-user";

    }

    @PostMapping("update/{id}")
    public String updateUser (@PathVariable("id") Integer id, @Valid User user, BindingResult result,
                              Model model) {
        if ( result.hasErrors() ) {
            user.setId(id);
            return "update-user";
        }

        userRepository.save(user);
        model.addAttribute("users", userRepository.findAll());
        return "index";
    }

    @GetMapping("delete/{id}")
    public String deleteUser (@PathVariable("id") Integer id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);
        model.addAttribute("users", userRepository.findAll());
        return "index";
    }

}
