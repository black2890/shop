package com.shop.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

/*  @Configuration : 해당 클래스가 Application 설정을 담당함을 나타냄
    빈(Bean)정의, property 설정, 컴포넌트 스캔과 같은 설정 작업을 수행
    XML 파일을 대체하여 JAVA 코드로 Application 설정을 정의
*/
@Configuration
// filter 체인에 필요없는 체인을 비활성화 시키거나 특정 필터를 추가한다거나 특정 디폴드 값을 바꾸거나 할때 목적
//보안 구성을 정의하고 필터를 추가하며, 인증 및 권한 부여를 구성할 수 있음
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.formLogin((it) -> it
                .loginPage("/members/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .failureUrl("/members/login/error")
        );

        http.logout((it) -> it
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/") //메인화면으로 이동
        );

        // ? : 한글자, ?? : 두글자 , * : 0~n개의 글자, /** -> /awevfsa/asvfdsa/asvddvfs ㄱㄴ
        /*

         */
        http.authorizeHttpRequests((req) -> {req
                    .requestMatchers(antMatcher("/")).permitAll()
                    .requestMatchers(antMatcher("/members/**")).permitAll()
                    .requestMatchers(antMatcher("/item/**")).permitAll()
                    .requestMatchers(antMatcher("/images/**")).permitAll()
                .requestMatchers(antMatcher("/favicon.ico/")).permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated();
        });

//        http.requestCache( (it) -> it.requestCache(cache));

        http.exceptionHandling((it) -> it
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint("/members/login"))
        );
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()
                );
    }
}
