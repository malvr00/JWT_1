package com.cos.jwt.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 있음.
// 동작 => /login 요청해서 username, password 전송하면 (POST)
// UsernamePasswordAuthenticationFilter 동작 함.
// 현재는 http.formLogin.disable() 했음. 그래서 필터로 재사용..?
// ** 버전이 다른관계로 다른 방법 사용. SecurityConfig 에 apply 참조 ***
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    // /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter 로그인 시도중");

        // 1. username, password 받아서
        // 2. authenticationManager 로 로그인 시도
        // 3. 하면 PrincipalDetailsServer 가 호출 되고 loadUserByUsername() 함수 실행.
        // 4. PrincipalDetails 를 세션에 담고 ( 세션에 담는 이유는 안담게 되면 권한 관리가 되지 않음 )
        // 5. JWT 토큰을 만들어서 응답해 주면 됨
        return super.attemptAuthentication(request, response);
    }
}
