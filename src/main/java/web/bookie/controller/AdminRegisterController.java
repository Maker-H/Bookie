package web.bookie.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/admin/register")
public class AdminRegisterController {

    @PostMapping("/user")
    public String userRegister(@Valid User user, Errors errors) {
        if (errors.hasErrors()) {
            log.error("User errors: " + errors);
            return "userForm";
        }

        log.info("userRegister: " + user);
        return "home";
    }
}
