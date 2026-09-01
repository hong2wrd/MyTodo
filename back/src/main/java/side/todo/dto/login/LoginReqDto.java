package side.todo.dto.login;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LoginReqDto {

    @Pattern(regexp = "^[a-zA-Z0-9]{5,20}$", message = "영문/숫자 5~20자 이내로 작성해주세요")
    @NotEmpty
    private String memberId;

    @NotEmpty
    @Size(min = 4, max = 20)
    private String password;
}
