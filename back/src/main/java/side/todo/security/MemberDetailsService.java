package side.todo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import side.todo.domain.Member;
import side.todo.repository.MemberRepository;

@Component
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * 로그인 시
     * @param memberId 사용자 ID
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        Member findMember = memberRepository.findByMemberIdAndRetired(memberId, false)
                .orElseThrow(() -> new InternalAuthenticationServiceException("아이디 및 비밀번호를 확인해주세요."));
        return new MemberDetails(findMember);
    }
}
