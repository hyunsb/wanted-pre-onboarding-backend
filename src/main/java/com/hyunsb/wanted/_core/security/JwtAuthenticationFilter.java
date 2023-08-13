package com.hyunsb.wanted._core.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hyunsb.wanted._core.error.ErrorMessage;
import com.hyunsb.wanted._core.util.FilterResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("[JWT 인증 필터] 작동");
        String token = request.getHeader(JwtProvider.HEADER);

        if (token == null) {
            log.info("[JWT 인증 필터] 토큰 없음");
            chain.doFilter(request, response);
            return;
        }

        try {
            log.info("[JWT 인증 필터] 토큰 확인 완료");
            DecodedJWT decodedJWT = JwtProvider.verify(token);
            Long userId = getUserIdFromToken(decodedJWT);

            request.setAttribute(JwtProvider.REQUEST, userId);
            chain.doFilter(request, response);

        } catch (SignatureVerificationException sve) {
            FilterResponse.unAuthorized(response, ErrorMessage.TOKEN_UN_AUTHORIZED);
        } catch (TokenExpiredException tee) {
            FilterResponse.unAuthorized(response, ErrorMessage.TOKEN_EXPIRED);
        } catch (JWTDecodeException exception) {
            FilterResponse.unAuthorized(response, ErrorMessage.TOKEN_VERIFICATION_FAILED);
        }
    }

    private Long getUserIdFromToken(DecodedJWT decodedJWT) {
        return decodedJWT.getClaim("id").asLong();
    }
}
