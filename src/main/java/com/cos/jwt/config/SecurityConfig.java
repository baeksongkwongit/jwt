package com.cos.jwt.config;

import com.cos.jwt.config.jwt.JwtAuthenticationFilter;
import com.cos.jwt.config.jwt.JwtAuthorizationFilter;
import com.cos.jwt.filter.MyFilter3;
import com.cos.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsConfig corsConfig;
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    private UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.addFilterBefore( new MyFilter3(), SecurityContextPersistenceFilter.class); //security 필터보다 먼저 실행하고 싶을때
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//세션을 사용하지 않겠습니다.
                .and()
                .addFilter(corsConfig.corsFilter()) //@CrossOrigin(인증이 없을때), 시큐리티 필터에 등록 인증(O)
                .formLogin().disable() //form 태그 로그인 안한다.
                .httpBasic().disable() //
                .addFilter(new JwtAuthenticationFilter(authenticationManager())) // AuthenticationManager 던져줘야함.
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository)) // AuthenticationManager 던져줘야함.
                .authorizeRequests()
                .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();
    }
}
