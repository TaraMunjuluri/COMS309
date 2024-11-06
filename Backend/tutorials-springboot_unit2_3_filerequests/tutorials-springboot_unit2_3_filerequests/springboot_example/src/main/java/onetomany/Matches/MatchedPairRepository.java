package onetomany.matches;

import onetomany.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchedPairRepository extends JpaRepository<onetomany.matches.MatchedPairMentorMentee, Long> {
    boolean existsByMentorAndMentee(User mentor, User mentee);

}
