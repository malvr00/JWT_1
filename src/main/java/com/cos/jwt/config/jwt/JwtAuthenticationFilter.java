package com.cos.jwt.config.jwt;

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
import java.io.BufferedReader;
import java.io.IOException;
import java.security.Principal;

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

        try {
//            BufferedReader br = request.getReader();
//            String input = null;
//            while((input = br.readLine()) != null){
//                System.out.println(input);
//            }
            ObjectMapper object = new ObjectMapper();
            User user = object.readValue(request.getInputStream(), User.class);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            // PrincipalServer 에 loadUserByUsername() 함수가 실행 됨
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

            System.out.println("principalDetails.getUsername() = " + principalDetails.getUser().getUsername());
            System.out.println("User = " + user);
            System.out.println("input String=" + request.getInputStream().toString());

            // authentication 객체가 session 영역에 임시로 저장해야 되는데 그 방법이 return.
            // 리턴의 이뉴는 권한 관리를 security 가 대신 해주기 때문에
            // 굳이 JWT 토큰을 사용하면서 세션을 만들 이유가 없음. 하지만 권한 처리때문에 session 넣어서 사용.
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // attemptAuthentication 실행 후 인증이 정상적으로 되었으면, successfulAuthentication 함수가 실행 됨.
    // successfulAuthentication 에서 JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response 해주면 됨.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("JwtAuthenticationFilter.successfulAuthentication 실행됨. ** 인증 완료 **");
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
