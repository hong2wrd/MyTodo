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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import side.todo.domain.Member;
import side.todo.domain.PhoneNumber;
import side.todo.domain.Todo;
import side.todo.domain.TodoType;
import side.todo.dto.member.MemberJoinReqDto;
import side.todo.dto.todoType.TodoTypeSaveReqDto;
import side.todo.dto.todoType.TodoTypeUpdateReqDto;
import side.todo.repository.MemberRepository;
import side.todo.repository.TodoRepository;
import side.todo.repository.TodoTypeRepository;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TodoTypeControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    TodoTypeRepository todoTypeRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    Long todoTypeId;

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

        memberRepository.saveAndFlush(member);

        TodoType todoType1 = TodoType.create("일상", member);
        TodoType todoType2 = TodoType.create("운동", member);
        List<TodoType> todoTypes = todoTypeRepository.saveAllAndFlush(List.of(todoType1, todoType2));

        todoRepository.saveAllAndFlush(
                List.of(
                        Todo.create("beforeTitle1", "BeforeContent1", todoType1, member),
                        Todo.create("beforeTitle2", "BeforeContent2", todoType1, member),
                        Todo.create("beforeTitle3", "BeforeContent3", todoType2, member)
                )
        );

        todoTypeId = todoTypes.get(0).getId();
    }

    @Test
    @DisplayName("1-1. TodoType 다건 조회 성공")
    @WithUserDetails(value = "test0000", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void successSearchTodoTyposTest() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(get("/todoType/list"));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("1-2. TodoType 저장 성공")
    @WithUserDetails(value = "test0000", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void successSaveTodoTyposTest() throws Exception {
        // given
        TodoTypeSaveReqDto todoTypeSaveReqDto = new TodoTypeSaveReqDto();
        todoTypeSaveReqDto.setMemberId("test0000");
        todoTypeSaveReqDto.setTitle("기타");

        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(todoTypeSaveReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/todoType")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                );
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("1-3. TodoType 저장 실패(사용자ID가 다를경우)")
    @WithUserDetails(value = "test0000", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void unsuccessSaveTodoTyposTest1() throws Exception {
        // given
        TodoTypeSaveReqDto todoTypeSaveReqDto = new TodoTypeSaveReqDto();
        todoTypeSaveReqDto.setMemberId("test0001");
        todoTypeSaveReqDto.setTitle("기타");

        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(todoTypeSaveReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/todoType")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("1-4. TodoType 저장 실패(제목 빈값)")
    @WithUserDetails(value = "test0000", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void unsuccessSaveTodoTyposTest2() throws Exception {
        // given
        TodoTypeSaveReqDto todoTypeSaveReqDto = new TodoTypeSaveReqDto();
        todoTypeSaveReqDto.setMemberId("test0000");
        todoTypeSaveReqDto.setTitle(" ");

        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(todoTypeSaveReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/todoType")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("1-5. TodoType 저장 실패(제목 10자리 초과)")
    @WithUserDetails(value = "test0000", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void unsuccessSaveTodoTyposTest3() throws Exception {
        // given
        TodoTypeSaveReqDto todoTypeSaveReqDto = new TodoTypeSaveReqDto();
        todoTypeSaveReqDto.setMemberId("test0000");
        todoTypeSaveReqDto.setTitle("가나다라마바사아자차카");

        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(todoTypeSaveReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/todoType")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("1-5. TodoType 변경 성공")
    @WithUserDetails(value = "test0000", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void successUpdateTodoTyposTest() throws Exception {
        // given
        TodoTypeUpdateReqDto todoTypeUpdateReqDto = new TodoTypeUpdateReqDto();
        todoTypeUpdateReqDto.setTodoTypeId(todoTypeId);
        todoTypeUpdateReqDto.setMemberId("test0000");
        todoTypeUpdateReqDto.setTitle("가나다라마바사아자차");

        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(todoTypeUpdateReqDto);

        // when
        ResultActions resultActions = mvc.perform(patch("/todoType")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        todoRepository.flush();

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("1-6. TodoType 삭제 성공")
    @WithUserDetails(value = "test0000", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void unsuccessDeleteTodoTyposTest() throws Exception {
        // given
        Long deleteTodoTypeId = todoTypeId;

        // when
        ResultActions resultActions = mvc.perform(delete("/todoType/" + deleteTodoTypeId));

        todoRepository.flush();

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }
}