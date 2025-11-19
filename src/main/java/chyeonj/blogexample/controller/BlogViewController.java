package chyeonj.blogexample.controller;


import chyeonj.blogexample.DTO.ArticleListViewResponse;
import chyeonj.blogexample.DTO.ArticleViewResponse;
import chyeonj.blogexample.Service.BlogService;
import chyeonj.blogexample.blogexam.domain.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BlogViewController {

    private final BlogService blogService;

    @GetMapping("/articles")
    public String getArticles(Model model){
        List<ArticleListViewResponse> artcles = blogService.findAll()
                .stream()
                .map(ArticleListViewResponse::new)
                .toList();
        model.addAttribute("articles",artcles);

        return "articleList";
    }

    @GetMapping("/articles/{id}")
    public  String getArticle(@PathVariable long id, Model model){
        Article article = blogService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));

        return "article";
    }


}
