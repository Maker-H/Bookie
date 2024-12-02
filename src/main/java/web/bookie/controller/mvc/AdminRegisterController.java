//package web.bookie.controller;
//
//import jakarta.validation.Valid;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.Errors;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import web.bookie.data.BookRepository;
//import web.bookie.data.ReviewRepository;
//import web.bookie.data.UserRepository;
//import web.bookie.domain.Book;
//import web.bookie.domain.Review;
//import web.bookie.domain.User;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//
//@Slf4j
//@Controller
//@RequestMapping("/admin/register")
//public class AdminRegisterController {
//
//    private final UserRepository userRepository;
//    private final BookRepository bookRepository;
//    private final ReviewRepository reviewRepository;
//
//    @Autowired
//    public AdminRegisterController(
//            UserRepository userRepository,
//            BookRepository bookRepository,
//            ReviewRepository reviewRepository
//    ) {
//        this.userRepository = userRepository;
//        this.bookRepository = bookRepository;
//        this.reviewRepository = reviewRepository;
//    }
//
//    @ModelAttribute(name = "user")
//    public User user() {
//        return new User();
//    }
//
//    @ModelAttribute(name = "book")
//    public Book book() {
//        return new Book();
//    }
//
//    @GetMapping("/user")
//    public String showUserForm() {
//        return "userForm";
//    }
//
//    @PostMapping("/user")
//    public String userRegister(@Valid User user, Errors errors) {
//        if (errors.hasErrors()) {
//            log.error("User errors: " + errors);
//            return "userForm";
//        }
//
//        log.info("Register => " + user);
//        userRepository.save(user);
//
//        return "home";
//    }
//
//
//
//    @GetMapping("/book")
//    public String showBookForm() {
//        return "bookForm";
//    }
//
//    @PostMapping("/book")
//    public String bookRegister(@Valid Book book, Errors errors) throws IOException {
//        if (errors.hasErrors()) {
//            log.error("Book errors:" + errors);
//            return "bookForm";
//        }
//
//        log.info("Register => " + book);
//        bookRepository.save(book);
//
//        return "home";
//    }
//
//    @ModelAttribute(name = "review")
//    public Review review() {
//        return new Review();
//    }
//
//    @GetMapping("/review")
//    public String showReviewForm(Model model) {
//
//        List<User> users = userRepository.findAll();
//        List<Book> books = bookRepository.findAll();
//
//        model.addAttribute("books", books);
//        model.addAttribute("users", users);
//
//        users.stream().forEach(u -> log.debug("users: " + u));
//        return "reviewForm";
//    }
//
//    @PostMapping("/review")
//    public String reviewRegister(@Valid Review review, Errors errors) {
//        if (errors.hasErrors()) {
//            log.error("review errors:" + errors);
//            return "reviewForm";
//        }
//
//        log.info("Register => " + review);
//        reviewRepository.save(review);
//
//        return "home";
//    }
//}
