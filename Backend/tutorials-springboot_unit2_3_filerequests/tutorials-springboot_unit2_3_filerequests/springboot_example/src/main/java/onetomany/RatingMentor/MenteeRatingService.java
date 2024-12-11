//package onetomany.RatingMentor;
//
//import onetomany.Matches.MatchedPairMentorMentee;
//import onetomany.Matches.MatchedPairRepository;
//import onetomany.MenteeSurvey.Mentee;
//import onetomany.MenteeSurvey.MenteeRepository;
//import onetomany.MentorSurvey.Mentor;
//import onetomany.MentorSurvey.MentorRepository;
//import onetomany.Users.User;
//import onetomany.Users.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import onetomany.RatingMentor.MenteeRatingRepository;
//import java.util.List;
//
////@Service
////public class MenteeRatingService {
////
////    private final MenteeRatingRepository menteeRatingRepository;
////
////    public MenteeRatingService(MenteeRatingRepository menteeRatingRepository) {
////        this.menteeRatingRepository = menteeRatingRepository;
////    }
////
////    public MenteeRating saveRating(Mentee mentee, Mentor mentor, int rating) {
////        MenteeRating menteeRating = new MenteeRating(mentee, mentor, rating);
////        return menteeRatingRepository.save(menteeRating);
////    }
////
////    public List<MenteeRating> getRatingsByMentee(Mentee mentee) {
////        return menteeRatingRepository.findByMentee(mentee);
////    }
////
////    public List<MenteeRating> getRatingsByMentor(Mentor mentor) {
////        return menteeRatingRepository.findByMentor(mentor);
////    }
////}
//
//@Service
//public class MenteeRatingService {
//
//    @Autowired
//    private MatchedPairRepository matchedPairRepository;
//
//    @Autowired
//    private MenteeRatingRepository menteeRatingRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private MentorRepository mentorRepository;
//
//    @Autowired
//    private MenteeRepository menteeRepository;
//
//    public MenteeRating rateMentor(Long menteeId, Long mentorId, int rating) {
//        // Ensure that both mentor and mentee exist
//        User mentor = userRepository.findById(mentorId)
//                .orElseThrow(() -> new IllegalArgumentException("Mentor not found"));
//
//        User mentee = userRepository.findById(menteeId)
//                .orElseThrow(() -> new IllegalArgumentException("Mentee not found"));
//
//        // Check if a matched pair exists for this mentor and mentee
//        MatchedPairMentorMentee matchedPair = matchedPairRepository.findByMentorAndMentee(mentor, mentee);
//        if (matchedPair == null) {
//            throw new IllegalArgumentException("No matched pair exists for this mentor and mentee");
//        }
//
//        // Create and save the rating
//        MenteeRating menteeRating = new MenteeRating();
//        Mentor mentorEntity = mentorRepository.findByUser(mentor); // Assuming mentorRepository finds Mentor by User
//        menteeRating.setMentor(mentorEntity);
////        menteeRating.setMentor(mentor);
////        menteeRating.setMentee(mentee);
//        Mentee menteeEntity = menteeRepository.findByUser(mentee);  // 'mentee' is the User object
//        menteeRating.setMentee(menteeEntity);
//        menteeRating.setRating(rating);
//
//        return menteeRatingRepository.save(menteeRating);
//    }
//}

package onetomany.RatingMentor;

import onetomany.Matches.MatchedPairMentorMentee;
import onetomany.Matches.MatchedPairRepository;
import onetomany.MenteeSurvey.Mentee;
import onetomany.MenteeSurvey.MenteeRepository;
import onetomany.MentorSurvey.Mentor;
import onetomany.MentorSurvey.MentorRepository;
import onetomany.Users.User;
import onetomany.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MenteeRatingService {
    @Autowired
    private MatchedPairRepository matchedPairRepository;

    @Autowired
    private MenteeRatingRepository menteeRatingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private MenteeRepository menteeRepository;

    @Transactional
    public MenteeRating rateMentor(Long menteeId, Long mentorId, int rating) {
        // Ensure that both mentor and mentee exist
        User mentorUser = userRepository.findById(mentorId)
                .orElseThrow(() -> new IllegalArgumentException("Mentor not found"));

        User menteeUser = userRepository.findById(menteeId)
                .orElseThrow(() -> new IllegalArgumentException("Mentee not found"));

        // Check if a matched pair exists for this mentor and mentee
        MatchedPairMentorMentee matchedPair = matchedPairRepository.findByMentorAndMentee(mentorUser, menteeUser);
        if (matchedPair == null) {
            throw new IllegalArgumentException("No active match exists for this mentor and mentee");
        }

        // Convert User to Mentor and Mentee entities
        Mentor mentorEntity = mentorRepository.findByUser(mentorUser);
        Mentee menteeEntity = menteeRepository.findByUser(menteeUser);

        // Check if a rating already exists
        Optional<MenteeRating> existingRating = menteeRatingRepository.findByMenteeAndMentor(menteeEntity, mentorEntity);

        MenteeRating menteeRating;
        if (existingRating.isPresent()) {
            // Update existing rating
            menteeRating = existingRating.get();
            menteeRating.setRating(rating);
            menteeRating.setRatingDate(LocalDateTime.now());
        } else {
            // Create new rating
            menteeRating = new MenteeRating(menteeEntity, mentorEntity, rating);
        }

        return menteeRatingRepository.save(menteeRating);
    }

    @Transactional(readOnly = true)
    public Double getMentorAverageRating(Long mentorId) {
        User mentorUser = userRepository.findById(mentorId)
                .orElseThrow(() -> new IllegalArgumentException("Mentor not found"));

        Mentor mentorEntity = mentorRepository.findByUser(mentorUser);
        return menteeRatingRepository.calculateAverageRatingForMentor(mentorEntity);
    }
}