package dennois.spring_match_finder_v2.integration.geocoding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LocationCorrectionServiceTest {

    private LocationCorrectionServiceImpl locationCorrectionService;

    @BeforeEach
    public void setUp() {
        locationCorrectionService = new LocationCorrectionServiceImpl();
    }

    @Test
    public void testCorrectLocationWithKnownErrors() {
        assertEquals("Lauf An Der Pegnitz", locationCorrectionService.correctLocation("Lauf An Der Pegnitz, Oberfranken"));
        assertEquals("Quickborn, Schleswig-Holstein", locationCorrectionService.correctLocation("Quickborn, S-h"));
        assertEquals("Quickborn, Schleswig-Holstein", locationCorrectionService.correctLocation("Quickborn, S:h"));
        assertEquals("Dachau, Bavaria", locationCorrectionService.correctLocation("Dachau Near Munich"));
    }

    @Test
    public void testCorrectLocationWithGunRange() {
        assertEquals("Texas", locationCorrectionService.correctLocation("Gun Range, Texas"));
        assertEquals("New York", locationCorrectionService.correctLocation("New York Gun Range"));
        assertEquals("", locationCorrectionService.correctLocation("Gun Range"));
    }

    @Test
    public void testCorrectLocationWithoutKnownErrors() {
        assertEquals("New York", locationCorrectionService.correctLocation("New York"));
    }

    @Test
    public void testSplitAndSearch() {
        List<String> result = locationCorrectionService.splitAndSearch("New York, Texas, California");
        assertEquals(3, result.size());
        assertEquals("New York", result.get(0));
        assertEquals("Texas", result.get(1));
        assertEquals("California", result.get(2));
    }
}