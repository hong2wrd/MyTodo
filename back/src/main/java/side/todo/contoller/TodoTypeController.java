package side.todo.contoller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import side.todo.dto.ResponseDto;
import side.todo.dto.todoType.ToDoTypeRespDto;
import side.todo.dto.todoType.TodoTypeSaveReqDto;
import side.todo.dto.todoType.TodoTypeUpdateReqDto;
import side.todo.security.MemberDetails;
import side.todo.service.TodoTypeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todoType")
public class TodoTypeController {

    private final TodoTypeService todoTypeService;

    @GetMapping("/list")
    public ResponseEntity<?> searchTodoTypes(@AuthenticationPrincipal MemberDetails memberDetails) {
        List<ToDoTypeRespDto> toDoTypeRespDtos = todoTypeService.searchTodoTypes(memberDetails.getMember());
        return new ResponseEntity<>(new ResponseDto<>(1, "Todo 타입을 조회하였습니다.", toDoTypeRespDtos), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> saveTodoType(@RequestBody @Valid TodoTypeSaveReqDto todoTypeSaveReqDto,
                                          @AuthenticationPrincipal MemberDetails memberDetails) {
        ToDoTypeRespDto toDoTypeRespDto = todoTypeService.saveTodoType(todoTypeSaveReqDto, memberDetails.getMember());
        return new ResponseEntity<>(new ResponseDto<>(1, "Todo 타입을 저장하였습니다.", toDoTypeRespDto), HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<?> updateTodoType(@RequestBody @Valid TodoTypeUpdateReqDto todotypeUpdateReqDto,
                                            @AuthenticationPrincipal MemberDetails memberDetails) {
        ToDoTypeRespDto toDoTypeRespDto = todoTypeService.updateTodoType(todotypeUpdateReqDto, memberDetails.getMember());
        return new ResponseEntity<>(new ResponseDto<>(1, "Todo 타입을 변경하였습니다.", toDoTypeRespDto), HttpStatus.OK);
    }

    @DeleteMapping("/{todoTypeId}")
    public ResponseEntity<?> deleteTodoType(@PathVariable Long todoTypeId,
                                            @AuthenticationPrincipal MemberDetails memberDetails) {
        todoTypeService.deleteTodoType(todoTypeId, memberDetails.getMember());
        return new ResponseEntity<>(new ResponseDto<>(1, "Todo 타입이 삭제되었습니다."), HttpStatus.OK);
    }
}