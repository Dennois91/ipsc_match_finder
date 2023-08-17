package dennois.spring_match_finder_v2.integration.geocoding;

import java.util.List;

public interface LocationCorrectionService {

    String correctLocation(String location);
    List<String> splitAndSearch(String location);

}
