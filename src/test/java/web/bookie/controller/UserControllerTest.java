package web.bookie.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import web.bookie.config.GlobalMockMvcConfig;
import web.bookie.domain.UserEntity;
import web.bookie.dto.request.UserRequestDTO;
import web.bookie.exceptions.errors.AuthError;
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
@Import(GlobalMockMvcConfig.class)
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private EntityManager em;

    static private String testId;
    static private String testPwd;
    static private UserRequestDTO userRequestDTO;

    @BeforeEach
    void before() {
        testId = "testId";
        testPwd = "testPwd";

        userRequestDTO = UserRequestDTO.getInstance(testId, testPwd);
    }

    /**
     * 유저 등록 요청을 검증하는 테스트.
     *
     * 프로세스:
     * 1. GIVEN: /auth/register로 POST 요청 전송.
     * 2. WHEN: 유효한 사용자 데이터를 등록 요청.
     * 3. THEN: 응답 상태 코드가 200이고, 응답의 userTsid가 DB의 userTsid와 일치.
     *
     * 검증 항목:
     * - HTTP 상태 코드 (200 OK)
     * - 응답 데이터의 userTsid와 DB 값 비교
     * - DB에 유효한 사용자 데이터 저장 여부
     */
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

    }

    /**
     * 유효한 사용자 데이터를 검증하는 테스트.
     *
     * 프로세스:
     * 1. GIVEN: 사용자 데이터를 DB에 저장
     * 2. WHEN: /auth/validate로 POST 요청
     * 3. THEN: 응답 상태 코드가 200이고, 응답의 userTsid가 DB의 userTsid와 일치
     *
     * 검증 항목:
     * - HTTP 상태 코드 (200 OK)
     * - 응답 데이터의 userTsid와 DB 값 비교
     */
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

    /**
     * 유효하지 않은 사용자 데이터를 검증할 때 발생하는 예외를 테스트.
     *
     * 프로세스:
     * 1. GIVEN: /auth/validate로 POST 요청 전송.
     * 2. WHEN: 잘못된 사용자 데이터로 검증 요청.
     * 3. THEN:
     *    - 응답 상태 코드가 4xx 에러여야 함.
     *    - 응답 데이터에서 적절한 에러 타입, 에러 이름, 메시지, 코드 확인.
     *
     * 검증 항목:
     * - HTTP 상태 코드 (4xx Client Error)
     * - 응답 데이터의 errorType, errorName, errorMessage, errorCode가 올바른지 확인.
     */
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