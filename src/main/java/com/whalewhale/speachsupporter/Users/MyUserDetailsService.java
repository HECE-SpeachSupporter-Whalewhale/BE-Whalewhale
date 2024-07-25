package com.whalewhale.speachsupporter.Users;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var result = usersRepository.findByUsername(username);
        if(result.isEmpty()) {
            throw new UsernameNotFoundException("아이디를 잘못 입력하셨습니다.");
        }
        var userdata = result.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (userdata.getIsAdmin() != null && userdata.getIsAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ADMIN")); // 관리자 권한
        } else {
            authorities.add(new SimpleGrantedAuthority("USER")); // 일반 사용자 권한
        }

        return new User(userdata.getUsername(), userdata.getPassword(), authorities);
    }
}
