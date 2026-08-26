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
import side.todo.domain.Todo;
import side.todo.domain.TodoType;
import side.todo.dto.member.MemberJoinReqDto;
import side.todo.dto.todo.TodoSaveReqDto;
import side.todo.dto.todo.TodoUpdateReqDto;
import side.todo.repository.MemberRepository;
import side.todo.repository.TodoRepository;
import side.todo.repository.TodoTypeRepository;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
class TodoControllerTest {

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

    TodoType saveTodoType;
    Long todoId;

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

        TodoType todoType = TodoType.create("일상", member);
        saveTodoType = todoTypeRepository.saveAndFlush(todoType);

        Todo todo = Todo.create("beforeTitle1", "BeforeContent1", todoType, member);
        todoRepository.save(Todo.create("beforeTitle2", "BeforeContent2", todoType, member));
        todoRepository.save(Todo.create("beforeTitle3", "BeforeContent3", todoType, member));
        todoId = todoRepository.saveAndFlush(todo).getId();
    }

    @Test
    @DisplayName("1-1. Todo 저장 성공(TodoType O)")
    @WithUserDetails(value = "test0000", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void successTodoSave2Test() throws Exception {
        // given
        TodoSaveReqDto todoSaveReqDto = new TodoSaveReqDto();
        todoSaveReqDto.setMemberId("test0000");
        todoSaveReqDto.setTitle("test title");
        todoSaveReqDto.setContent("test content...");
        todoSaveReqDto.setTodoType(saveTodoType.getId());

        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(todoSaveReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
        resultActions.andExpect(jsonPath("$.data.title").value(todoSaveReqDto.getTitle()));
    }

    @Test
    @DisplayName("1-2. Todo 저장 실패(TodoType X)")
    @WithUserDetails(value = "test0000", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void unsuccessTodoSave1Test() throws Exception {
        // given
        TodoSaveReqDto todoSaveReqDto = new TodoSaveReqDto();
        todoSaveReqDto.setMemberId("test0000");
        todoSaveReqDto.setTitle("test title");
        todoSaveReqDto.setContent("test content...");
        todoSaveReqDto.setTodoType(null);

        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(todoSaveReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("1-3. 투두 저장 실패(TodoType가 다를 경우)")
    @WithUserDetails(value = "test0000", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void unsuccessTodoSave2Test() throws Exception {
        // given
        TodoSaveReqDto todoSaveReqDto = new TodoSaveReqDto();
        todoSaveReqDto.setMemberId("test0000");
        todoSaveReqDto.setTitle("test title");
        todoSaveReqDto.setContent("test content...");
        todoSaveReqDto.setTodoType(1000L);

        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(todoSaveReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("1-4. Todo 정보 변경 성공")
    @WithUserDetails(value = "test0000", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void successTodoUpdateTest() throws Exception {
        // given
        TodoUpdateReqDto todoUpdateReqDto = new TodoUpdateReqDto();
        todoUpdateReqDto.setMemberId("test0000");
        todoUpdateReqDto.setTodoId(todoId);
        todoUpdateReqDto.setTitle("afterTitle");
        todoUpdateReqDto.setContent("afterContent");
        todoUpdateReqDto.setTodoType("일상");

        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(todoUpdateReqDto);

        // when
        ResultActions resultActions = mvc.perform(put("/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(jsonPath("$.data.content").value(todoUpdateReqDto.getContent()));
    }

    @Test
    @DisplayName("1-5. Todo 정보 변경 실패")
    @WithUserDetails(value = "test0000", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void unsuccessTodoUpdateTest() throws Exception {
        // given
        TodoUpdateReqDto todoUpdateReqDto = new TodoUpdateReqDto();
        todoUpdateReqDto.setMemberId("test0000");
        todoUpdateReqDto.setTodoId(100L);
        todoUpdateReqDto.setTitle("afterTitle");
        todoUpdateReqDto.setContent("afterContent");
        todoUpdateReqDto.setTodoType("일상");

        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(todoUpdateReqDto);

        // when
        ResultActions resultActions = mvc.perform(put("/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("1-6. Todo 정보 삭제 성공")
    @WithUserDetails(value = "test0000", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void successTodoDeleteTest() throws Exception {
        // given
        Long deleteTodoId = todoId;

        // when
        ResultActions resultActions = mvc.perform(delete("/todo/" + deleteTodoId));

        todoRepository.flush();

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("1-7. Todo 정보 삭제 실패")
    @WithUserDetails(value = "test0000", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void unsuccessTodoDeleteTest() throws Exception {
        // given
        Long deleteTodoId = 100L; // 없는 TodoId

        // when
        ResultActions resultActions = mvc.perform(delete("/todo/" + deleteTodoId));

        todoRepository.flush();

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("1-8. Todo 단건 조회 성공")
    @WithUserDetails(value = "test0000", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void successTodoSearchTest() throws Exception {
        // given
        Long searchTodoId = todoId;

        // when
        ResultActions resultActions = mvc.perform(get("/todo/" + searchTodoId));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("1-9. Todo 다건 조회 성공")
    @WithUserDetails(value = "test0000", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void successTodosSearchTest() throws Exception {
        // given
        Long todoTypeId = saveTodoType.getId();
        String memberId = "test0000";

        // when
        ResultActions resultActions = mvc.perform(get("/todo/" + memberId + "/" + todoTypeId));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }
}