package dennois.spring_match_finder_v2.services.proximityService;

import dennois.spring_match_finder_v2.model.Airport;
import dennois.spring_match_finder_v2.model.IPSCMatch;
import dennois.spring_match_finder_v2.model.Proximity;
import org.locationtech.jts.geom.Point;

import java.util.List;

public interface ProximityService {
    void computeAndSaveProximityForAllMatches();

    void saveProximityData(IPSCMatch match, List<Airport> closestAirports);
    Proximity createProximity(IPSCMatch match, Airport airport, double distance);

    double calculateDistance(Point point1, Point point2);

    List<Airport> findTwoClosestAirports(IPSCMatch match);


}
