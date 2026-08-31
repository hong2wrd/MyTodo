package side.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import side.todo.domain.Member;
import side.todo.domain.PhoneNumber;
import side.todo.dto.member.*;
import side.todo.exception.ApiException;
import side.todo.exception.ErrorCode;
import side.todo.repository.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 가입
     * @param joinReqDto
     * @return MemberJoinRespDto
     */
    @Override
    public MemberJoinRespDto joinMember(MemberJoinReqDto joinReqDto) {
        memberRepository.findByMemberIdAndRetired(joinReqDto.getMemberId(), false)
                .ifPresent(member -> {
                    throw new ApiException(ErrorCode.MEM_CONFLICT);
                });

        String encryptPassword = passwordEncoder.encode(joinReqDto.getPassword());

        Member createdMember = Member.create(
                joinReqDto.getMemberId(),
                joinReqDto.getMemberName(),
                encryptPassword,
                new PhoneNumber(joinReqDto.getDdd(), joinReqDto.getTel1(), joinReqDto.getTel2())
        );

        Member saveMember = memberRepository.save(createdMember);

        return MemberJoinRespDto.builder()
                .memberId(saveMember.getMemberId())
                .memberName(saveMember.getName())
                .build();
    }

    /**
     * 탈퇴
     * @param memberId
     */
    @Override
    public void retireMember(String memberId) {

        Member findMember = memberRepository.findByMemberIdAndRetired(memberId, false)
                .orElseThrow(() -> new ApiException(ErrorCode.MEM_NOT_FOUND));

        findMember.retire();
    }

    /**
     * 수정
     * @param updateReqDto
     * @return
     */
    @Override
    public MemberUpdateRespDto updateMember(MemberUpdateReqDto updateReqDto, String memberId) {
        Member findMember = memberRepository.findByMemberIdAndRetired(memberId, false)
                .orElseThrow(() -> new ApiException(ErrorCode.MEM_NOT_FOUND));

        findMember.update(updateReqDto.getMemberName(), updateReqDto.getDdd(), updateReqDto.getTel1(), updateReqDto.getTel2());

        return MemberUpdateRespDto.builder()
                .memberId(findMember.getMemberId())
                .memberName(findMember.getName())
                .ddd(findMember.getPhoneNumber().getDdd())
                .tel1(findMember.getPhoneNumber().getTel1())
                .tel2(findMember.getPhoneNumber().getTel2())
                .build();
    }

    @Override
    public void updatePassword(MemberUpdatePasswordReqDto updatePasswordReqDto, String memberId) {
        Member findMember = memberRepository.findByMemberIdAndRetired(memberId, false)
                .orElseThrow(() -> new ApiException(ErrorCode.MEM_NOT_FOUND));

        String encryptPassword = passwordEncoder.encode(updatePasswordReqDto.getChangePassword());
        findMember.changePassword(encryptPassword);
    }

    @Override
    public MemberSearchRespDto searchMember(String memberId) {
        Member findMember = memberRepository.findByMemberIdAndRetired(memberId, false)
                .orElseThrow(() -> new ApiException(ErrorCode.MEM_NOT_FOUND));

        return MemberSearchRespDto.builder()
                .memberId(findMember.getMemberId())
                .memberName(findMember.getName())
                .ddd(findMember.getPhoneNumber().getDdd())
                .tel1(findMember.getPhoneNumber().getTel1())
                .tel2(findMember.getPhoneNumber().getTel2())
                .build();
    }

    @Override
    public MemberConflictRespDto searchConflictMember(String memberId) {
        return MemberConflictRespDto.builder()
                .memberId(memberId)
                .conflict(memberRepository.findByMemberIdAndRetired(memberId, false).isPresent())
                .build();
    }
}
