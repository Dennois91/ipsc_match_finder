package dennois.spring_match_finder_v2.services.matchScraperService;

import dennois.spring_match_finder_v2.model.IPSCMatch;

public interface MatchDetailsScraper {
    void populateMatchDetails(IPSCMatch match);
}
