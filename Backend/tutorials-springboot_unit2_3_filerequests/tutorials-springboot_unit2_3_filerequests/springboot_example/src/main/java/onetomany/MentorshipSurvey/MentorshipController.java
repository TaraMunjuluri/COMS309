package onetomany.MentorshipSurvey;

import com.yourpackage.users.User;// this needs to be replaced with users
import com.yourpackage.users.UserRepository; //needs to be replaced with users
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mentorship")
public class MentorshipController {

    @Autowired
    MentorshipRepository mentorshipRepository;

    @Autowired
    UserRepository userRepository;

    // Submit mentor/mentee choice
    @PostMapping("/submit")
    public ResponseEntity<String> submitMentorship(@RequestParam("userId") int userId,
                                                   @RequestParam("isMentor") boolean isMentor,
                                                   @RequestParam("surveyResponse") String surveyResponse) {
        User user = userRepository.findById(userId);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        Mentorship mentorship = new Mentorship(user, isMentor, surveyResponse);
        mentorshipRepository.save(mentorship);

        String role = isMentor ? "mentor" : "mentee";
        return new ResponseEntity<>("Survey submitted successfully. User assigned as " + role, HttpStatus.CREATED);
    }

    // Get survey status for the logged-in user
    @GetMapping("/status/{userId}")
    public ResponseEntity<?> getMentorshipStatus(@PathVariable int userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        Mentorship mentorship = mentorshipRepository.findByUser(user);
        if (mentorship == null) {
            return new ResponseEntity<>("Survey not completed.", HttpStatus.OK);
        }

        return new ResponseEntity<>(mentorship, HttpStatus.OK);
    }

    // Update mentor/mentee choice
    @PutMapping("/update")
    public ResponseEntity<String> updateMentorship(@RequestParam("userId") int userId,
                                                   @RequestParam("isMentor") boolean isMentor,
                                                   @RequestParam("surveyResponse") String surveyResponse) {
        User user = userRepository.findById(userId);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        Mentorship mentorship = mentorshipRepository.findByUser(user);
        if (mentorship == null) {
            return new ResponseEntity<>("Survey not found for this user", HttpStatus.NOT_FOUND);
        }

        mentorship.setMentor(isMentor);
        mentorship.setSurveyResponse(surveyResponse);
        mentorshipRepository.save(mentorship);

        String role = isMentor ? "mentor" : "mentee";
        return new ResponseEntity<>("Survey updated successfully. User assigned as " + role, HttpStatus.OK);
    }
}
