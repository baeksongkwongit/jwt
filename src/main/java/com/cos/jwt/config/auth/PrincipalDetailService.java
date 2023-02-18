package com.cos.jwt.config.auth;

import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


//Http://localhost:8080/login 요청이 올대 동작 한다.  => 여기서 동작을 안함 securityConfig 에서 diable 해서 그렇다.
@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

    private  final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailService 의 loadUserByUsername");
        User userEntity = userRepository.findByUsername(username);
        return new PrincipalDetails(userEntity);
    }
}
