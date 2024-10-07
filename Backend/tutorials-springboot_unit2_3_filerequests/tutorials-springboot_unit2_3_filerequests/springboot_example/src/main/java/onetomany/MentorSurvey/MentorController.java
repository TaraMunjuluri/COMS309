package onetomany.MentorSurvey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mentor")
public class MentorController {

    @Autowired
    MentorRepository mentorRepository;

    // Create a new mentor based on user inputs
    @PostMapping("/create")
    public ResponseEntity<String> createMentor(@RequestParam("userId") int userId,
                                               @RequestParam("major") String major,
                                               @RequestParam("classification") Mentor.Classification classification,
                                               @RequestParam("areaOfMentorship") Mentor.AreaOfMentorship areaOfMentorship) {

        // Check if the user is already a mentor
        Mentor existingMentor = mentorRepository.findByUserId(userId);
        if (existingMentor != null) {
            return new ResponseEntity<>("User is already a mentor", HttpStatus.CONFLICT);
        }

        // Create and save a new mentor
        Mentor mentor = new Mentor(userId, major, classification, areaOfMentorship);
        mentorRepository.save(mentor);

        return new ResponseEntity<>("Mentor created successfully", HttpStatus.CREATED);
    }





}
