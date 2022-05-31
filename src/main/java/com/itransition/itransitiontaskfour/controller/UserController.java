package com.itransition.itransitiontaskfour.controller;


import com.itransition.itransitiontaskfour.entity.User;
import com.itransition.itransitiontaskfour.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;


    @GetMapping("/")
    public String getAllUsers(Model model){
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("users",allUsers);
        return "home";
    }

    @GetMapping("/register")
    public String registerPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String saveUser(User user, Model ra){

        Map<String,String> messages = userService.save(user);
        Map.Entry<String,String> entry = messages.entrySet().iterator().next();
        String key = entry.getKey();
        String value = entry.getValue();

        ra.addAttribute("status", key);
        ra.addAttribute("message", value);
        return "registration";
    }

    @PostMapping("/{id}")
    public String deleteUser(@PathVariable Long id,RedirectAttributes ra){
        Map<String,String> messages = userService.delete(id);
        return getMapData(ra, messages);
    }

    private String getMapData(RedirectAttributes ra, Map<String, String> messages) {
        Map.Entry<String,String> entry = messages.entrySet().iterator().next();
        String key = entry.getKey();
        String value = entry.getValue();

        ra.addFlashAttribute("status", key);
        ra.addFlashAttribute("message", value);
        return "redirect:/";
    }

    @GetMapping("/block/{id}")
    public String blockUser(@PathVariable Long id, RedirectAttributes ra, HttpServletRequest request){
        Map<String, String> messages = userService.blockUser(id,request);

        return getMapData(ra, messages);
    }

    @GetMapping("/unblock/{id}")
    public String unBlockUser(@PathVariable Long id,RedirectAttributes ra){
        Map<String, String> messages = userService.unBlockUser(id);
        return getMapData(ra, messages);
    }

}
