package web.bookie.config;

import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@TestConfiguration
public class GlobalMockMvcConfig {
    @Bean
    public MockMvcBuilderCustomizer customToPrintMockMvc() {
        return builder -> builder.alwaysDo(print());
    }
}
