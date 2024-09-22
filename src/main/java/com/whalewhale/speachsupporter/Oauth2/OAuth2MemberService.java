package com.whalewhale.speachsupporter.Oauth2;

import com.whalewhale.speachsupporter.Users.Users;
import com.whalewhale.speachsupporter.Users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class OAuth2MemberService extends DefaultOAuth2UserService {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2MemberService.class);

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        logger.info("OAuth2User Attributes: {}", oAuth2User.getAttributes());

        // 이메일 가져오기
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        if (email == null || email.isEmpty()) {
            logger.error("OAuth2 공급자가 이메일을 반환하지 않았습니다.");
            throw new OAuth2AuthenticationException("OAuth2 공급자가 이메일을 반환하지 않았습니다.");
        }

        // 사용자 정보 DB에서 조회
        Users user = usersRepository.findByUsername(email).orElse(null);

        if (user == null) {
            // 새 사용자 저장 로직
            user = createNewUser(email, name);
            logger.info("새 OAuth2 사용자 저장: {}", email);
        } else {
            // 기존 사용자인 경우 업데이트 처리 필요 시 처리
            logger.info("이미 존재하는 OAuth2 사용자: {}", email);
        }

        // 사용자의 정보와 권한을 DefaultOAuth2User로 반환
        return new DefaultOAuth2User(
                oAuth2User.getAuthorities(),
                oAuth2User.getAttributes(),
                "email"
        );
    }

    // 새로운 사용자 생성 메소드
    private Users createNewUser(String email, String name) {
        Users newUser = new Users();
        newUser.setUsername(email);
        newUser.setNickname(name != null ? name : "Unnamed User");
        newUser.setIsAdmin(false);  // 기본값으로 관리자가 아닌 사용자로 설정
        newUser.setPassword(null);  // 비밀번호 없음 (OAuth2 사용자는 비밀번호를 사용하지 않음)
        newUser.setUser_job("INCOMPLETE_PROFILE");  // 기본 프로필 상태

        try {
            // 사용자 저장
            return usersRepository.save(newUser);
        } catch (Exception e) {
            logger.error("새 OAuth2 사용자 저장 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("OAuth2 사용자 저장 실패", e);
        }
    }
}
