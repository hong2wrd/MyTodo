package side.todo.contoller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import side.todo.dto.ResponseDto;
import side.todo.dto.todo.*;
import side.todo.security.MemberDetails;
import side.todo.service.TodoService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todo")
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<?> saveTodo(@RequestBody @Valid TodoSaveReqDto todoSaveReqDto,
                                      @AuthenticationPrincipal MemberDetails memberDetails) {
        TodoSaveRespDto todoSaveRespDto = todoService.saveTodo(todoSaveReqDto, memberDetails.getUsername());
        return new ResponseEntity<>(new ResponseDto<>(1, "새로운 Todo가 저장되었습니다.", todoSaveRespDto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@RequestBody @Valid TodoUpdateReqDto todoUpdateReqDto,
                                        @AuthenticationPrincipal MemberDetails memberDetails) {
        TodoUpdateRespDto todoUpdateRespDto = todoService.updateTodo(todoUpdateReqDto, memberDetails.getUsername());
        return new ResponseEntity<>(new ResponseDto<>(1, "Todo가 변경되었습니다.", todoUpdateRespDto), HttpStatus.OK);
    }

    @PatchMapping("/{todoId}")
    public ResponseEntity<?> completeTodo(@PathVariable Long todoId,
                                          @AuthenticationPrincipal MemberDetails memberDetails) {
        todoService.completeTodo(todoId, memberDetails.getUsername());
        return ResponseEntity.ok(null);
    }

    @PatchMapping("/{todoId}/{todoTypeId}")
    public ResponseEntity<?> changeTodoTypeTodo(@PathVariable Long todoId,
                                                @PathVariable Long todoTypeId,
                                                @AuthenticationPrincipal MemberDetails memberDetails) {
        todoService.changeTodoType(todoId, todoTypeId, memberDetails.getUsername());
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<?> deleteTodo(@PathVariable Long todoId,
                                        @AuthenticationPrincipal MemberDetails memberDetails) {
        todoService.deleteTodo(todoId, memberDetails.getUsername());
        return new ResponseEntity<>(new ResponseDto<>(1, "Todo가 삭제되었습니다."), HttpStatus.OK);
    }

    @GetMapping("/todoType/{todoTypeId}")
    public ResponseEntity<?> searchTodos(@PathVariable String memberId,
                                         @PathVariable(required = false) Long todoTypeId,
                                         @AuthenticationPrincipal MemberDetails memberDetails) {
        List<TodoRespDto> todoRespDtos = todoService.searchTodos(todoTypeId, memberDetails.getUsername());
        return new ResponseEntity<>(new ResponseDto<>(1, "Todo가 조회되었습니다.", todoRespDtos), HttpStatus.OK);
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<?> searchTodo(@PathVariable Long todoId,
                                        @AuthenticationPrincipal MemberDetails memberDetails) {
        TodoRespDto todoRespDto = todoService.searchTodo(todoId, memberDetails.getUsername());
        return new ResponseEntity<>(new ResponseDto<>(1, "Todo가 조회되었습니다.", todoRespDto), HttpStatus.OK);
    }

}
