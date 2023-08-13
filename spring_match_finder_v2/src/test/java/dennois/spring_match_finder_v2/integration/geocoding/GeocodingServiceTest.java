package dennois.spring_match_finder_v2.integration.geocoding;

import dennois.spring_match_finder_v2.model.IPSCMatch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GeocodingServiceTest {

    @Mock
    private IPSCMatch iPSCMatch;

    @InjectMocks
    private GeocodingService geocodingService;

    @Test
    public void testUpdateMatchWithGeoData_WithNullLatLon() {
        Map<String, Object> geocodeData = new HashMap<>();
        geocodeData.put("lat", null);
        geocodeData.put("lon", null);

        geocodingService.updateMatchWithGeoData(iPSCMatch, geocodeData);

        verify(iPSCMatch, never()).setLatitude(anyDouble());
        verify(iPSCMatch, never()).setLongitude(anyDouble());
    }

    @Test
    public void testUpdateMatchWithGeoData_WithValidLatLon() {
        Map<String, Object> geocodeData = new HashMap<>();
        geocodeData.put("lat", "40.7128");
        geocodeData.put("lon", "74.0060");

        geocodingService.updateMatchWithGeoData(iPSCMatch, geocodeData);

        verify(iPSCMatch).setLatitude(anyDouble());
        verify(iPSCMatch).setLongitude(anyDouble());
    }
}