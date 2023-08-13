package dennois.spring_match_finder_v2.bootstrap;

import dennois.spring_match_finder_v2.config.SSLDisablerConfig;
import dennois.spring_match_finder_v2.scraper.MatchScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MatchScraper matchScraper;

    @Autowired
    public DataInitializer(MatchScraper matchScraper) {
        this.matchScraper = matchScraper;

    }

    @Override
    public void run(String... args) throws Exception {
        SSLDisablerConfig.disableSSLVerification();
        matchScraper.fetchAndSaveMatches();
    }
}
