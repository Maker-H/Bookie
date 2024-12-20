package web.bookie.exceptions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import web.bookie.config.GlobalMockMvcConfig;
import web.bookie.dto.request.UserRequestDTO;
import web.bookie.dto.response.UserResponseDTO;
import web.bookie.service.UserService;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("memtest")
@Import(GlobalMockMvcConfig.class)
class GlobalExceptionHandlerTest {

    @MockBean private UserService<UserRequestDTO, UserResponseDTO> userService;
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    private String testId;
    private String testPwd;
    private UserRequestDTO userRequestDTO;

    @BeforeEach
    void before() {
        testId = "testId";
        testPwd = "testPwd";

        userRequestDTO = UserRequestDTO.getInstance(testId, testPwd);
    }

    @Test
    void handleException_ShouldReturnErrorResponse() throws Exception {
        Mockito.when(userService.validateUser(userRequestDTO, new MockHttpServletRequest()))
                .thenThrow(new RuntimeException("Database error"));

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

        assertEquals(RuntimeException.class.getSuperclass().getSimpleName(), errorType);
        assertEquals(RuntimeException.class.getSimpleName(), errorName);
        assertEquals("Database error", errorMessage);
    }

}