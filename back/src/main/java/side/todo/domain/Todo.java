package side.todo.domain;

import jakarta.persistence.*;
import lombok.*;
import side.todo.dto.todo.TodoRespDto;

@Entity
@Getter
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String title;

    @Column(length = 100)
    private String content;

    @Builder.Default
    @Column(nullable = false)
    private boolean completed = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_type_id")
    private TodoType todoType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_id")
    private Member member;

    public static Todo create(String title, String content, Member member) {
        return Todo.builder()
                .title(title)
                .content(content)
                .member(member)
                .build();
    }

    public static Todo create(String title, String content, TodoType todoType, Member member) {
        return Todo.builder()
                .title(title)
                .content(content)
                .todoType(todoType)
                .member(member)
                .build();
    }

    public void update(String title, String content, TodoType todoType) {
        this.title = title;
        this.content = content;
        this.todoType = todoType;
    }

    public TodoRespDto toDto() {
        return TodoRespDto.builder()
                .todoId(id)
                .title(title)
                .content(content)
                .todoType(todoType.toDto())
                .build();
    }

}
