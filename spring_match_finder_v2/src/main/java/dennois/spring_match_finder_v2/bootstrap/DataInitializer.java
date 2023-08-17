package dennois.spring_match_finder_v2.bootstrap;

import dennois.spring_match_finder_v2.config.SSLDisablerConfig;
import dennois.spring_match_finder_v2.integration.geocoding.AppendGeocodeService;
import dennois.spring_match_finder_v2.services.airportDataService.AirportDataService;
import dennois.spring_match_finder_v2.services.matchScraperService.MatchScraper;
import dennois.spring_match_finder_v2.services.proximityService.ProximityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final MatchScraper matchScraper;
    private final AppendGeocodeService appendGeocode;
    private final AirportDataService airportDataService;
    private final ProximityService proximityService;

    @Autowired
    public DataInitializer(MatchScraper matchScraper, AppendGeocodeService appendGeocode, AirportDataService airportDataService, ProximityService proximityService) {
        this.matchScraper = matchScraper;
        this.appendGeocode = appendGeocode;
        this.airportDataService = airportDataService;
        this.proximityService = proximityService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting data initialization...");
        SSLDisablerConfig.disableSSLVerification();

        log.info("Fetching and storing airport data...");
        //airportDataService.fetchAndStoreAirportData();

        log.info("Fetching and saving matches...");
        //matchScraper.fetchAndSaveMatches();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info("Updating all matches with coordinates...");
        //appendGeocode.updateAllMatchesWithCoordinates();

        log.info("Calculating proximity's...");
        //proximityService.computeAndSaveProximityForAllMatches();

        log.info("Data initialization completed.");
    }
}