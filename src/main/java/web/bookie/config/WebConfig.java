package web.bookie.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/admin/register/user").setViewName("/userForm");
        registry.addViewController("/admin/register/book").setViewName("/bookForm");
        registry.addViewController("/admin/register/review").setViewName("reviewForm");
    }

}
