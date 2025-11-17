package chyeonj.blogexample.controller;


import chyeonj.blogexample.DTO.AddArticleRequest;
import chyeonj.blogexample.DTO.ArticleResponse;
import chyeonj.blogexample.Service.BlogService;
import chyeonj.blogexample.blogexam.domain.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController // HTTP Response Body에 객체 데이터를 JSON 형식으로 반환하는 컨트롤러
public class BlogApiController {

    private final BlogService blogService;

    //HTTP 메서드가 POST일 때 전달받은 URL과 동일하면 메서드로 매핑
    @PostMapping("/api/articles")
    //@RequestBody로 요청 본문 값 매핑
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request){
        Article savedArticle = blogService.save(request);

        //요청한 자원이 성공적으로 생성 되었으며 저장된 블로그 글 정보를 응답 객체에 담아 전송
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    @GetMapping("/{id}")
    public Article findByIdArticle(@PathVariable long id){
        return blogService.findById(id);
    }

    // Article 엔티티 조화한뒤 -> ArtcleResponse DTO로 변환하여 -> ResponseEntity로 감싸서 반환하는 전체 조회 API
    @GetMapping("/api/articles/all")
    public ResponseEntity<List<ArticleResponse>> findAllArticle(){ //ResponseEntity 클라이언트의 HTTP 응답을 보내기 위함
        List<ArticleResponse> articles = blogService.findAll()
                .stream()   //리스트를 스트림 형태로 변환 요소마다 변환 작업을 수핼할 수 있게함.
                .map(ArticleResponse::new)  // 리스트에 들어 있는 각각의 Article 엔티티를 ArticleResponse DTO 변환
                                            // 생성자 매핑 방식 -> new ArticleResponse(article)과 같은 의미
                .toList(); // 스트림으로 변환된 DTO들을 다시 리스트로 모음

        return ResponseEntity.ok()  //HTTP 200 OK 상태를 가진 ResponseEntity를 뜻함
                .body(articles);    //DTO리스트를 응답의 body에 넣어 클라이언트로 반환
    }

    // 단적 조회
    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id){
        Article article  = blogService.findById(id);

        return ResponseEntity.ok().body(new ArticleResponse(article));
        //Http 200일 때(.ok()), JSON형태로 반환(.body( DTO에 article에서 찾은 정보를 .ArticleResponse(article)))
    }
    
    // 삭제
    @DeleteMapping("/api/articles/{id}") 
    public ResponseEntity<Void> deleteArticle(@PathVariable long id){   //<Void> 응답 바디가 없음 리턴값을 반환하지 않아도 될 때 사용
        blogService.delete(id);
        return ResponseEntity.ok().build(); //.bulid() Body에 담아서 반환할게 없을 때 사용 <Void>를 선언 했기에

    }

}
