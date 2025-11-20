package chyeonj.blogexample.controller;


import chyeonj.blogexample.DTO.ArticleListViewResponse;
import chyeonj.blogexample.DTO.ArticleViewResponse;
import chyeonj.blogexample.Service.BlogService;
import chyeonj.blogexample.blogexam.domain.Article;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BlogViewController {

    private final BlogService blogService;

    @GetMapping("/articles")
    public String getArticles(Model model){
        List<ArticleListViewResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleListViewResponse::new)
                .toList();
        model.addAttribute("articles",articles);

        return "articleList";
    }

    @GetMapping("/articles/{id}")
    public  String getArticle(@PathVariable(required = false) Long id, Model model){ //Long은 숫자를 감싸는 객체 Long a = 10L; 가능
        Article article = blogService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));
        return "article";
    }

    @GetMapping("/new-article")
    //id 키를 가진 쿼리 파라미터의 값을 id 변수에 매핑(id는 없을 수도 있음)
    public String newArticle(@RequestParam(required = false) Long id, Model model){ //Long은 숫자를 감싸는 객체 Long a = 10L; 가능
        if (id == null){
            model.addAttribute("article", new ArticleViewResponse());
        } else {
            Article article = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }
        return "newArticle";
    }


}
