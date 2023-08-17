package dennois.spring_match_finder_v2.services.airportDataService;

import dennois.spring_match_finder_v2.model.Airport;

import java.io.File;
import java.util.List;

public interface AirportDataService {
    void fetchAndStoreAirportData();
    byte[] fetchAirportDataFromSource();
    File saveToTempFile(byte[] csvContent);
    List<Airport> processCsvFile(File tempFile);
    void updateDatabase(List<Airport> airports);
    void cleanupTempFile(File tempFile);
}
