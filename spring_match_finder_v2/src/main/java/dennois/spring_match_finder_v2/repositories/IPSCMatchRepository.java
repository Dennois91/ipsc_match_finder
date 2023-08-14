package dennois.spring_match_finder_v2.repositories;

import dennois.spring_match_finder_v2.model.IPSCMatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPSCMatchRepository extends JpaRepository<IPSCMatch, Integer> {

    IPSCMatch findByMatchDetailsLink(String matchDetailsLink);
}