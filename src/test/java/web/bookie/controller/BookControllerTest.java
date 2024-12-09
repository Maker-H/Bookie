package web.bookie.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import web.bookie.config.GlobalMockMvcConfig;
import web.bookie.domain.BookEntity;
import web.bookie.dto.request.BookRequestDTO;
import web.bookie.dto.request.BookRequestDTO.BookRequestDTOBuilder;
import web.bookie.respository.BookRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("memtest")
@Import(GlobalMockMvcConfig.class)
class BookControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired BookRepository bookRepository;

    static private String title;
    static private String author;
    static private BookRequestDTOBuilder bookRequestDTOBuilder;

    @BeforeAll
    static void before() {
        title = "책!!";
        author = "나??";
        bookRequestDTOBuilder = BookRequestDTO.builder(title, author);
    }

    @Test
    void registerBook_ShouldSuccess() throws Exception {
        BookRequestDTO bookRequestDTO = bookRequestDTOBuilder.build();

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/book/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(bookRequestDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        String savedBookTsid = rootNode.path("data").path("bookTsid").asText();

        Optional<BookEntity> selectedBook = bookRepository.findById(savedBookTsid);
        assertTrue(selectedBook.isPresent());


    }

    @Test
    void register_RequestDTO_PublishDateParsingTest_ShouldSuccess() {

    }

    @Test
    void register_RequestDTO_PublishDateParsingTest_ThrowError() {

    }

    @Test
    void register_RequestDTO_Description_LOBTest() {

    }
}