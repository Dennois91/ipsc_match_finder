package dennois.spring_match_finder_v2.integration.geocoding;

import dennois.spring_match_finder_v2.model.IPSCMatch;
import dennois.spring_match_finder_v2.repositories.IPSCMatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class AppendGeocodeServiceTest {

    @Mock
    private IPSCMatchRepository matchRepository;

    @Mock
    private GeocodingService geocodingService;

    @InjectMocks
    private AppendGeocodeService appendGeocodeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateAllMatchesWithCoordinates() {
        // Given
        IPSCMatch matchWithCoords = mock(IPSCMatch.class);
        when(matchWithCoords.getLatitude()).thenReturn(40.7128);
        when(matchWithCoords.getLongitude()).thenReturn(-74.0060);

        IPSCMatch matchWithoutCoords = mock(IPSCMatch.class);
        when(matchWithoutCoords.getLatitude()).thenReturn(null);
        when(matchWithoutCoords.getLongitude()).thenReturn(null);

        List<IPSCMatch> matches = Arrays.asList(matchWithCoords, matchWithoutCoords);
        when(matchRepository.findAll()).thenReturn(matches);

        appendGeocodeService.updateAllMatchesWithCoordinates();

        verify(geocodingService, never()).updateMatchWithCoordinates(matchWithCoords);
        verify(geocodingService).updateMatchWithCoordinates(matchWithoutCoords);

        verify(matchRepository, never()).save(matchWithCoords);
        verify(matchRepository).save(matchWithoutCoords);
    }
}
