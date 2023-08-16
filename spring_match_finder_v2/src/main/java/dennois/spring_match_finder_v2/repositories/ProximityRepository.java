package dennois.spring_match_finder_v2.repositories;

import dennois.spring_match_finder_v2.model.Airport;
import dennois.spring_match_finder_v2.model.Proximity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProximityRepository extends JpaRepository<Proximity,Integer> {

    @Query(value = "SELECT * FROM airport " +
            "ORDER BY ST_Distance_Sphere(location_point, ST_GeomFromText(:wktLocationPoint)) " +
            "LIMIT 2", nativeQuery = true)
    List<Airport> findTwoClosestAirports(@Param("wktLocationPoint") String wktLocationPoint);

}
