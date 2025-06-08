package com.myfitmate.myfitmate.domain.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";  // src/main/resources/templates/login.html
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";  // src/main/resources/templates/signup.html
    }
}
