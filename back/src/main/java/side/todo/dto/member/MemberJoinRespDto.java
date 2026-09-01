package side.todo.dto.member;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberJoinRespDto {

    private String memberId;
    private String memberName;

}
