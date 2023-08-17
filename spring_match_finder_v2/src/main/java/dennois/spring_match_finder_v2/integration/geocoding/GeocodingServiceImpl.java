package dennois.spring_match_finder_v2.integration.geocoding;

import dennois.spring_match_finder_v2.model.IPSCMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class GeocodingServiceImpl implements GeocodingService {

    @Value("${geocode.root.url}")
    private String rootUrl;
    private final RestTemplate restTemplate;
    private final LocationCorrectionServiceImpl locationCorrectionService;


    @Autowired
    public GeocodingServiceImpl(RestTemplate restTemplate, LocationCorrectionServiceImpl locationCorrectionService) {
        this.restTemplate = restTemplate;
        this.locationCorrectionService = locationCorrectionService;

    }

    @Override
    public void updateMatchWithCoordinates(IPSCMatch match) {
        if (match.getLatitude() == null || match.getLongitude() == null) {
            String location = locationCorrectionService.correctLocation(match.getLocation());
            Map<String, Object> geocodeData = fetchCoordinates(location);

            if (geocodeData == null) {
                List<String> searchTerms = locationCorrectionService.splitAndSearch(location);
                for (String searchTerm : searchTerms) {
                    geocodeData = fetchCoordinates(searchTerm);
                    if (geocodeData != null) {
                        break;
                    }
                }
            }
            if (geocodeData != null) {
                updateMatchWithGeoData(match, geocodeData);
            }
            sleepToAvoidRateLimits();
        }
    }

    @Override
    public Map<String, Object> fetchCoordinates(String location) {
        if (rootUrl == null || rootUrl.trim().isEmpty()) {
            throw new IllegalStateException("rootUrl is not set or is invalid.");
        }
        String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8);
        String url = rootUrl + "/search?q=" + encodedLocation + "&format=json";

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        if (response.getStatusCode() == HttpStatus.OK && !Objects.requireNonNull(response.getBody()).isEmpty()) {
            return response.getBody().get(0);
        }
        return null;
    }

    @Override
    public void updateMatchWithGeoData(IPSCMatch match, Map<String, Object> geocodeData) {
        Object latObj = geocodeData.get("lat");
        Object lonObj = geocodeData.get("lon");


        if (latObj != null && lonObj != null) {
            String latString = latObj.toString().trim();
            String lonString = lonObj.toString().trim();

            try {
                double lat = Double.parseDouble(latString);
                double lon = Double.parseDouble(lonString);

                match.setLatitude(lat);
                match.setLongitude(lon);

            } catch (NumberFormatException e) {
                //TODO Handle exception
            }
        }
    }

    @Override
    public void sleepToAvoidRateLimits() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}