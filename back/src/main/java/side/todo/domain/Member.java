package side.todo.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import side.todo.domain.enums.Role;

import java.util.ArrayList;import java.util.List;

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

    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @Column(name = "mem_name")
    private String name;

    @Builder.Default
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.MEMBER;

    @Embedded
    private PhoneNumber phoneNumber;

    @Builder.Default
    @Column(name = "retired", nullable = false)
    private boolean retired = false;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<TodoType> todoTypes = new ArrayList<>();

    public void addTodoType(TodoType todoType) {
        this.todoTypes.add(todoType);
        todoType.assignMember(this);
    }

    public static Member of(String memberId, String role, String name) {
        return Member.builder()
                .memberId(memberId)
                .role(Role.valueOf(role))
                .name(name)
                .build();
    }

    public static Member create(String memberId, String memberName, String encryptPassword, PhoneNumber phoneNumber) {
        return Member.builder()
                .memberId(memberId)
                .name(memberName)
                .password(encryptPassword)
                .phoneNumber(phoneNumber)
                .build();
    }

    public void retire() {
        this.retired = true;
    }

    public void update(String name, String ddd, String tel1, String tel2) {
        this.name = name;
        this.phoneNumber.update(ddd, tel1, tel2);
    }

    public void changePassword(String changePassword) {
        this.password = changePassword;
    }

}
