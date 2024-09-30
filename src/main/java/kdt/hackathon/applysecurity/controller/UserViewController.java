package kdt.hackathon.applysecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/service")
    public String service() {
        return "service";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";}
}