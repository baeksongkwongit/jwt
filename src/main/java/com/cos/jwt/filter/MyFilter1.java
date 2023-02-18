package com.cos.jwt.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyFilter1 implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

//        HttpServletRequest req = (HttpServletRequest) servletRequest;
//        HttpServletResponse res = (HttpServletResponse) servletResponse;
//    //토큰 : 코스 가 넘어오면 인증 하고 아니면 진입도 못하게 한다.
//        if(req.getMethod().equals("POST")){
//            String headerAuth = req.getHeader("Authorization");
//            System.out.println(headerAuth);
//
//            if(headerAuth.equals("cos")){
//                filterChain.doFilter(req, res);
//
//            }else{
//                System.out.println("인증안됨!!!");
//
//            }
//        }
        System.out.println("필터1");
        filterChain.doFilter(servletRequest, servletResponse);

    }
}
