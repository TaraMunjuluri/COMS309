package onetomany.matches;

import onetomany.MenteeSurvey.Mentee;
import onetomany.MentorSurvey.Mentor;
import onetomany.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchedPairRepository extends JpaRepository<onetomany.matches.MatchedPair, Long> {
    boolean existsByMentorAndMentee(User mentor, User mentee);

}
