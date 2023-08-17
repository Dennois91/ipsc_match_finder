package dennois.spring_match_finder_v2.scheduled;

import dennois.spring_match_finder_v2.integration.geocoding.AppendGeocodeServiceImpl;
import dennois.spring_match_finder_v2.services.matchScraperService.MatchScraperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyTasksSchedule {
    private final MatchScraperImpl matchScraper;
    private final AppendGeocodeServiceImpl appendGeocode;
    @Autowired
    public DailyTasksSchedule(MatchScraperImpl matchScraper, AppendGeocodeServiceImpl appendGeocode) {
        this.matchScraper = matchScraper;
        this.appendGeocode = appendGeocode;
    }

    @Scheduled(cron = "0 0 3 * * ?", zone="America/New_York")
    public void dailyScheduledTasks() {
        matchScraper.fetchAndSaveMatches();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        appendGeocode.updateAllMatchesWithCoordinates();
    }
}


