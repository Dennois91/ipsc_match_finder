package dennois.spring_match_finder_v2.integration.geocoding;

import dennois.spring_match_finder_v2.model.IPSCMatch;
import dennois.spring_match_finder_v2.repositories.IPSCMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppendGeocodeService {

    private final IPSCMatchRepository matchRepository;
    private final GeocodingService geocodingService;

    @Autowired
    public AppendGeocodeService(IPSCMatchRepository matchRepository, GeocodingService geocodingService) {
        this.matchRepository = matchRepository;
        this.geocodingService = geocodingService;
    }

    public void updateAllMatchesWithCoordinates() {
        List<IPSCMatch> matches = matchRepository.findAll();

        for (IPSCMatch match : matches) {
            if (match.getLatitude() == null || match.getLongitude() == null) {
                geocodingService.updateMatchWithCoordinates(match);
                matchRepository.save(match);
            }
        }
    }
}
