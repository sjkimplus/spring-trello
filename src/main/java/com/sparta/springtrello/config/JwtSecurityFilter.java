package com.sparta.springtrello.config;

import com.sparta.springtrello.common.exception.ErrorCode;
import com.sparta.springtrello.common.exception.HotSixException;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import com.sparta.springtrello.domain.user.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtSecurityFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpRequest,
            @NonNull HttpServletResponse httpResponse,
            @NonNull FilterChain chain
    ) throws ServletException, IOException {
        String authorizationHeader = httpRequest.getHeader("Authorization");
        String url = httpRequest.getRequestURI();

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ") && !url.startsWith("/users/sign-up") && !url.startsWith("/users/sign-in")) {
            String jwt = jwtUtil.substringToken(authorizationHeader);
            try {
                Claims claims = jwtUtil.extractClaims(jwt);
                Long userId = Long.parseLong(claims.getSubject());
                String email = claims.get("email", String.class);
                String nickname = claims.get("nickname", String.class);
                UserRole userRole = UserRole.of(claims.get("userRole", String.class));

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    AuthUser authUser = new AuthUser(userId, email, userRole);

                    JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (SecurityException | MalformedJwtException e) {
                throw new HotSixException(ErrorCode.JWT_INVALID);
//                log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.", e);
//                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않는 JWT 서명입니다.");
            } catch (ExpiredJwtException e) {
                throw new HotSixException(ErrorCode.JWT_EXPIRED);
//                log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
//                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 토큰입니다.");
            } catch (UnsupportedJwtException e) {
                throw new HotSixException(ErrorCode.JWT_TYPE_ERROR);
//                log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
//                httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");
            } catch (Exception e) {
                throw new HotSixException(ErrorCode.INTERNAL_SERVER_ERROR);
//                log.error("Internal server error", e);
//                httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
        chain.doFilter(httpRequest, httpResponse);
    }
}