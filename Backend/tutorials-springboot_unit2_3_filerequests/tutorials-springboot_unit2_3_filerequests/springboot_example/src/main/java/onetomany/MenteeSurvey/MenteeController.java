package onetomany.MenteeSurvey;

import onetomany.Users.User;
import onetomany.services.MatchMentorMenteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/mentee")
public class MenteeController {

    @Autowired
    MenteeRepository menteeRepository;

    @Autowired
    private MatchMentorMenteeService matchMentorMenteeService;

    // Create a new mentee based on the logged-in user
    @PostMapping("/create")
    public ResponseEntity<String> createMentee(@RequestBody Mentee mentee, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            return new ResponseEntity<>("User not logged in", HttpStatus.UNAUTHORIZED);
        }

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // Check if the user is already a mentee
        Mentee existingMentee = menteeRepository.findByUser(loggedInUser);
        if (existingMentee != null) {
            return new ResponseEntity<>("User is already a mentee", HttpStatus.CONFLICT);
        }

        // Associate the mentee with the logged-in user
        mentee.setUser(loggedInUser);

        // Save the new mentee
        menteeRepository.save(mentee);

        // Trigger matching logic
        matchMentorMenteeService.findNewMatches();

        return new ResponseEntity<>("Mentee created successfully", HttpStatus.CREATED);
    }

    // Get all mentees
    @GetMapping("/all")
    public ResponseEntity<List<Mentee>> getAllMentees() {
        List<Mentee> mentee = menteeRepository.findAll();
        return ResponseEntity.ok(mentee);
    }

    @PostMapping
    public Mentee saveMentee(@RequestBody Mentee mentee) {
        Mentee savedMentee = menteeRepository.save(mentee);

        // Trigger matching after saving a mentee
        matchMentorMenteeService.findNewMatches();

        return savedMentee;
    }
}
