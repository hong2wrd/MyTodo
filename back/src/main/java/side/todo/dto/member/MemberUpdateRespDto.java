package side.todo.dto.member;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateRespDto {
    private String memberId;
    private String memberName;
    private String ddd;
    private String tel1;
    private String tel2;
}
