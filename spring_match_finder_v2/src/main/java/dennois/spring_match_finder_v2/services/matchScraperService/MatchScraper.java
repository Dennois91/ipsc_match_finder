package dennois.spring_match_finder_v2.services.matchScraperService;

import dennois.spring_match_finder_v2.model.IPSCMatch;
import dennois.spring_match_finder_v2.repositories.IPSCMatchRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class MatchScraper {
    @Value("${ipsc.calendar.url}")
    private String IPSC_CALENDAR_URL;
    private final IPSCMatchRepository IPSCMatchRepository;
    private final MatchDetailsScraper matchDetailsScraper;

    @Autowired
    public MatchScraper(IPSCMatchRepository IPSCMatchRepository,
                        MatchDetailsScraper matchDetailsScraper) {
        this.IPSCMatchRepository = IPSCMatchRepository;
        this.matchDetailsScraper = matchDetailsScraper;
    }

    public void fetchAndSaveMatches() {

        try {
            Document doc = Jsoup.connect(IPSC_CALENDAR_URL)
                    .data("fdo", "Y")
                    .data("mmfilter", "3")
                    .data("discipline", "")
                    .data("regval", "")
                    .data("submit", "GO")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .post();

            Elements matchRows = doc.select("table[cellpadding=9] tr");

            for (Element row : matchRows) {
                Elements tds = row.select("td");

                String matchDetailsLink = tds.get(0).select("a").attr("href");
                String matchType = tds.get(0).text();

                String country = tds.get(1).select("b").text();
                String date = tds.get(1).ownText();

                // Extracting match name, location, and contact email.
                String innerHtml = tds.get(2).html();
                String[] splitData = innerHtml.split("<br>");

                String matchName = splitData[0].contains("<a") ? tds.get(2).select("a").text() : splitData[0];
                String contactEmail = tds.get(2).select("a[href^=mailto]").text();
                matchName = matchName.replace(contactEmail, "").trim(); // Remove email from the matchName

                String location = splitData.length > 1 ? splitData[1].replaceAll("<.*?>", "").trim() : "Default Location or N/A"; // Removing any HTML tags and trimming

                IPSCMatch existingMatch = IPSCMatchRepository.findByMatchDetailsLink(matchDetailsLink);

                if (existingMatch == null) {
                    existingMatch = new IPSCMatch(matchDetailsLink, matchType, country, date, matchName, location, contactEmail);
                }
                try {
                    matchDetailsScraper.populateMatchDetails(existingMatch);
                    IPSCMatchRepository.save(existingMatch);
                    log.info("Match details populated and saved: {}", existingMatch);
                    Thread.sleep(2000);

                } catch (DataAccessException e) {
                    log.error("Error saving match: {}", existingMatch, e);
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
