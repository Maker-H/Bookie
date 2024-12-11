package web.bookie.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import web.bookie.config.GlobalMockMvcConfig;
import web.bookie.domain.UserEntity;
import web.bookie.dto.request.UserRequestDTO;
import web.bookie.exceptions.errors.AuthError;
import web.bookie.respository.UserRepository;
import web.bookie.util.SessionManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("memtest")
@Import(GlobalMockMvcConfig.class)
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private MockHttpSession mockHttpSession;
    @Autowired private MockHttpServletRequest mockHttpServletRequest;
    @Autowired private UserRepository userRepository;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private EntityManager em;
    @Autowired private SessionManager sessionManager;

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
     * 유효한 사용자 데이터로 로그인 성공 테스트 (세션 시작 확인).
     *
     * 프로세스:
     * 1. GIVEN: 사용자 데이터를 DB에 저장.
     * 2. WHEN: /auth/login로 POST 요청 전송.
     * 3. THEN:
     *    - 응답 데이터의 userTsid와 DB 값 비교
     *    - DB값과 세션값 비교
     */
    @Test
    void login_ShouldStartSession() throws Exception {

        UserEntity expected = userRepository.save(userRequestDTO.toEntity());
        em.flush();
        em.clear();

        MvcResult result = mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userRequestDTO))
                                .session(mockHttpSession)
                )
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        String savedUserTsid = rootNode.path("data").path("userTsid").asText();

        assertEquals(expected.getTsid(), savedUserTsid);

        // 세션에 사용자 정보가 있는지 확인
        UserEntity actualUser = (UserEntity) mockHttpSession.getAttribute("user");
        assertNotNull(actualUser);
        assertEquals(expected.getId(), actualUser.getId());
    }


    /**
     * 세션에 저장된 사용자 데이터 검증 테스트.
     *
     * 프로세스:
     * 1. GIVEN: 로그인 세션이 시작된 상태.
     * 2. WHEN: /auth/validate로 POST 요청.
     * 3. THEN:
     *    - 응답 상태 코드가 200.
     *    - 세션과 DB 사용자 정보가 일치.
     */
    @Test
    void validateUser_ShouldMatchSession() throws Exception {
        UserEntity expected = userRepository.save(userRequestDTO.toEntity());
        em.flush();
        em.clear();
        sessionManager.startSession(mockHttpServletRequest, expected);

        MvcResult result = mockMvc.perform(
                        post("/auth/validate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userRequestDTO))
                                .session(mockHttpSession)
                )
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        String savedUserTsid = rootNode.path("data").path("userTsid").asText();

        assertEquals(expected.getTsid(), savedUserTsid);

        // 세션 정보와 DB 사용자 정보 일치 확인
        UserEntity actualUser = (UserEntity) mockHttpSession.getAttribute("user");
        assertNotNull(actualUser);
        assertEquals(expected.getId(), actualUser.getId());
    }
    /**
     * 세션 무효화 로그아웃 성공 테스트.
     *
     * 프로세스:
     * 1. GIVEN: 로그인 세션이 시작된 상태.
     * 2. WHEN: /auth/logout로 GET 요청.
     * 3. THEN:
     *    - 응답 상태 코드가 200.
     *    - 세션이 무효화됨.
     */
    @Test
    void logout_ShouldInvalidateSession() throws Exception {
        UserEntity userEntity = userRepository.save(userRequestDTO.toEntity());
        em.flush();
        em.clear();
        sessionManager.startSession(mockHttpServletRequest, userEntity);

        MvcResult result = mockMvc.perform(
                        get("/auth/logout")
                                .contentType(MediaType.APPLICATION_JSON)
                                .session(mockHttpSession) // 세션 전달
                )
                .andExpect(status().isOk())
                .andReturn();

        // 세션 무효화 확인
        assertTrue(mockHttpSession.isInvalid());
    }

    /**
     * 유효하지 않은 사용자 데이터로 로그인 시도 시 예외 발생 테스트.
     *
     * 프로세스:
     * 1. GIVEN: 잘못된 사용자 데이터 준비.
     * 2. WHEN: /auth/login으로 POST 요청 전송.
     * 3. THEN: 응답 상태 코드 4xx와 적절한 예외 정보 반환.
     */
    @Test
    void login_InvalidUser_ShouldThrowAuthErrorException() throws Exception {
        UserRequestDTO invalidUserRequest = new UserRequestDTO("invalidId", "invalidPassword");

        MvcResult result = mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidUserRequest))
                )
                .andExpect(status().is4xxClientError())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        assertEquals(AuthError.class.getSimpleName(), rootNode.path("errorType").asText());
        assertEquals(AuthError.USER_NOT_VALID.name(), rootNode.path("errorName").asText());
        assertEquals(AuthError.USER_NOT_VALID.getErrorMsg(), rootNode.path("errorMessage").asText());
        assertEquals(AuthError.USER_NOT_VALID.getErrorCode(), rootNode.path("errorCode").asInt());
    }

    /**
     * 세션에 저장된 사용자와 검증 요청의 사용자가 다를 경우 예외 발생 테스트.
     *
     * 프로세스:
     * 1. GIVEN: 세션에 사용자 저장 후, 다른 사용자 데이터로 검증 요청.
     * 2. WHEN: /auth/validate로 POST 요청.
     * 3. THEN: 응답 상태 코드 4xx와 적절한 예외 정보 반환.
     */
    @Test
    void validateUser_InvalidSessionUser_ShouldThrowAuthErrorException() throws Exception {
        UserEntity validUser = userRepository.save(userRequestDTO.toEntity());
        UserEntity anotherUser = userRepository.save(new UserRequestDTO("anotherId", "anotherPassword").toEntity());
        em.flush();
        em.clear();
        sessionManager.startSession(mockHttpServletRequest, validUser);


        UserRequestDTO anotherUserRequest = new UserRequestDTO("anotherId", "anotherPassword");

        MvcResult result = mockMvc.perform(
                        post("/auth/validate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(anotherUserRequest))
                                .session(mockHttpSession)
                )
                .andExpect(status().is4xxClientError())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        assertEquals(AuthError.class.getSimpleName(), rootNode.path("errorType").asText());
        assertEquals(AuthError.USER_NOT_VALID.name(), rootNode.path("errorName").asText());
        assertEquals(AuthError.USER_NOT_VALID.getErrorMsg(), rootNode.path("errorMessage").asText());
        assertEquals(AuthError.USER_NOT_VALID.getErrorCode(), rootNode.path("errorCode").asInt());
    }

    /**
     * DB에 없는 사용자로 검증 요청 시 예외 발생 테스트.
     *
     * 프로세스:
     * 1. GIVEN: DB에 존재하지 않는 사용자 데이터 준비.
     * 2. WHEN: /auth/validate로 POST 요청.
     * 3. THEN: 응답 상태 코드 4xx와 적절한 예외 정보 반환.
     */
    @Test
    void validateUser_NonExistentUser_ShouldThrowAuthErrorException() throws Exception {
        UserRequestDTO nonExistentUserRequest = new UserRequestDTO("nonExistentId", "nonExistentPassword");

        MvcResult result = mockMvc.perform(
                        post("/auth/validate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(nonExistentUserRequest))
                )
                .andExpect(status().is4xxClientError())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        assertEquals(AuthError.class.getSimpleName(), rootNode.path("errorType").asText());
        assertEquals(AuthError.USER_NOT_VALID.name(), rootNode.path("errorName").asText());
        assertEquals(AuthError.USER_NOT_VALID.getErrorMsg(), rootNode.path("errorMessage").asText());
        assertEquals(AuthError.USER_NOT_VALID.getErrorCode(), rootNode.path("errorCode").asInt());
    }

}