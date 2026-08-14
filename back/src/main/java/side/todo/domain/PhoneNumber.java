package side.todo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PhoneNumber {

    @Builder.Default
    @Column(name = "phone_ddd", length = 3, nullable = false)
    private String ddd = "010";

    @Column(name = "phone_tel1", length = 4, nullable = false)
    private String tel1;

    @Column(name = "phone_tel2", length = 4, nullable = false)
    private String tel2;

}
