package side.todo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import side.todo.domain.Member;
import side.todo.domain.Todo;
import side.todo.domain.TodoType;
import side.todo.repository.MemberRepository;
import side.todo.repository.TodoRepository;
import side.todo.repository.TodoTypeRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberBatchServiceImplTest {

    @Mock private MemberRepository memberRepository;
    @Mock private TodoRepository todoRepository;
    @Mock private TodoTypeRepository todoTypeRepository;
    @InjectMocks private MemberBatchServiceImpl memberBatchService;

    @Test
    void deletesRetiredMemberDataInForeignKeyOrder() {
        Member member = mock(Member.class);
        List<Member> members = List.of(member);
        List<Todo> todos = List.of(mock(Todo.class));
        List<TodoType> todoTypes = List.of(mock(TodoType.class));
        when(memberRepository.findByRetiredTrue()).thenReturn(members);
        when(todoRepository.findByMembers(members)).thenReturn(todos);
        when(todoTypeRepository.findByMembers(members)).thenReturn(todoTypes);

        int processedCount = memberBatchService.deleteRetiredMembers();

        assertThat(processedCount).isEqualTo(1);
        var ordered = inOrder(memberRepository, todoRepository, todoTypeRepository);
        ordered.verify(memberRepository).findByRetiredTrue();
        ordered.verify(todoRepository).findByMembers(members);
        ordered.verify(todoTypeRepository).findByMembers(members);
        ordered.verify(todoRepository).deleteAll(todos);
        ordered.verify(todoTypeRepository).deleteAll(todoTypes);
        ordered.verify(memberRepository).deleteAll(members);
    }

    @Test
    void returnsZeroWithoutLoadingChildrenWhenNoRetiredMembersExist() {
        when(memberRepository.findByRetiredTrue()).thenReturn(List.of());

        assertThat(memberBatchService.deleteRetiredMembers()).isZero();

        verifyNoInteractions(todoRepository, todoTypeRepository);
    }
}
