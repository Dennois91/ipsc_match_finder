package dennois.spring_match_finder_v2.scraper;

import dennois.spring_match_finder_v2.model.ISPCMatch;
import dennois.spring_match_finder_v2.repositories.IPSCMatchRepository;
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
public class MatchScraper {
    @Value("${ipsc.calendar.url}")
    private String IPSC_CALENDAR_URL;
    @Value("${match.details.root.url}")
    private String MATCH_DETAILS_ROOT_URL;
    private final IPSCMatchRepository IPSCMatchRepository;

    @Autowired
    public MatchScraper(IPSCMatchRepository IPSCMatchRepository) {
        this.IPSCMatchRepository = IPSCMatchRepository;
    }

    public void fetchAndSaveMatches() {

        try {
            Document doc = Jsoup.connect(IPSC_CALENDAR_URL)
                    .data("fdo", "Y")
                    .data("mmfilter", "3")
                    .data("discipline", "")
                    .data("regval", "")
                    .data("submit", "GO")
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

                ISPCMatch ISPCMatch = new ISPCMatch(matchDetailsLink, matchType, country, date, matchName, location, contactEmail);

                // Check if entry exists
                if (!IPSCMatchRepository.existsByMatchDetailsLink(matchDetailsLink)) {
                    try {
                        IPSCMatchRepository.save(ISPCMatch); // Persist the match to the database
                    } catch (DataAccessException e) {
                        //TODO Handle database exceptions, log them
                        System.err.println("Error saving match: " + ISPCMatch);
                        e.printStackTrace();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}