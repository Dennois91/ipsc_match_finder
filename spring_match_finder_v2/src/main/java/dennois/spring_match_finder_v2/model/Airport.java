package dennois.spring_match_finder_v2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String ident;
    private String name;
    private Double latitude;
    private Double longitude;


    @OneToMany(mappedBy = "airport", cascade = CascadeType.ALL)
    private List<Proximity> proximities = new ArrayList<>();

    @Column(columnDefinition = "POINT")
    private Point locationPoint;

    public Airport(int id, String ident, String name, Double latitude, Double longitude) {
        this.id = id;
        this.ident = ident;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}