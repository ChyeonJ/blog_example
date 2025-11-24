package chyeonj.blogexample.config;

import chyeonj.blogexample.Service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private final UserDetailService userService;

    //스프링 시큐리티 기능 비활성화
    // 스프링 시큐리티의 모든 기능을 사용하지 않게 설정하는 코드, 인증, 인가 서비스를 모든 곳에 적용하지 않음 / 정적 리소스 저장
    @Bean
    public WebSecurityCustomizer configure() {
        //정적 리소스만 스프링 시큐리티 사용을 비활성화
        return (web) -> web.ignoring()
                // .requestMatchers(toH2Console())  MySQL 사용하므로 사용하지 않음
                .requestMatchers("/static/**");
    }

    //특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests() //인증, 인가 설정
                .requestMatchers("/login", "/signup", "/user").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()// 폼 기반 로그인 설정
                .loginPage("/login")    //로그인 페이지 경로 설정
                .defaultSuccessUrl("/articles") //로그인이 완료 되었을 때 이동할 경로 설정
                .and()
                .logout()// 로그아웃 설정
                .logoutSuccessUrl("/login") //로그아웃 완료 되었을 때 경로 설정
                .invalidateHttpSession(true)//로그아웃 이후 세션 전체삭제 여부 설정
                .and()
                .csrf().disable() //csrf 비활성화 공격 방지하기 위해 활성화가 맞지만 개발단계 이기에 잠시 비활성화
                .build();
    }

    //인증 관리자 관련 설정 사용자 정보를 가져올 서비스를 재정의, 인증 방법, 예를들어 LDAP, JDBC 기반 인증 설정할 때 사용
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                //사용자 정보 서비스 설정
                .userDetailsService(userService)// 사용자 정보를 가져올 서비스 설정 * 이때 설정하는 서비스 클래스는 반드시 UserDetailsService 상속 받은 클래스
                .passwordEncoder(bCryptPasswordEncoder)// 비밀번호 암호화하기 위한 인코더를 설정
                .and()
                .build();
    }
    
    // 패스워드 인코더로 사용할 빈 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}