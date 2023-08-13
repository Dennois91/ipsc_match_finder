package dennois.spring_match_finder_v2.bootstrap;

import dennois.spring_match_finder_v2.config.SSLDisablerConfig;
import dennois.spring_match_finder_v2.integration.geocoding.AppendGeocodeService;
import dennois.spring_match_finder_v2.scraper.MatchScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MatchScraper matchScraper;
    private final AppendGeocodeService appendGeocode;


    @Autowired
    public DataInitializer(MatchScraper matchScraper, AppendGeocodeService appendGeocode) {
        this.matchScraper = matchScraper;
        this.appendGeocode = appendGeocode;

    }

    @Override
    public void run(String... args) throws Exception {
        SSLDisablerConfig.disableSSLVerification();
        matchScraper.fetchAndSaveMatches();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        appendGeocode.updateAllMatchesWithCoordinates();
    }
}
