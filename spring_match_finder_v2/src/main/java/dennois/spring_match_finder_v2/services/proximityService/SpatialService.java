package dennois.spring_match_finder_v2.services.proximityService;

import org.locationtech.jts.geom.Point;

public interface SpatialService {
    Point createPoint(double latitude, double longitude);
}
