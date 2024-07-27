package com.whalewhale.speachsupporter.Oauth2;

import com.whalewhale.speachsupporter.Users.Users;
import com.whalewhale.speachsupporter.Users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OAuth2MemberService extends DefaultOAuth2UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("loadUser called");

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("oAuth2User = " + oAuth2User.getAttributes());

        String email = (String) oAuth2User.getAttributes().get("email");
        String name = (String) oAuth2User.getAttributes().get("name");

        Users user = usersRepository.findByUsername(email).orElse(null);
        if (user == null) {
            user = new Users();
            user.setUsername(email);
            user.setNickname(name);
            user.setUser_job("Not specified");
            user.setIsAdmin(false);
            user.setPassword(null);

            try {
                usersRepository.save(user);
                System.out.println("User saved: " + user);
            } catch (Exception e) {
                System.err.println("Failed to save user: " + e.getMessage());
                e.printStackTrace();
            }

        }

        return oAuth2User;
    }

}