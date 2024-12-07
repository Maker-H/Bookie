package web.bookie.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import web.bookie.domain.UserEntity;
import web.bookie.dto.request.UserRequestDTO;
import web.bookie.error.AuthError;
import web.bookie.respository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("memtest")
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private EntityManager em;

    private String testId;
    private String testPwd;
    private UserRequestDTO userRequestDTO;

    @BeforeEach
    void before() {
        testId = "testId";
        testPwd = "testPwd";

        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setId(testId);
        userRequestDTO.setPassword(testPwd);
    }

    @TestConfiguration
    public class CustomMockMvcConfig {

        @Bean
        public MockMvcBuilderCustomizer customMockMvc() {
            return builder -> builder.alwaysDo(print());
        }
    }

    @Test
    void registerUser_ShouldReturnSuccess() throws Exception {

        MvcResult result = mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userRequestDTO))
                )
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        String savedUserTsid = rootNode.path("data").path("userTsid").asText();

        Optional<UserEntity> selectedUser = userRepository.findByIdAndPassword(testId, testPwd);
        assertTrue(selectedUser.isPresent());
        assertEquals(selectedUser.get().getTsid(), savedUserTsid);

    }

    @Test
    void validateUser_ShouldReturnSuccess() throws Exception {

        userRepository.save(userRequestDTO.toEntity());
        em.flush();
        em.clear();

        MvcResult result = mockMvc.perform(
                        post("/auth/validate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userRequestDTO))
                )
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        String savedUserTsid = rootNode.path("data").path("userTsid").asText();

        Optional<UserEntity> selectedUser = userRepository.findByIdAndPassword(testId, testPwd);
        assertTrue(selectedUser.isPresent());
        assertEquals(selectedUser.get().getTsid(), savedUserTsid);

    }

    @Test
    void validateUser_ShouldReturnAuthErrorExcpetion() throws Exception {

        MvcResult result = mockMvc.perform(
                        post("/auth/validate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userRequestDTO))
                )
                .andExpect(status().is4xxClientError())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        String errorType = rootNode.path("errorType").asText();
        String errorName = rootNode.path("errorName").asText();
        String errorMessage = rootNode.path("errorMessage").asText();
        int errorCode = rootNode.path("errorCode").asInt();

        assertEquals(AuthError.class.getSimpleName(), errorType);
        assertEquals(AuthError.USER_NOT_VALID.name(), errorName);
        assertEquals(AuthError.USER_NOT_VALID.getErrorMsg(), errorMessage);
        assertEquals(AuthError.USER_NOT_VALID.getErrorCode(), errorCode);
    }

}