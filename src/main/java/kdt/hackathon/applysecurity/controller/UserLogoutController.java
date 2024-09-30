package kdt.hackathon.applysecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserLogoutController {

    @PostMapping("/api/logout")
    public String logout() {return "redirect:/login";}
}
// <form action="/logout" method="POST">
