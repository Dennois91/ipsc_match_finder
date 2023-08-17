package dennois.spring_match_finder_v2.repositories;

import dennois.spring_match_finder_v2.model.Airport;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AirportRepository extends JpaRepository<Airport, Integer> {
    @Query(value = "SELECT a FROM Airport a ORDER BY ST_Distance_Sphere(a.locationPoint, :locationPoint)")
    List<Airport> findTwoClosestAirports(@Param("locationPoint") Point locationPoint, Pageable pageable);
}
