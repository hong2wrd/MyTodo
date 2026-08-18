package side.todo.contoller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import side.todo.domain.Member;
import side.todo.domain.PhoneNumber;
import side.todo.dto.member.MemberJoinReqDto;
import side.todo.dto.member.MemberRetireReqDto;
import side.todo.dto.member.MemberUpdatePasswordReqDto;
import side.todo.dto.member.MemberUpdateReqDto;
import side.todo.repository.MemberRepository;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.boot.test.context.SpringBootTest.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
class MemberControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        MemberJoinReqDto memberJoinReqDto = new MemberJoinReqDto();
        memberJoinReqDto.setMemberId("test0000");
        memberJoinReqDto.setMemberName("testName");
        memberJoinReqDto.setPassword("1234");
        memberJoinReqDto.setDdd("010");
        memberJoinReqDto.setTel1("0000");
        memberJoinReqDto.setTel2("1111");

        String encryptPassword = passwordEncoder.encode(memberJoinReqDto.getPassword());

        PhoneNumber phoneNumber = PhoneNumber.builder()
                .ddd(memberJoinReqDto.getDdd())
                .tel1(memberJoinReqDto.getTel1())
                .tel2(memberJoinReqDto.getTel2())
                .build();

        Member member = Member.create(memberJoinReqDto.getMemberId(), memberJoinReqDto.getMemberName(), encryptPassword, phoneNumber);

        memberRepository.save(member);
    }

    @Test
    @DisplayName("회원가입 성공")
    void successJoin() throws Exception {
        // given
        MemberJoinReqDto joinReqDto = new MemberJoinReqDto();
        joinReqDto.setMemberId("test1234");
        joinReqDto.setMemberName("testName");
        joinReqDto.setPassword("1234");
        joinReqDto.setDdd("010");
        joinReqDto.setTel1("0000");
        joinReqDto.setTel2("1111");

        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(joinReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
        resultActions.andExpect(jsonPath("$.data.memberName").value("testName"));
    }

    @Test
    @DisplayName("회원가입 실패")
    void unsuccessJoin() throws Exception {
        // given
        MemberJoinReqDto joinReqDto = new MemberJoinReqDto();
        joinReqDto.setMemberId("test0000");
        joinReqDto.setMemberName("testName");
        joinReqDto.setPassword("1234");
        joinReqDto.setDdd("010");
        joinReqDto.setTel1("0000");
        joinReqDto.setTel2("1111");

        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(joinReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @DisplayName("탈퇴 성공")
    @WithUserDetails(value = "test0000", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void successRetire() throws Exception {
        // given
        MemberRetireReqDto retireReqDto = new MemberRetireReqDto();
        retireReqDto.setMemberId("test0000");

        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(retireReqDto);

        // when
        ResultActions resultActions = mvc.perform(delete("/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        memberRepository.flush();

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("탈퇴 실패")
    @WithUserDetails(value = "test0000", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void unsuccessRetire() throws Exception {
        // given
        MemberRetireReqDto retireReqDto = new MemberRetireReqDto();
        retireReqDto.setMemberId("test0001");

        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(retireReqDto);

        // when
        ResultActions resultActions = mvc.perform(delete("/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("사용자정보 변경 성공")
    @WithUserDetails(value = "test0000", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void successUpdate() throws Exception {
        // given
        MemberUpdateReqDto updateReqDto = new MemberUpdateReqDto();
        updateReqDto.setMemberId("test0000");
        updateReqDto.setMemberName("updateTesteName");
        updateReqDto.setDdd("010");
        updateReqDto.setTel1("1234");
        updateReqDto.setTel2("5678");

        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(updateReqDto);

        // when
        ResultActions resultActions = mvc.perform(put("/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        memberRepository.flush();

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    @WithUserDetails(value = "test0000", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void successUpdatePassword() throws Exception {
        // given
        MemberUpdatePasswordReqDto updateReqDto = new MemberUpdatePasswordReqDto();
        updateReqDto.setMemberId("test0000");
        updateReqDto.setChangePassword("123456asdf");

        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(updateReqDto);

        // when
        ResultActions resultActions = mvc.perform(patch("/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        memberRepository.flush();

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("비밀번호 변경 실패")
    @WithUserDetails(value = "test0000", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void unsuccessUpdatePassword() throws Exception {
        // given
        MemberUpdatePasswordReqDto updateReqDto = new MemberUpdatePasswordReqDto();
        updateReqDto.setMemberId("test0001");
        updateReqDto.setChangePassword("123456asdf");

        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(updateReqDto);

        // when
        ResultActions resultActions = mvc.perform(patch("/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}