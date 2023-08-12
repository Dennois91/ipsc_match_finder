package dennois.spring_match_finder_v2.repositories;

import dennois.spring_match_finder_v2.model.ISPCMatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPSCMatchRepository extends JpaRepository<ISPCMatch, Integer> {

    boolean existsByMatchDetailsLink(String matchDetailsLink);
}