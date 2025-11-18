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

Entity 구성

### [Article.java](https://github.com/ChyeonJ/blog_example/blob/main/src/main/java/chyeonj/blogexample/blogexam/domain/Article.java)

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

### [BlogRepository.java](https://github.com/ChyeonJ/blog_example/blob/main/src/main/java/chyeonj/blogexample/repository/BlogRepository.java)

BlogRepositoty 인터페이스 생성 DB의 CRUD 해결을 위한 다리

### [AddArticleRequest.java](https://github.com/ChyeonJ/blog_example/blob/main/src/main/java/chyeonj/blogexample/DTO/AddArticleRequest.java)

// 저장 (.save)할 때 사용하기 위한 DTO(데이터 전달용 Controller <-> Service)

@AllArgsConstructor //클래스 안에 있는 모든 필드를 한 번에 넣어 객체로 만들 수 있게 해줌

### [BlogService.java](https://github.com/ChyeonJ/blog_example/blob/main/src/main/java/chyeonj/blogexample/Service/BlogService.java)

@RequiredArgsConstructor //final, @NonNull 필드만 받는 필수값만 넣는 생성자 자동 생성 애너테이션



