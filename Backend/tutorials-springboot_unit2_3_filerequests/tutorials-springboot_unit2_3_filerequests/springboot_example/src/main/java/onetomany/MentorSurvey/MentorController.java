package onetomany.MentorSurvey;

import onetomany.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/mentor")
public class MentorController {

    @Autowired
    MentorRepository mentorRepository;

    // Create a new mentor based on the logged-in user
    @PostMapping("/create")
    public ResponseEntity<String> createMentor(@RequestBody Mentor mentor, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            return new ResponseEntity<>("User not logged in", HttpStatus.UNAUTHORIZED);
        }

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // Check if the user is already a mentor
        Mentor existingMentor = mentorRepository.findByUser(loggedInUser);
        if (existingMentor != null) {
            return new ResponseEntity<>("User is already a mentor", HttpStatus.CONFLICT);
        }

        // Associate the mentor with the logged-in user
        mentor.setUser(loggedInUser);

        // Save the new mentor
        mentorRepository.save(mentor);

        return new ResponseEntity<>("Mentor created successfully", HttpStatus.CREATED);
    }
    // Get all mentors
    @GetMapping("/all")
    public ResponseEntity<List<Mentor>> getAllMentors() {
        List<Mentor> mentors = mentorRepository.findAll();
        return ResponseEntity.ok(mentors);
    }


}
