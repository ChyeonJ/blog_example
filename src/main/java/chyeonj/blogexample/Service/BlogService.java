package chyeonj.blogexample.Service;


import chyeonj.blogexample.DTO.AddArticleRequest;
import chyeonj.blogexample.blogexam.domain.Article;
import chyeonj.blogexample.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RequiredArgsConstructor    //final이 붙거나 @NotNull이 붙은 필드의 생성자 추가
@Service //빈 등록
public class BlogService {

    private final BlogRepository blogRepository;

    //블로그 글 추가 메서드
    public Article save(AddArticleRequest request){
        return blogRepository.save(request.toEntity());
    }

    public Article findById(long id){return blogRepository.findById(id).orElse(null);}

    public List<Article> findAll(){return blogRepository.findAll();}

    public void deleteById (@PathVariable long id){blogRepository.delete(id);}
}
