package side.todo.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import side.todo.domain.Member;
import side.todo.dto.join.MemberJoinReqDto;
import side.todo.dto.login.LoginReqDto;
import side.todo.repository.MemberRepository;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
class JwtAuthenticationFilterTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtProperties jwtProperties;

    @BeforeEach
    void setUp() {
        MemberJoinReqDto memberJoinReqDto = new MemberJoinReqDto();
        memberJoinReqDto.setMemberId("test1234");
        memberJoinReqDto.setMemberName("testName");
        memberJoinReqDto.setPassword("1234");
        memberJoinReqDto.setDdd("010");
        memberJoinReqDto.setTel1("0000");
        memberJoinReqDto.setTel2("1111");

        String encryptPassword = passwordEncoder.encode(memberJoinReqDto.getPassword());

        memberRepository.save(Member.from(memberJoinReqDto, encryptPassword));
    }

    @Test
    @DisplayName("successfulAuthentication 확인")
    void successfulAuthenticationTest() throws Exception {
        // given
        LoginReqDto loginReqDto = new LoginReqDto();
        loginReqDto.setMemberId("test1234");
        loginReqDto.setPassword("1234");

        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(loginReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        String jwtAccessToken = resultActions.andReturn().getResponse().getHeader(jwtProperties.getAccessTokenHeader());
        String jwtRefreshToken = resultActions.andReturn().getResponse().getHeader(jwtProperties.getRefreshTokenHeader());

        System.out.println("responseBody = " + responseBody);
        System.out.println("jwtAccessToken = " + jwtAccessToken);
        System.out.println("jwtRefreshToken = " + jwtRefreshToken);

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        assertNotNull(jwtAccessToken);
        assertNotNull(jwtRefreshToken);
        resultActions.andExpect(jsonPath("$.data.name").value("testName"));

    }

    @Test
    @DisplayName("unsuccessfulAuthentication 확인(password X)")
    void unsuccessfulAuthenticationTest1() throws Exception {
        // given
        LoginReqDto loginReqDto = new LoginReqDto();
        loginReqDto.setMemberId("test1234");
        loginReqDto.setPassword("12345");

        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(loginReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        String jwtAccessToken = resultActions.andReturn().getResponse().getHeader(jwtProperties.getAccessTokenHeader());
        String jwtRefreshToken = resultActions.andReturn().getResponse().getHeader(jwtProperties.getRefreshTokenHeader());

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());
        assertNull(jwtAccessToken);
        assertNull(jwtRefreshToken);
    }

    @Test
    @DisplayName("unsuccessfulAuthentication 확인(memberId X)")
    void unsuccessfulAuthentication2() throws Exception {
        // given
        LoginReqDto loginReqDto = new LoginReqDto();
        loginReqDto.setMemberId("test12345");
        loginReqDto.setPassword("1234");

        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(loginReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
        String jwtAccessToken = resultActions.andReturn().getResponse().getHeader(jwtProperties.getAccessTokenHeader());
        String jwtRefreshToken = resultActions.andReturn().getResponse().getHeader(jwtProperties.getRefreshTokenHeader());

        resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());
        assertNull(jwtAccessToken);
        assertNull(jwtRefreshToken);
    }

}