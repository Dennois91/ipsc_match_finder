package dennois.spring_match_finder_v2.scraper;

import dennois.spring_match_finder_v2.model.IPSCMatch;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MatchDetailsScraper {

    @Value("${match.details.root.url}")
    private String MATCH_DETAILS_ROOT_URL;


    public void populateMatchDetails(IPSCMatch match) {
        String DETAIL_URL = match.getMatchDetailsLink();

        try {
            Document doc = Jsoup.connect(MATCH_DETAILS_ROOT_URL + DETAIL_URL).get();

            Elements matchRows = doc.select("TABLE[CELLPADDING=2] TR");

            for (Element row : matchRows) {
                Element labelElement = row.selectFirst("TD[NOWRAP][VALIGN=TOP][ALIGN=RIGHT] B");
                Element valueElement = row.select("TD").get(1);

                if (labelElement != null && valueElement != null) {
                    String label = labelElement.text();
                    String value = valueElement.text();

                    switch (label) {
                        case "Number of Days:" -> match.setNumberOfDays(Integer.parseInt(value.trim()));
                        case "Minimum Rounds:" -> match.setMinimumRounds(Integer.parseInt(value.trim()));
                        case "Number of Stages:" -> match.setNumberOfStages(Integer.parseInt(value.trim()));
                        case "Entry Fee (USD):" -> match.setMinimumFeeUSD(Double.parseDouble(value.trim()));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
