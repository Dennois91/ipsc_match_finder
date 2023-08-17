package dennois.spring_match_finder_v2.integration.geocoding;

import dennois.spring_match_finder_v2.model.IPSCMatch;

import java.util.Map;

public interface GeocodingService {
    void updateMatchWithCoordinates(IPSCMatch match);
    Map<String, Object> fetchCoordinates(String location);
    void updateMatchWithGeoData(IPSCMatch match, Map<String, Object> geocodeData);
    void sleepToAvoidRateLimits();
}
