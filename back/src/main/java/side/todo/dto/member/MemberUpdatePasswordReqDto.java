package side.todo.dto.member;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberUpdatePasswordReqDto {

    @NotEmpty
    @Size(min = 4, max = 20)
    private String changePassword;
}
