package side.todo.contoller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import side.todo.domain.Member;
import side.todo.dto.ResponseDto;
import side.todo.dto.member.*;
import side.todo.security.MemberDetails;
import side.todo.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public ResponseEntity<?> searchMember(@PathVariable String memberId,
                                          @AuthenticationPrincipal MemberDetails memberDetails) {
        Member member = memberDetails.getMember();
        MemberSearchRespDto searchRespDto =  memberService.searchMember(memberId, member);
        return new ResponseEntity<>(new ResponseDto<>(1, "사용자 정보를 조회했습니다.", searchRespDto), HttpStatus.OK);
    }

    @GetMapping("/{memberId}/conflict")
    public ResponseEntity<?> searchConflictMember(@PathVariable String memberId) {
        MemberConflictRespDto memberConflictRespDto = memberService.searchConflictMember(memberId);
        return new ResponseEntity<>(new ResponseDto<>(1, "사용자 정보를 조회했습니다.", memberConflictRespDto), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> joinMember(@RequestBody @Valid MemberJoinReqDto joinReqDto) {
        MemberJoinRespDto joinRespDto = memberService.joinMember(joinReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "가입이 완료되었습니다.", joinRespDto), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> retireMember(@RequestBody @Valid MemberRetireReqDto retireReqDto) {
        memberService.retireMember(retireReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "탈퇴가 완료되었습니다."), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateMember(@RequestBody @Valid MemberUpdateReqDto updateReqDto) {
        MemberUpdateRespDto memberUpdateRespDto = memberService.updateMember(updateReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "사용자 정보가 변경되었습니다.", memberUpdateRespDto), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<?> updatePassword(@RequestBody @Valid MemberUpdatePasswordReqDto updatePasswordReqDto,
                                            @AuthenticationPrincipal MemberDetails memberDetails) {
        memberService.updatePassword(updatePasswordReqDto, memberDetails.getMember());
        return new ResponseEntity<>(new ResponseDto<>(1, "비밀번호가 변경되었습니다."), HttpStatus.OK);
    }
}
