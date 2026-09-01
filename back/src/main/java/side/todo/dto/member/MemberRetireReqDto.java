package side.todo.dto.member;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberRetireReqDto {
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9]{5,20}$", message = "영문/숫자 5~20자 이내로 작성해주세요.")
    private String memberId;
}
