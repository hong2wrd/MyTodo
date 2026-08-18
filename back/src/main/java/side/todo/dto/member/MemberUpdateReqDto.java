package side.todo.dto.member;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateReqDto {

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9]{5,20}$", message = "영문/숫자 5~20자 이내로 작성해주세요.")
    private String memberId;

    @NotEmpty
    private String memberName;

    @NotEmpty
    @Size(min = 3, max = 3, message = "휴대폰번호 양식을 확인해주세요.")
    private String ddd;

    @NotEmpty
    @Size(min = 4, max = 4, message = "휴대폰번호 양식을 확인해주세요.")
    private String tel1;

    @NotEmpty
    @Size(min = 4, max = 4, message = "휴대폰번호 양식을 확인해주세요.")
    private String tel2;
}
