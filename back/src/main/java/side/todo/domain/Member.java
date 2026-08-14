package side.todo.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;import java.util.ArrayList;import java.util.List;

@Entity
@Getter
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mem_id", nullable = false, unique = true, length = 20, updatable = false)
    private String memberId;

    @Column(name = "mem_name")
    private String name;

    @Embedded
    private PhoneNumber phoneNumber;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<TodoType> todoTypes = new ArrayList<>();

    public void addTodoType(TodoType todoType) {
        this.todoTypes.add(todoType);
        todoType.assignMember(this);
    }

}
