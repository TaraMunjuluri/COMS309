//package onetomany.RatingMentor;
//
//
//import onetomany.MenteeSurvey.Mentee;
//import onetomany.MenteeSurvey.MenteeRepository;
//import onetomany.MentorSurvey.Mentor;
//import onetomany.MentorSurvey.MentorRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import onetomany.MenteeSurvey.Mentee;
//import onetomany.MentorSurvey.Mentor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/mentee-ratings")
//public class MenteeRatingController {
//
////    @Autowired
////    private MenteeRatingRepository menteeRatingRepository;
//
//    @Autowired
//    private MenteeRatingService menteeRatingService;
//
//    // Constructor Injection (preferred)
//
//    private final MenteeRepository menteeRepository;
//    private final MentorRepository mentorRepository;
//    private final MenteeRatingRepository menteeRatingRepository;
//
//    // Use explicit constructor for dependency injection
//    public MenteeRatingController(
//            MenteeRepository menteeRepository,
//            MentorRepository mentorRepository,
//            MenteeRatingRepository menteeRatingRepository) {
//        this.menteeRepository = menteeRepository;
//        this.mentorRepository = mentorRepository;
//        this.menteeRatingRepository = menteeRatingRepository;
//    }
//
//
//    @PostMapping("/rate/{menteeId}/{mentorId}/{rating}")
//    public ResponseEntity<MenteeRating> rateMentor(
//            @PathVariable Long menteeId,
//            @PathVariable Long mentorId,
//            @PathVariable int rating) {
//        try {
//            MenteeRating ratingResponse = menteeRatingService.rateMentor(menteeId, mentorId, rating);
//            return ResponseEntity.ok(ratingResponse);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//    }
//
//    @GetMapping("/mentee/{menteeId}")
//    public List<MenteeRating> getRatingsByMentee(@PathVariable Long menteeId) {
//        Mentee mentee = new Mentee();
//        mentee.setId(menteeId); // Assuming Mentee entity has an 'id' field
//        return menteeRatingRepository.findByMentee(mentee);
//    }
//
//    @GetMapping("/mentor/{mentorId}")
//    public List<MenteeRating> getRatingsByMentor(@PathVariable Long mentorId) {
//        Mentor mentor = new Mentor();
//        mentor.setId(mentorId); // Assuming Mentor entity has an 'id' field
//        return menteeRatingRepository.findByMentor(mentor);
//    }
////    @PutMapping("/{menteeId}/rate/{mentorId}/{rating}")
////    public ResponseEntity<MenteeRating> rateMentor(
////            @PathVariable Long menteeId,
////            @PathVariable Long mentorId,
////            @PathVariable int rating) {
////
////        // Ensure the rating is valid
////        if (rating < 1 || rating > 5) {
////            return ResponseEntity.badRequest().body(null);
////        }
////
////        // Fetch the mentee and mentor
////        Mentee mentee = menteeRepository.findById(menteeId)
////                .orElseThrow(() -> new RuntimeException("Mentee not found"));
////        Mentor mentor = mentorRepository.findById(mentorId)
////                .orElseThrow(() -> new RuntimeException("Mentor not found"));
////
////        // Create the rating
////        MenteeRating menteeRating = new MenteeRating();
////        menteeRating.setMentee(mentee);
////        menteeRating.setMentor(mentor);
////        menteeRating.setRating(rating);
////
////        // Save the rating
////        MenteeRating savedRating = menteeRatingRepository.save(menteeRating);
////        return ResponseEntity.status(HttpStatus.CREATED).body(savedRating);
////    }
//
//}


package onetomany.RatingMentor;

import onetomany.MenteeSurvey.Mentee;
import onetomany.MentorSurvey.Mentor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mentee-ratings")
public class MenteeRatingController {
    @Autowired
    private MenteeRatingService menteeRatingService;

    @Autowired
    private MenteeRatingRepository menteeRatingRepository;

    @PostMapping("/rate/{menteeId}/{mentorId}/{rating}")
    public ResponseEntity<?> rateMentor(
            @PathVariable Long menteeId,
            @PathVariable Long mentorId,
            @PathVariable int rating) {
        try {
            MenteeRating ratingResponse = menteeRatingService.rateMentor(menteeId, mentorId, rating);
            return ResponseEntity.ok(ratingResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/mentee/{menteeId}")
    public List<MenteeRating> getRatingsByMentee(@PathVariable Long menteeId) {
        Mentee mentee = new Mentee();
        mentee.setId(menteeId);
        return menteeRatingRepository.findByMentee(mentee);
    }

    @GetMapping("/mentor/{mentorId}")
    public List<MenteeRating> getRatingsByMentor(@PathVariable Long mentorId) {
        Mentor mentor = new Mentor();
        mentor.setId(mentorId);
        return menteeRatingRepository.findByMentor(mentor);
    }

    @GetMapping("/mentor/{mentorId}/average")
    public ResponseEntity<?> getMentorAverageRating(@PathVariable Long mentorId) {
        try {
            Double averageRating = menteeRatingService.getMentorAverageRating(mentorId);
            return ResponseEntity.ok(averageRating != null ? averageRating : 0.0);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/ratings/{menteeUsername}")
    public ResponseEntity<?> getRatingsByUsername(@PathVariable String menteeUsername) {
        try {
            // Find the mentee by username
            Mentee mentee = menteeRatingService.getMenteeByUsername(menteeUsername);
            if (mentee == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Mentee not found"));
            }

            // Retrieve all ratings for the mentee
            List<MenteeRating> ratings = menteeRatingRepository.findByMentee(mentee);
            return ResponseEntity.ok(ratings);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/rate")
    public ResponseEntity<?> rateMentorByUsername(
            @RequestParam String menteeUsername,
            @RequestParam String mentorUsername,
            @RequestParam int rating) {
        try {
            MenteeRating ratingResponse = menteeRatingService.rateMentorByUsername(menteeUsername, mentorUsername, rating);
            return ResponseEntity.ok(ratingResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/mentor/average")
    public ResponseEntity<?> getMentorAverageRatingByUsername(@RequestParam String mentorUsername) {
        try {
            Long mentorId = menteeRatingService.getMentorIdByUsername(mentorUsername);
            Double averageRating = menteeRatingService.getMentorAverageRating(mentorId);
            return ResponseEntity.ok(averageRating != null ? averageRating : 0.0);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    // Inner class for error responses
    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}