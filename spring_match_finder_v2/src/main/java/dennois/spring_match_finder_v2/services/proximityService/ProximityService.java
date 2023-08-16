package dennois.spring_match_finder_v2.services.proximityService;

import dennois.spring_match_finder_v2.model.Airport;
import dennois.spring_match_finder_v2.model.IPSCMatch;
import dennois.spring_match_finder_v2.repositories.AirportRepository;
import dennois.spring_match_finder_v2.repositories.IPSCMatchRepository;
import dennois.spring_match_finder_v2.repositories.ProximityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProximityService {

    private final IPSCMatchRepository matchRepository;
    private final AirportRepository airportRepository;
    private final ProximityRepository proximityRepository;

    @Autowired
    public ProximityService(IPSCMatchRepository matchRepository,
                            AirportRepository airportRepository,
                            ProximityRepository proximityRepository) {
        this.matchRepository = matchRepository;
        this.airportRepository = airportRepository;
        this.proximityRepository = proximityRepository;
    }

    public void computeAndSaveProximitiesForAllMatches() {
        List<IPSCMatch> matches = matchRepository.findAll();

        for (IPSCMatch match : matches) {
            List<Airport> closestAirports = findTwoClosestAirports(match);
            saveProximityData(match, closestAirports);
        }
    }

    private void saveProximityData(IPSCMatch match, List<Airport> closestAirports) {
    }

    private List<Airport> findTwoClosestAirports(IPSCMatch match) {
        String wktLocationPoint = "POINT(" + match.getLongitude() + " " + match.getLatitude() + ")";
        return airportRepository.findTwoClosestAirports(wktLocationPoint);

    }
}
