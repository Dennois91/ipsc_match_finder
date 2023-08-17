package dennois.spring_match_finder_v2.integration.geocoding;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LocationCorrectionServiceImpl implements LocationCorrectionService {

    private final Map<String, String> errorMapping;

    public LocationCorrectionServiceImpl() {
        errorMapping = new HashMap<>();
        errorMapping.put("Lauf An Der Pegnitz, Oberfranken", "Lauf An Der Pegnitz");
        errorMapping.put("Quickborn, S-h", "Quickborn, Schleswig-Holstein");
        errorMapping.put("Quickborn, S:h", "Quickborn, Schleswig-Holstein");
        errorMapping.put("Dachau Near Munich", "Dachau, Bavaria");
    }

    @Override
    public String correctLocation(String location) {
        location = errorMapping.getOrDefault(location, location);

        if (location.toLowerCase().contains("gun range")) {
            int commaIndex = location.indexOf(",");
            if (commaIndex != -1) {
                location = location.substring(commaIndex + 1).trim();
            } else {
                location = location.replaceFirst("(?i)gun range", "").trim();
            }
        }
        return location;
    }

    @Override
    public List<String> splitAndSearch(String location) {
        List<String> searchTerms = new ArrayList<>();
        String[] parts = location.split(",");

        for (String part : parts) {
            searchTerms.add(part.trim());
        }

        return searchTerms;
    }
}
