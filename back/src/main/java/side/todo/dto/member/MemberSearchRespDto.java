package side.todo.dto.member;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSearchRespDto {

    private String memberId;
    private String memberName;
    private String ddd;
    private String tel1;
    private String tel2;
}
