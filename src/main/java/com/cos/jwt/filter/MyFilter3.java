package com.cos.jwt.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyFilter3 implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        //토큰 : cos이걸 만드ㄹ어줘야함. id, pw 정상적으로 들어와서 로그인이 완료되면 토큰을 만들어주고 그걸 응답을 해준다.
        //요청할때 마다 header에  Authorization에 Value값으로 토큰을 가지고 오겠죠?
        //그때 토큰이 넘어오면 이 토큰이 내가 만든 토큰이 맞는지만 검증만 하면 됨.(RSA, HS256)
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        //토큰 : 코스 가 넘어오면 인증 하고 아니면 진입도 못하게 한다.
        if(req.getMethod().equals("POST")){
            String headerAuth = req.getHeader("Authorization");
            System.out.println(headerAuth);

            if(headerAuth.equals("cos")){
                filterChain.doFilter(req, res);
            }else{
                System.out.println("인증안됨!!!");
            }
        }
    }
}
