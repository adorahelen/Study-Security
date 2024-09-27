package kdt.hackathon.applysecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {
    @GetMapping("/login")
    public String login() {
        return "login";
    } // 이메일 부분을 전화번호를 입력하라고만 수정하면 될듯



    @GetMapping("/signup")
    public String signup() {
        return "signup";
    } // 이메일 , 패스워드 만 나오는데 4개를 다 입력받게 수정해야함

    @GetMapping("/service")
    public String service() {
        return "service";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";}
}