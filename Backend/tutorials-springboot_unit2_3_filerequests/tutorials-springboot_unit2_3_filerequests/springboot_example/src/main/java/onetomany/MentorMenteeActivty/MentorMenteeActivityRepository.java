package onetomany.Matches;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentorMenteeActivityRepository extends JpaRepository<onetomany.Matches.MentorMenteeActivity, Long> {
    List<onetomany.Matches.MentorMenteeActivity> findByMatchedPair(MatchedPairMentorMentee matchedPair);
    List<onetomany.Matches.MentorMenteeActivity> findByMatchedPairOrderByMeetingDateDesc(MatchedPairMentorMentee matchedPair);
}