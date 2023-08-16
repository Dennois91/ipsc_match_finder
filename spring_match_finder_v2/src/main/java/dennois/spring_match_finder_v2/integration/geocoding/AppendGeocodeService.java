package dennois.spring_match_finder_v2.integration.geocoding;

import dennois.spring_match_finder_v2.model.IPSCMatch;
import dennois.spring_match_finder_v2.repositories.IPSCMatchRepository;
import dennois.spring_match_finder_v2.services.proximityService.SpatialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class AppendGeocodeService {

    private final IPSCMatchRepository matchRepository;
    private final GeocodingService geocodingService;
    private final SpatialService spatialService;

    @Autowired
    public AppendGeocodeService(IPSCMatchRepository matchRepository, GeocodingService geocodingService, SpatialService spatialService) {
        this.matchRepository = matchRepository;
        this.geocodingService = geocodingService;
        this.spatialService = spatialService;
    }

    @Transactional
    public void updateAllMatchesWithCoordinates() {
        List<IPSCMatch> matchesWithoutLocationPoint = matchRepository.findByLocationPointIsNullAndLatitudeIsNotNullAndLongitudeIsNotNull();
        List<IPSCMatch> matchesWithoutCoordinates = matchRepository.findByLatitudeIsNullOrLongitudeIsNull();

        for (IPSCMatch match : matchesWithoutLocationPoint) {
            log.info("Creating point for match : {}", match.getId());
            match.setLocationPoint(spatialService.createPoint(match.getLatitude(), match.getLongitude()));
        }

        for (IPSCMatch match : matchesWithoutCoordinates) {
            log.info("Creating coordinates and point for match: {}", match.getId());
            geocodingService.updateMatchWithCoordinates(match);
            if (match.getLatitude() != null && match.getLongitude() != null) {
                match.setLocationPoint(spatialService.createPoint(match.getLatitude(), match.getLongitude()));
            }
        }

        matchesWithoutLocationPoint.addAll(matchesWithoutCoordinates);
        matchRepository.saveAll(matchesWithoutLocationPoint);
    }
}
