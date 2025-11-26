# blog_example

---------------------------------------------------------------------------

# 프로젝트 프로그램
- 개발툴 : InteliJ, PostMan, MySQL, GitHub

- 개발 언어 : JAVA, Spring, Thymeleaf

- AWS

# 버전

- jdk SE 17.02
- Springboot 3.0.2
- spring-management 1.1.0
- MySQL - 8.0.44

# 개발 로드맵

1. 블로그 API 구현 CRUD (RESTFul API)
2. 블로그 화면 구성하기 (Thymeleaf)
3. 로그인, 로그아웃, 회원가입 구현
   - Spring Security, JWT, OAuth2
4. AWS 배포 (일래스틱 빈스토크)

---------------------------------------------------------------------------

## 1. 블로그 API 구현 CRUD (RESTFul API)

API와 REST API
API : 네트워크에서 API 프로그램 간에 상호작용하기 위한 매개체

ex) 손님(클라이언트) <-> 점원(API) <-> 주방(서버)

클라이언트 : "구글 메인화면 보여줘" 요청

API : 요청을 받아서 서버에게 전달

서버 : API가 준 요청을 처리해 결과물을 만들고 이것을 다시 API로 전달

REST API
웹의 장점을 최대한 활용하는 API : 자원을 이름으로 구분해 자원의 상태를 주고받는 API 방식

URL 설계 방식

REST API 특징 : 서버/ 클라이언트 구조, 무상태, 캐시 처리 가능, 계층화, 인터페이스 일관성과 같은 특징이 존재

REST API 장단점

장점

URL만 보고도 무슨 행동을 하는 API인지 명확하게 알 수 있다
무상태인 특징 덕분에 클라이언트와 서버의 역할이 명확하게 분리
HTTP 표준을 사용하는 모든 플랫폼에서 사용할 수 있다.
주소와 메서드만 보고 요청의 내용을 파악할 수 잇다는 강력한 장점이 있다.
단점

HTTP 메서드 - GET, POST 같은 방식의 개수에 제한이 있다.
설계를 하기 위해 공식적으로 제공되는 표준 규약이 없다.
REST API 사용 규칙

URL에는 동사를 쓰지 않고, 자원을 표시

ex) 맞는 표현 : /students/1 틀린표현 : /get-student?student_id=1 틀린 표현의 경우 실제 개발 상황에서 누군가는 get, 누군가는 show 방식의 url을 설곟할 수 있기 때문

동사는 HTTP 메서드로

ex)

id가 1인 블로그 글을 조회하는 API : GET /articles/1
블로그 글을 추가하는 API : POST /articles
블로그 글을 수정하는 API : PUT /articles/1
블로그 글을 삭제하는 API : DELETE /articles/1

---------------------------------------------------------------------------

### ![Article.java](https://github.com/ChyeonJ/blog_example/blob/main/Step.1/Article.java.png)

@Getter //클래스의 모든 필드에 대한 Getter 메서드를 자동 생성 

@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자를 만들 때 JPA는 사용할 수 있지만 외부 개발자는 생성자를 새롭게 생성하지 못하게 막는 애너테이션

@GeneratedValue(strategy = GenerationType.IDENTITY) //기본키 1씩 증가 Auto_increment

컬럼 : id, title, content

@Builder 빌더 패턴 사용하여 객체 생성

@Bulider 사용 이유 
1. 가독성 좋음
2. 필드를 선택적으로 넣기 쉬움
3. 객체를 불변하게 만들기 좋음
4. 생성자 오버로딩에 용이함

ex) User user = new User("홍길동", 25, "서울", null, true, "010-1234-5678"); 

- 필드가 많은 객체 생성 시 어떤 값이 어떤 필드인지 구분이 힘듦 홍길동이 name인지? content인지?

### ![BlogRepository.java](https://github.com/ChyeonJ/blog_example/blob/main/Step.1/BlogRepository.java.png)

BlogRepositoty 인터페이스 생성 DB의 CRUD 해결을 위한 다리

### ![AddArticleRequest.java](https://github.com/ChyeonJ/blog_example/blob/main/Step.1/AddArticleRequest.java.png)

// 저장 (.save)할 때 사용하기 위한 DTO(데이터 전달용 Controller <-> Service)

@AllArgsConstructor //클래스 안에 있는 모든 필드를 한 번에 넣어 객체로 만들 수 있게 해줌

### ![ArticleResponse.java](https://github.com/ChyeonJ/blog_example/blob/main/Step.1/ArticleResponse.java.png)

// 조회 (.findAll, .fimdById(),) 클라이언트에게 전달할 블로그 글 데이터를 담는 DTO

AddArticleRequest랑 다르게 article.getTitle();로 .get 메서드 사용

### ![UpdateArticleRequest.java](https://github.com/ChyeonJ/blog_example/blob/main/Step.1/UpdateArticleRequest.java.png)

// 수정 (.update()) 블로그 글 수정 요청 데이터를 담는 DTO

// 생성자 자동생성 애너테이션 사용

@NoArgsConstructor(access = AccessLevel.PROTECTED)

// 모든 필드를 한 번에 넣을 수 있음 AddArticleRequest에서도 사용

@AllArgsConstructor

### ![BlogService.java](https://github.com/ChyeonJ/blog_example/blob/main/Step.1/BlogService.java.png)

@Service 스프링 빈으로 등록되어 다른 클래스 주입가능

@RequiredArgsConstructor //final, @NonNull 필드만 받는 필수값만 넣는 생성자 자동 생성 애너테이션

private final BlogRepository blogRepository; // 의존성 주입 DB와 통신 생성자 자동생성 애너테이션 덕분에 안전하게 주입 가능

주요 메서드
1. save(AddArticleRequest request) DTO를 엔티티로 변환하여 DB에 저장, 저장된 엔티티 반환
2. findById(long id) ID로 블로그 글 조회, 존재 하지 않으면(orElseThrow)로 IllegalArgumentException 에러 발생
3. findAll() 블로그 글 전체 조회
4. delete(long id) ID로 블로그 글 삭제
5. update(long id, UpdateArticleRequest request) 글 수정 (@Transactional 트랜잭션) 기존 엔티티 조회 후 수정 메서드 호출

### ![BlogApiController.java](https://github.com/ChyeonJ/blog_example/blob/main/Step.1/BlogApiController.java.png)

주요 특징

@RestController : HTTP Response Body에 데이터를 JSON 형식으로 변환

@RequiredArgsConstructor : final 필드 자동 주입, 위에서 쓰였듯이 @NotNun 비어있으면 안된다도 포함

@ResponseEntity : HTTP 상태 코드, 응답 본문을 반환 .ok, .body, .bulid 사용 때 나옴

#### API 목록

1. 블로그 글 생성

   - @PostMapping 사용 /api/articles 경로 지정
   - @RequestBody: AddArticleRequest //클라이언트가 요청할 때 본문(Body)에 담긴 json 등 형태의 데이터를 자바 객체로 변환
   - ResponseEntity: Article 객체 생성
   - HTTP 상태코드 : 201 Created

2. 단건 조회 (Article Entity의 ID조회)
    
   - @GettMapping 사용 /api/articles/{id}
   - 특정 ID 블로그 글 조회
   - @PathVarialbe : 링크 내의 {id} long id 담아낸다.
   - ReponseBody : ArticleResponse DTO 담기
   - HTTP 상태코드 : 201 Created

3. 전체 조회

   - @GetMapping 사용 /api/articles/all
   - ResponseEntity : List<ArticlesResponse> DTO 담고 LIST화한다 전체를 가져와야 하기 때문
   - HTTP 상태코드 : 201 Created

4. 블로그 글 삭제

   - DeleteMapping 사용 /api/articles/{id}
   - @PathVarialbe : 링크 내의 {id} long id 담는다
   - ResponseEntity : <Void> .body가 없어 리턴 값을 반환하지 않을 때 사용
   - HTTP 상태코드 : 200 ok

5. 블로그 글 수정

   - @PutMapping 사용 /api/articles/{id}
   - @PathVarialbe : 링크 내의 {id} long id 담는다
   - @RequestBody: AddArticleRequest //클라이언트가 요청할 때 본문(Body)에 담긴 json 등 형태의 데이터를 자바 객체로 변환
   - ResponseEntity : 수정된 Article 엔티티 반환
   - HTTP 상태코드 : 200 ok

---------------------------------------------------------------------------

## 2. 블로그 화면 구성하기 (Thymeleaf)

타임리프 (Thymeleaf) : 템플릿 엔진 스프링 서버에서 데이터를 받아 우리가 보는 웹(HTML)에 데이터를 넣어 보여주는 도구

 Build.gradle : implementation 'org.springframework.boot:spring-boot-starter-thymeleaf' //타임리프 (Thymeleaf)

타임리프를 사용하기에 앞서 현업에 필요한 기술은 아니라고 판단이 들었다.

하지만 프론트엔드 지식이 많이 부족하기에 

추후에 업그레이드 염두를 두고 타임리프를 사용하요 REST API를 사용하지 못하는 코드 작성을 하여 CRUD 구현만 진행했다.

---------------------------------------------------------------------------

# 3. 로그인, 로그아웃 구현하기 (spring security)

스프링 시큐리티 : 스프링 기반의 애플리케이션 보안(인증, 인가, 권한)을 담당하는 스프링 하위 프레임워크
1. 보안 관련 옵션 다수 제공
2. CSRF 공격 : 사용자의 권한을 가지고 특정 동작을 수행하도록 유도하는 공격
3. 세션 고정 공격 : 사용자의 인증 정보를 탈취하거나 변조하는 공격을 말함
4. 3,4번의 공격들을 방어 해주고 요청 헤도ㅓ도 보안 처리를 해주므로 보안 관련 개발 부담을 크게 줄여줌

- 인증 : 인증은 사용자의 신원을 입증하는 과정 ex) 사이트에 로그인을할 때 누구인지 확인하는 과정
- 인가 : 사이트의 특정 부분에 접근할 수 있는지 권한을 확인하는 작업 ex) 관리자는 관리자 페이지만 사용자는 사용자 페이지만
 
Build.gradle 의존성 추가

    //스프링 시큐리티
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity6'

    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.springframework.security:spring-security-test'

### ![스프링 시큐리티 필터](https://github.com/ChyeonJ/blog_example/blob/main/Step.2/Filter.png)

- [이미지 출처](https://lalwr.blogspot.com/2018/06/spring-security.html)

SecurityContextPersistenceFilter부터 시작하여 아래로 내려가며 FilterSecurityInterceptor까지 순서대로 필터를 거침

중요한 필터
1. UsernamePasswordAuthenticationFilter : 아이디와 패스워드가 넘어오면 인증 요청을 위임하는 인증 관리자 역할
2. FilterSecurityInterceptor : 권한 부여 처리를 위임해 접근 제어 결정을 쉽게 하는 접근 결정 관리자 역할

각 필터의 설명

1. SecurityContextPersistenceFilter : SecurityContext(접근 주체와 인증에 대한 정보를 담고 있는 객체)를 가져오거나 저장하는 역할
2. LogoutFilter : 설정된 로그아웃 URL로 오는 요청을 확인해 해당 사용자를 로그아웃 처리
3. ***UsernamePasswordAuthenticationFilter*** : 인증이 성공하면 AuthenticationSuccessHandler를 인증 실패하면 AuthenticationFailureGandler 실행
4. DefaultLoginPageGeneratingFilter : 사용자가 로그인페이지를 따로 저장하지 않았을 때 기본으로 설정하는 로그인 페이지 필터
5. BasicAuthenticationFilter : 요청 헤더에 있는 아이디와 패스워드를 파시항해서 인증 요청 인증이 성공하면 AuthenticationSuccessHandler를 인증 실패하면 AuthenticationFailureGandler 실행
6. RequestCacheAwareFilter : 로그인 성공 후, 관련 있는 캐시 요청이 있는지 확인하고 캐시 요청을 처리 로그인 하지 않았던 페이지를 기억했다가 로그인하면 로그인하지 않은 페이지로 이동
7. SecurityContextHolderAwareRequestFilter : HttpServeltRequest 정보를 감싸, 필터 체인 상의 다음 필터들에게 부가 정보 제공
8. AnonymousAuthenticationFilter : 필터가 호출되는 시점까지 인증되지 않았다면 익명 사용자 객체인 AnonymousAuthentication을 만들어 SecurityContext로 삽입
9. SessionManagementFilter : 인증된 사용자와 관련된 세션 관련 작업을 진행, 세션 변조 방지 전략을 설정하고, 유효하지 않은 세션 삭제, 세션 생성 전략 세우는 등 작업 처리
10. ExceptionTranslationFilter : 요청을 처리하는 중에 발생할 수 있는 예외를 위임하거나 전달합니다.
11. ***FilterSecurityInterceptor*** : 이 과정에서는 이미 사용자가 인증되어 유효한 사용자인지 알 수 있음. 인가 관련 설정

### ![스프링 시큐리티 폼 로그인](https://github.com/ChyeonJ/blog_example/blob/main/Step.2/%ED%8F%BC%20%EB%A1%9C%EA%B7%B8%EC%9D%B8%20%EC%9D%B8%EC%A6%9D.png)

- [이미지 출처](https://velog.io/@solchan/Spring-Security-%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0-%EA%B8%B0%EB%B3%B8-%EA%B0%9C%EB%85%90%EA%B3%BC-%EC%9D%B8%EC%A6%9DForm-%ED%9D%90%EB%A6%84)

인증 흐름
1. 사용자가 폼에 아이디와 패스워드를 입력 ***HTTPServletRequest***에 아이디 비밀번호 정보 전달
2. 이때 ***AuthenticationFilter***가 넘어온 아이디와 비밀번호의 유효성 검사
3. 유효성 검사 종료 후 ***UsernamePassswordAuthenticationToken***을 만들어 넘김
4. 전달받은 인증용 객체 ***UsernamePassswordAuthenticationToken***을 ***AuthenticationManager***에 전송
5. ***UsernamePassswordAuthenticationToken***을 ***AuthenticationProvider***에 전송
6. 사용자 아이디를 UserDetailService에 보내고 사용자 아이디로 찾은 사용자 정보를 UserDetails객체로 만들어 ***AuthenticationProvider*** 전달
7. DB에 있는 사용자 정보 가져옴
8. 입력 정보와 UserDetails의 정보를 비교해 실제 인증처리
9. 8 ~ 10 인증 완료 시 ***SecurityContextHolder***에 Authentication을 저장
10. 인증이 성공하면 ***AuthenticationSuccessHandler***를 인증 실패하면 ***AuthenticationFailureGandler*** 실행

---------------------------------------------------------------------------

### ![User.java](https://github.com/ChyeonJ/blog_example/blob/main/Step.2/User.png)

- User 클래스가 상속한 implements UserDetails는 스프링 시큐리티에서 사용자의 인증 정보를 담아두는 인터페이스

오버라이드 메서드 별 반환 타입 및 설명
1. getAuthorities : 반환타입 Collection<? extends GrantedAuthority> : 사용자가 가지고 있는 권한의 목록 반환
    - Collection : List, Set등 여러개의 권한 담기 가능 / <? extends GrantedAuthority> : GrantedAutority를 구현한 어떤 타입이든 가능
    - 사용자가 가진 권한을 스프링 시큐리티에 알려주는 역할
2. getUserName : 반환타입 String : 사용자를 식별할 수 있는 사용자 이름 반환 / 사용자는 unique해야 하기에 unique한 이메일 반환
3. getPassword : 반환타임 String : 사용자 비밀번호를 반환 / 비밀번호는 암호화해서 저장
4. isAccountNotExpired : 반환타입 Boolean : 계정이 만료 되었는지 확인 / 만료 하지 않았다면 true(1) 반환
5. isAccountNonLocked : 반환타입 Boolean : 계정이 잠금 되었는지 확인 / 잠금 되지 않으면 true(1) 반환
6. isCredentialsNonExpired() : 반환타입 Boolean : 비밀번호 만료 확인 / 만료 하지 않았다면 true(1) 반환
7. isEnabled() : 반환타입 Boolean : 계정이 사용 가능한지 확인하는 메서드 / 사용 가능하다면 true(1) 반환

### ![UserRepository.java](https://github.com/ChyeonJ/blog_example/blob/main/Step.2/UserRepository.png)

- 이메일로 사용자 식별이 가능한 상태, 사용자 정보를 가져오기 위해서 스프링 시큐리티가 이메일을 전달 받아야함
- 스프링 데이터(JPA)는 규칙에 맞춰 선언하면 쿼리를 생성해줌 
***findByEmail()***

select * FROM users

Where email = #{email}

### ![UserDetailService.java](https://github.com/ChyeonJ/blog_example/blob/main/Step.2/UserDetailService.png)

- 로그인을 진행할 때 사용자 정보를 가져오는 코드
- loadUserByUsername()메서드를 오버라이딩하여 사용자 정보를 가져오는 로직 완성

### ![WebSecurityConfig.java](https://github.com/ChyeonJ/blog_example/blob/main/Step.2/config%2CWebSecurityConfig.png)

- 프로젝트 전체의 스프링 시큐리티 설정을 담당하는 보안 설정 클래스 로그인/로그아웃, 인증이 필요한 페이지, 인증이 필요 없는 페이지 관리

- @Configuration : 스프링 설정 파일이라는 의미

***WebSecurityCustomizer***
1. /staitc/** 경로는 css,js 이미지 같은 정적 파일 위하는 곳
2. 정적 리소스는 로그인 여부와 상관없이 접근 가능해야 하므로 스프링 시큐리티 모든 기능을 사용하지 않게함

***SecurityFilterChain***
1. 인증이 필요 없는 url
    - /login 로그인 페이지
    - /signup 회원가입 페이지
    - /user 회원가입 요청 처리
    - /.well-known/** - 브라우저가 자동으로 요청하는 특별한 경로(본인은 로그인 후 화이트 라벨 400코드가 지속 발생하여 추가한 코드)
2. 그외 나머지 요청은 .anyRequest().authenticated() 로그인 해여하만 접근가능
3. 로그인 설정
    - .formLogin().loginPage("/login").defaultSuccessUrl("/articles")
    - 커스텀 로그인 페이지 사용 .loginPage("/login")
    - 로그인 설공 시 .defaultSuccessUrl("/articles") 이동
4. 로그아웃 설정
   - .logout().logoutSuccessUrl("/login").invalidateHttpSession(true)
   - 로그 아웃 후 .logoutSuccessUrl("/login") 이동
   - .invalidateHttpSession(true) 세션 완전 삭제
5. csrf 비활성화 / 개발단계 이기에 False
    - 개발 중에는 폼 제출 편의를 위해 CSRF 체크 비활성화

***AuthenticationManager***
1. 로그인 요청 시 사용자의 이메일/비밀번호를 확인하는 관리 객체
2. 실제 사용자 정보를 가져오는 서비스 : UserDetailService
3. 비밀번호 암호화 방식 : BCryptPasswordEncoder

-> 이메일 기반 로그인 + 암호화된 비밀번호 검사 수행

***BcryptPasswordEncoder***
1. 사용자의 비밀번호는 DB에 절대 평문 저장하면 안됨
2. BCrypt는 스프링 시큐리티에서 권장하는 강력한 암호화 방식
3. 회원가입 시 비밀번호 암호화, 로구인 시 암호 비교에 사용

### ![AddUserRequest.java](https://github.com/ChyeonJ/blog_example/blob/main/Step.2/AddUserRequest.png)

- 사용자 정보를 담고 있는 객체 생성

### ![UserService.java](https://github.com/ChyeonJ/blog_example/blob/main/Step.2/UserService.png)

- 회원 가입 기능을 담당
1. save() 메서드 회원가입 화면에서 전달된 요청 DTO(AddUserRequest)를 받아 실제 USer 엔티티로 변환후 DB에 저장
2. .password(bCryPasswordEncoder.encode(dto.getPassword()))
 -  비밀번호 암호화 저장 하는 핵심 코드

### ![UserApiController.java](https://github.com/ChyeonJ/blog_example/blob/main/Step.2/UserApiController.png)

- /user로 접속하면 회원가입 메서드를 호출한 후 회원가입이 완료 되면 강제로 /login으로 이동하게 함
- /logout url(GET) 받아오면 로그아웃 메서드 실행 
- 현재 인증 된 SecurityContextHolder.getContext().getAuthentication() (권한, 세션 정보등) 가져와서
- SecurityContextHolder에 저장
- new SecurityContextLogoutHandler().logout(request, response, authentication); 핸들러를 통해 실질적 로그아웃 코드 실행
- 세션, 인증정보, 쿠키, 내부 Authentication 객체 null처리 실행
- 다시 로그인 창으로 이동하게 반환함

### ![UserViewController.java](https://github.com/ChyeonJ/blog_example/blob/main/Step.2/UserViewController.png)

1. /login url 접속 시 login.html 반환
2. /signup url 접속 시 signup.html 반환

---------------------------------------------------------------------------

# 3-1. JWT로 로그인 로그아웃 구현하기

### 사전 지식

- 토큰 기반 인증 : 서버에서 클라이언트를 구분하기 위한 유일한 값 
 1. 클라이언트는 이 토큰을 지니고 있음
 2. 여러 요청과 함께 토큰을 신청
 3. 서버는 토큰만 보고 유효한 사용자인지 검증

![토큰 기반 인증]()

순서
1. 로그인 요청 (클라이언트 -> 서버) : 클라이언트가 아이디와 비밀번호를 서버에게 전달 인증요청
2. 토큰 생성 후 응답 (서버 -> 클라이언트) : 서버는 아이디와 비밀번호를 확인 후 유효한 사용자인지 검증
3. 토큰 저장 : 클라이언트는 서버가 준 토큰을 저장
4. 토큰 정보와 함께 요청 (클라이언트 -> 서버) : 인증이 필요한 API를 사용할 때 토큰을 함께 보냄
5. 토큰 검증 : 서버는 토큰이 유효한지 검증
6. 응답 (서버 -> 클라이언트) : 토큰이 유효하다만 클라이언트가 요청한 내용 처리

토큰 기반 인증 특징
- 무상태성
1. 토큰이 서버가 아닌 클라이언트가 지니고 있어 서버에 저장할 필요 X (스케일 부담이 없음)
2. 사용자 인증 상태를 유지하면서 이휴 요청을 처리 해야함 ***상태관리***
3. 클라이언트의 인증 정보를 저장하거나 유지하지 않아도 되기 때문에 완전한 ***무상태***로 효율적인 검증 가능
- 확장성
1. 서버를 확장할 때 상태 관리를 신경 쓸 필요가 없음
2. 토큰을 가지는 주체가 클라이언트이기에 하나의 토큰으로 여러개의 서버에 요청을 보낼 수 있음
3. 토큰 기반 인증을 사용하는 다른 시스템에 접근해 로그인 방식 확장 용이, 다른 서비스에 권한 공유 가능
- 무결성
1. 토큰 방식은 HMAC(hash-head message authentication)기법
2. 토큰을 발급한 후에 토큰 정보를 변경하는 행위 X
3. 누군가 토큰을 한글자라도 변경하면 서버에서는 유효하지 않은 토큰이라는 판단을 내리게 됨

### JWT

- JWT 구조 : 헤더, 내용, 서명(해싱)

헤더에는 토큰의 타입과 해싱 알고리즘을 지정하는 정보들 담음

ex)

{ "typ" : "JWT" //JWT 토큰 , "alg" : "HS256" //HS256 해싱 알고리즘}

내용에는 토큰과 관련된 정보를 담음. / 내용의 한 덩어리를 "클레임"이라고 부름

클레임은 키값의 한 쌍으로 이루어짐

***클레임***
1. 등록된 클레임
- JWT에서 정의해 둔 공통 표준 키
- iss : 토큰 발급자
- sub : 토큰 제목
- iat : 토큰이 발급된 시간
- exp : 토큰 만료시간
- jti : 일회용 토큰

2. 공개된 클레임
- JWT를 사용하는 여러 서비스에서 공통으로 사용하는 키
- email
- name
- given_name
- family_name

3. 비공개 클레임
- 서비스에서 마음대로 만든 키
- role
- userId
- premiumPlan = true
- department = "dev"





