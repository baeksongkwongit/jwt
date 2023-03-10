package com.cos.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.config.auth.PrincipalDetailService;
import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

//스피링 시큐리티에서 UsernamePasswordAuthenticationFilter rk dlTdma.
// /login 요청해서 username, passsword전송하면 (post)
// UsernamePasswordAuthenticationFilter 동작을함.

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter 로그인 시도됨!!!");

        //1. username, password받아서
        try {
//            BufferedReader br = request.getReader();
//
//            String input =null;
//            while((input = br.readLine())!=null){
//                System.out.println(input);
//            }

            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(),User.class);
            System.out.println(user);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            //PricipalDetailsService의 loadUserByuUsername()함수가 실행됨된 후 정상이면 authentication이 리턴됨.
            //DB에 있는 username과 password가 일치 한다.
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("로그인완료됨 : " + principalDetails.getUser().getUsername()); //로그인 정상적으로 되었다는 뜻
            //authentication객체가 session영역에 저장을 해야하고 그 방법이 return 해주면됨.
            //리턴의 이유는 권한 관리를 security가 대신 해주기 때문에 편하려고 하는거임.
            //귿이 jwt 토큰을 사용하면서 세션을 만들 이유가 없음. 근데 단지 권한 처리때문에 session넣어 줍니다.


//            /System.out.println(request.getInputStream().toString());

            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }


        //2. 정상인지 로그인 시도를 해보는거authenticationManager 로 로긍ㄴ 시도를 하면
        // pricipalDeatilsService가 호출 loadUserByUsername() 함수 실행됨.
        //3. pricipalDetails를 세션에 담고 --> 권한 관리를 위해서
        //4.JWP 토큰을 만들어서 응답해주면됨.

//        return super.attemptAuthentication(request, response);
        return null;

    }

    //attemptAuthentication실행 후 인증이 정상적으로 되었으면 seccessfulAuthentication함수가 실행되요
    //jwt토큰을 만들어서 request요청한 사용자에게 jwt토큰을 response해주면 됨.

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        System.out.println("unsuccessfulAuthentication 실행됨 인증이 완료되었다는 뜻");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();


        String jwtToken = JWT.create()
                .withSubject("cos토콘")
                .withExpiresAt(new Date(System.currentTimeMillis()+(60000*10))) //10분
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512("cos"));


        //super.successfulAuthentication(request, response, chain, authResult);
        response.addHeader("Authorization", "Bearer " + jwtToken);
    }
}
