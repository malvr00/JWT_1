package com.cos.jwt.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter1 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // ex) 토큰 : cos 이걸 만들어줘야 함. id, pw 정상적으로 들어와서 로그인이 완료 되면 토큰을 만들어주고 그걸 응답
        // 요청 할 때 마다 header에 Authorization에 value 값 으로 토큰을 가지고 와야 함.
        // 그때 토큰이 넘어오면 이 토큰이 내가 만든 토큰이 맞는지만 검증만 하면 됨. (검증 : RSA, HS256 )
        if(req.getMethod().equals("POST")){
            System.out.println("POST 요청");
            String headerAuth = req.getHeader("Authorization");
            System.out.println("MyFilter1.doFilter");
            System.out.println(headerAuth);
            if(headerAuth.equals("cos")){
                chain.doFilter(request, response);
            } else {
                PrintWriter out = res.getWriter();
                out.println("인증안됨;");
            }
        }

    }
}
