package dennois.spring_match_finder_v2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class IPSCMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String matchDetailsLink;
    private String matchType;
    private String country;
    private String date;
    private String matchName;
    private String location;
    private String contactEmail;

    private Double latitude;
    private Double longitude;

    private Integer numberOfDays;
    private Integer minimumRounds;
    private Integer numberOfStages;
    private Double minimumFeeUSD;

    public IPSCMatch(String matchDetailsLink, String matchType, String country, String date, String matchName, String location, String contactEmail) {
        this.matchDetailsLink = matchDetailsLink;
        this.matchType = matchType;
        this.country = country;
        this.date = date;
        this.matchName = matchName;
        this.location = location;
        this.contactEmail = contactEmail;
    }

}