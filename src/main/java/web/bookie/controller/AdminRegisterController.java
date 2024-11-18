package web.bookie.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.bookie.domain.Book;
import web.bookie.domain.User;

@Slf4j
@Controller
@RequestMapping("/admin/register")
public class AdminRegisterController {

    @GetMapping("/user")
    public String showUserForm(Model model) {
        model.addAttribute("user", new User());
        return "userForm";
    }

    @PostMapping("/user")
    public String userRegister(@Valid User user, Errors errors) {
        if (errors.hasErrors()) {
            log.error("User errors: " + errors);
            return "userForm";
        }

        log.info("Register => " + user);
        return "home";
    }

    @GetMapping("/book")
    public String showBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "bookForm";
    }

    @PostMapping("/book")
    public String bookRegister(@Valid Book book, Errors errors) {
        if (errors.hasErrors()) {
            log.error("Book errors:" + errors);
            return "bookForm";
        }

        log.info("Register => " + book);
        return "home";
    }
}
