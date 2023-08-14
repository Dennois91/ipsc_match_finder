package dennois.spring_match_finder_v2;

import dennois.spring_match_finder_v2.scraper.MatchScraper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringMatchFinderV2Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringMatchFinderV2Application.class, args);

        
    }

}
