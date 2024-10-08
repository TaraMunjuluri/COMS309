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

    // Create a new mentor based on JSON input
    @PostMapping("/create")
    public ResponseEntity<String> createMentor(@RequestBody Mentor mentor) {

        // Check if the user is already a mentor
        Mentor existingMentor = mentorRepository.findByUserId(mentor.getUserId());
        if (existingMentor != null) {
            return new ResponseEntity<>("User is already a mentor", HttpStatus.CONFLICT);
        }

        // Create and save a new mentor
        mentorRepository.save(mentor);

        return new ResponseEntity<>("Mentor created successfully", HttpStatus.CREATED);
    }
}
