package dennois.spring_match_finder_v2.services.airportDataService;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import dennois.spring_match_finder_v2.model.Airport;
import dennois.spring_match_finder_v2.repositories.AirportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class AirportDataService {

    @Value("${airport.csv.url}")
    private String Url;
    private final RestTemplate restTemplate;
    private final AirportRepository airportRepository;


    @Autowired
    public AirportDataService(RestTemplate restTemplate, AirportRepository airportRepository) {
        this.restTemplate = restTemplate;
        this.airportRepository = airportRepository;
    }

    @Transactional
    public void fetchAndStoreAirportData() {
        byte[] csvContent = fetchAirportDataFromSource();
        File tempFile = saveToTempFile(csvContent);
        List<Airport> airports = processCsvFile(tempFile);
        updateDatabase(airports);
        cleanupTempFile(tempFile);
    }

    private byte[] fetchAirportDataFromSource() {
        ResponseEntity<byte[]> response = restTemplate.getForEntity(Url, byte[].class);

        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("Fetching airport data from {}", Url);
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch airport data: " + response.getStatusCode());
        }
    }


    private File saveToTempFile(byte[] csvContent) {
        try {
            File tempFile = File.createTempFile("airport_data_", ".csv");
            Files.write(tempFile.toPath(), csvContent);
            log.info("Saving CSV content to a temporary file.");
            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save CSV content to a temporary file", e);
        }
    }


    private List<Airport> processCsvFile(File tempFile) {
        try (FileReader fileReader = new FileReader(tempFile);
             CSVReader csvReader = new CSVReader(fileReader)) {

            List<Airport> airports = new ArrayList<>();
            String[] nextLine;
            boolean firstRow = true;

            while ((nextLine = csvReader.readNext()) != null) {
                if (firstRow) {
                    firstRow = false;
                    continue;
                }

                if (nextLine.length < 6) {
                    log.warn("Skipping malformed line in CSV: {}", Arrays.toString(nextLine));
                    continue;
                }

                String airportType = nextLine[2];
                if (!"large_airport".equalsIgnoreCase(airportType) && !"medium_airport".equalsIgnoreCase(airportType)) {
                    continue;
                }

                Airport airport = new Airport();
                airport.setIdent(nextLine[1]);
                airport.setName(nextLine[3]);
                airport.setLatitude(Double.parseDouble(nextLine[4]));
                airport.setLongitude(Double.parseDouble(nextLine[5]));

                airports.add(airport);
            }

            log.info("Processed {} airports from the CSV.", airports.size());
            return airports;

        } catch (IOException | CsvValidationException e) {
            log.error("Failed to process the CSV file", e);
            throw new RuntimeException("Failed to process the CSV file", e);
        }
    }

    public void updateDatabase(List<Airport> airports) {
        if (airports == null || airports.isEmpty()) {
            log.warn("No airports to update in the database.");
            return;
        }
        try {
            log.info("Starting update of airport data...");
            airportRepository.deleteAll();
            List<Airport> savedAirports = airportRepository.saveAll(airports);
            log.info("Successfully updated {} airports in the database.", savedAirports.size());
        } catch (Exception e) {
            log.error("Failed to update airports in the database", e);
        }
    }


    private void cleanupTempFile(File tempFile) {
        try {
            if (tempFile != null && tempFile.exists()) {
                boolean deleted = tempFile.delete();
                if (deleted) {
                    log.info("Temporary file {} deleted successfully.", tempFile.getPath());
                } else {
                    log.warn("Failed to delete temporary file {}. You might want to delete it manually.", tempFile.getPath());
                }
            }
        } catch (Exception e) {
            log.error("Error occurred while trying to delete temporary file {}", tempFile.getPath(), e);
        }
    }
}
