package com.sparta.springtrello.config;

import com.sparta.springtrello.domain.member.entity.Member;
import com.sparta.springtrello.domain.member.entity.MemberRole;
import com.sparta.springtrello.domain.user.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final JwtSecurityFilter jwtSecurityFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtSecurityFilter, SecurityContextHolderAwareRequestFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/sign-up", "/users/sign-in").permitAll()
                        .requestMatchers("/workspaces").hasAuthority("ROLE_ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/**").hasAnyAuthority("ROLE_USER","ROLE_ADMIN","ROLE_CREATOR","ROLE_READER") // GET 요청에 ROLE_READER 권한만 허용
//                        .requestMatchers(HttpMethod.POST, "/**").hasAnyAuthority("ROLE_ADMIN","ROLE_CREATOR")// ROLE_CREATOR와 ROLE_ADMIN만 POST 요청 허용
//                        .requestMatchers(HttpMethod.PUT, "/**").hasAnyAuthority( "ROLE_ADMIN","ROLE_CREATOR") // ROLE_CREATOR와 ROLE_ADMIN만 PUT 요청 허용
//                        .requestMatchers(HttpMethod.DELETE, "/**").hasAnyAuthority( "ROLE_ADMIN","ROLE_CREATOR") // ROLE_CREATOR와 ROLE_ADMIN만 DELETE 요청 허용
                        .anyRequest().authenticated()
                )
                .build();
    }
}