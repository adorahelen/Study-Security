package kdt.hackathon.applysecurity.controller;

import kdt.hackathon.applysecurity.controller.dto.WebSingUpRequest;
import kdt.hackathon.applysecurity.service.UserSingUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserSingUpController {
    private final UserSingUpService userSingUpService;

    @PostMapping("/user")
    public String userSingUp(@RequestBody WebSingUpRequest webSingUpRequest) {
        userSingUpService.save(webSingUpRequest);
        return "redirect:/login";
    }
}
