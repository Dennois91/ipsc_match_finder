package dennois.spring_match_finder_v2.repositories;

import dennois.spring_match_finder_v2.model.Airport;
import dennois.spring_match_finder_v2.model.Proximity;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProximityRepository extends JpaRepository<Proximity,Integer> {

    @Query(value = "SELECT ST_Distance_Sphere(:point1, :point2)", nativeQuery = true)
    Double findDistanceBetweenPoints(@Param("point1") Point point1, @Param("point2") Point point2);

}
