package side.todo.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import side.todo.domain.Member;
import side.todo.domain.PhoneNumber;
import side.todo.dto.member.*;
import side.todo.exception.ApiException;
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
                    throw new IllegalArgumentException("중복된 아이디 입니다.");
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
     * @param retireReqDto
     */
    @Override
    public void retireMember(MemberRetireReqDto retireReqDto) {

        Member findMember = memberRepository.findByMemberIdAndRetired(retireReqDto.getMemberId(), false)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        findMember.retire();
    }

    /**
     * 수정
     * @param updateReqDto
     * @return
     */
    @Override
    public MemberUpdateRespDto updateMember(MemberUpdateReqDto updateReqDto) {
        Member findMember = memberRepository.findByMemberIdAndRetired(updateReqDto.getMemberId(), false)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

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
    public void updatePassword(MemberUpdatePasswordReqDto updatePasswordReqDto, Member member) {

        if(!member.getMemberId().equals(updatePasswordReqDto.getMemberId())) {
            throw new IllegalArgumentException("사용자의 오류가 발생하였습니다.");
        }

        Member findMember = memberRepository.findByMemberIdAndRetired(updatePasswordReqDto.getMemberId(), false)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        String encryptPassword = passwordEncoder.encode(updatePasswordReqDto.getChangePassword());
        System.out.println(encryptPassword);
        System.out.println("encryptPassword.length() = " + encryptPassword.length());
        findMember.changePassword(encryptPassword);

    }
}
