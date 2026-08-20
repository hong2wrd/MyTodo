package side.todo.dto.member;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberConflictRespDto {

    private String memberId;
    private boolean conflict;

}
