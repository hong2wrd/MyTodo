package side.todo.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import side.todo.dto.todoType.ToDoTypeRespDto;

@Getter
@Entity
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class TodoType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_id")
    private Member member;

    protected void assignMember(Member member) {
        this.member = member;
    }

    public static TodoType create(String title, Member member) {
        return TodoType.builder()
                .title(title)
                .member(member)
                .build();
    }

    public void update(String title) {
        this.title = title;
    }

    public ToDoTypeRespDto toDto() {
        return ToDoTypeRespDto.builder()
                .todoTypeId(id)
                .todoTitle(title)
                .build();
    }


}
