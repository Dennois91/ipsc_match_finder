package dennois.spring_match_finder_v2.services.proximityService;

import dennois.spring_match_finder_v2.model.Airport;
import dennois.spring_match_finder_v2.model.IPSCMatch;
import dennois.spring_match_finder_v2.model.Proximity;
import dennois.spring_match_finder_v2.repositories.AirportRepository;
import dennois.spring_match_finder_v2.repositories.IPSCMatchRepository;
import dennois.spring_match_finder_v2.repositories.ProximityRepository;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
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

    @Transactional
    public void computeAndSaveProximityForAllMatches() {
        try {
            List<IPSCMatch> matches = matchRepository.findAll();

            for (IPSCMatch match : matches) {
                List<Airport> closestAirports = findTwoClosestAirports(match);
                saveProximityData(match, closestAirports);
            }
            log.info("Proximity data computation and saving completed.");

        } catch (Exception e) {
            log.error("An error occurred during proximity data computation and saving: {}", e.getMessage());
        }

    }

    private void saveProximityData(IPSCMatch match, List<Airport> closestAirports) {
        if (closestAirports.size() >= 2) {
            double distanceToFirstAirport = calculateDistance(match.getLocationPoint(), closestAirports.get(0).getLocationPoint());
            double distanceToSecondAirport = calculateDistance(match.getLocationPoint(), closestAirports.get(1).getLocationPoint());

            Proximity proximity1 = createProximity(match, closestAirports.get(0), distanceToFirstAirport);
            Proximity proximity2 = createProximity(match, closestAirports.get(1), distanceToSecondAirport);

            proximityRepository.saveAll(Arrays.asList(proximity1, proximity2));
            proximityRepository.flush();
        }
    }

    private Proximity createProximity(IPSCMatch match, Airport airport, double distance) {
        Proximity proximity = new Proximity();
        proximity.setMatch(match);
        proximity.setAirport(airport);
        proximity.setDistanceToAirport(distance);
        return proximity;
    }

    private double calculateDistance(Point point1, Point point2) {
        Double distance = proximityRepository.findDistanceBetweenPoints(point1, point2);
        return distance != null ? distance : 0.0;
    }

    private List<Airport> findTwoClosestAirports(IPSCMatch match) {
        Point locationPoint = match.getLocationPoint();
        return airportRepository.findTwoClosestAirports(locationPoint, PageRequest.of(0, 2));
    }
}