package onetomany.matches;

import onetomany.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchedPairRepository extends JpaRepository<onetomany.matches.MatchedPairMentorMentee, Long> {

    // Find all matches where the specified user is the mentor
    List<onetomany.matches.MatchedPairMentorMentee> findByMentor(User mentor);

    // Find all matches where the specified user is the mentee
    List<onetomany.matches.MatchedPairMentorMentee> findByMentee(User mentee);

    // Check if a match already exists between a specific mentor and mentee
    boolean existsByMentorAndMentee(User mentor, User mentee);
}
