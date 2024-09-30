package kdt.hackathon.applysecurity.controller;

import kdt.hackathon.applysecurity.controller.dto.WebSingUpRequest;
import kdt.hackathon.applysecurity.service.UserSingUpService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@RequiredArgsConstructor
@Controller
public class UserSingUpController {
    private static final Logger log = LoggerFactory.getLogger(UserSingUpController.class);
    private final UserSingUpService userSingUpService;

    @PostMapping("/api/user")
    public String userSingUp(@ModelAttribute WebSingUpRequest webSingUpRequest) {
        log.info(webSingUpRequest.toString());
        userSingUpService.save(webSingUpRequest);
        return "redirect:/login";
    }
} // 폼 태그로 보내기 때문에, @RequestBody 가 아닌 @ModelAttribute
