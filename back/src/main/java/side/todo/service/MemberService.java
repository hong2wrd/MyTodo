package side.todo.service;

import side.todo.domain.Member;
import side.todo.dto.member.*;

public interface MemberService {

    MemberJoinRespDto joinMember(MemberJoinReqDto joinReqDto);

    void retireMember(String memberId);

    MemberUpdateRespDto updateMember(MemberUpdateReqDto updateReqDto, String memberId);

    void updatePassword(MemberUpdatePasswordReqDto updatePasswordReqDto, String memberId);

    MemberSearchRespDto searchMember(String memberId);

    MemberConflictRespDto searchConflictMember(String memberId);
}
