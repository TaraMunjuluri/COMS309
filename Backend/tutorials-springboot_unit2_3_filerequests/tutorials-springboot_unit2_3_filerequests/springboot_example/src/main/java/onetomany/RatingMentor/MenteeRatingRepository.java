//package onetomany.RatingMentor;
//
//import onetomany.Matches.MatchedPairMentorMentee;
//import onetomany.MenteeSurvey.Mentee;
//import onetomany.MentorSurvey.Mentor;
//import onetomany.Users.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface MenteeRatingRepository extends JpaRepository<MenteeRating, Long> {
//    List<MenteeRating> findByMentee(Mentee mentee);
//    List<MenteeRating> findByMentor(Mentor mentor);
//
//}

package onetomany.RatingMentor;

import onetomany.MenteeSurvey.Mentee;
import onetomany.MentorSurvey.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenteeRatingRepository extends JpaRepository<MenteeRating, Long> {
    // Find ratings by mentee
    List<MenteeRating> findByMentee(Mentee mentee);

    // Find ratings by mentor
    List<MenteeRating> findByMentor(Mentor mentor);

    // Find a specific rating by mentee and mentor
    Optional<MenteeRating> findByMenteeAndMentor(Mentee mentee, Mentor mentor);

    // Calculate average rating for a specific mentor
    @Query("SELECT AVG(mr.rating) FROM MenteeRating mr WHERE mr.mentor = :mentor")
    Double calculateAverageRatingForMentor(@Param("mentor") Mentor mentor);
}