package side.todo.service;

import side.todo.domain.Member;
import side.todo.dto.member.*;

public interface MemberService {

    MemberJoinRespDto joinMember(MemberJoinReqDto joinReqDto);

    void retireMember(MemberRetireReqDto retireReqDto);

    MemberUpdateRespDto updateMember(MemberUpdateReqDto updateReqDto);

    void updatePassword(MemberUpdatePasswordReqDto updatePasswordReqDto, Member member);

    MemberSearchRespDto searchMember(String memberId, Member member);
}
