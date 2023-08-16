package dennois.spring_match_finder_v2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;


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

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    private List<Proximity> proximities = new ArrayList<>();

    @Column(columnDefinition = "POINT")
    @NonNull
    private Point locationPoint;

    public IPSCMatch(String matchDetailsLink, String matchType, String country, String date, String matchName, String location, String contactEmail) {
        this.matchDetailsLink = matchDetailsLink;
        this.matchType = matchType;
        this.country = country;
        this.date = date;
        this.matchName = matchName;
        this.location = location;
        this.contactEmail = contactEmail;
    }

    @Override
    public String toString() {
        return "IPSCMatch{" +
                "id=" + id +
                ", matchDetailsLink='" + matchDetailsLink + '\'' +
                ", matchType='" + matchType + '\'' +
                ", country='" + country + '\'' +
                ", date='" + date + '\'' +
                ", matchName='" + matchName + '\'' +
                ", location='" + location + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", numberOfDays=" + numberOfDays +
                ", minimumRounds=" + minimumRounds +
                ", numberOfStages=" + numberOfStages +
                ", minimumFeeUSD=" + minimumFeeUSD +
                '}';
    }
}