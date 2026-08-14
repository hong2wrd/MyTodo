package side.todo.dto.login;

import lombok.Builder;
import side.todo.domain.Member;

@Builder
public class LoginRespDto {
    private String userId;
    private String name;

    public static LoginRespDto from(Member member) {
        return LoginRespDto.builder()
                .userId(member.getMemberId())
                .name(member.getName())
                .build();
    }
}
