package chyeonj.blogexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing //created_at(생성일), updated_at(수정일) 자동 업데이트 
@SpringBootApplication(scanBasePackages = "chyeonj.blogexample")
public class ApplicationDevelop {

    public static void main(String[] args){ SpringApplication.run(ApplicationDevelop.class, args);}

}
