package onetomany.MenteeSurvey;

import onetomany.MenteeSurvey.Mentee;
import onetomany.MenteeSurvey.MenteeRepository;
import onetomany.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/mentee")
public class MenteeController {

    @Autowired
    MenteeRepository menteeRepository;

    // Create a new mentee based on the logged-in user
    @PostMapping("/create")
    public ResponseEntity<String> createMentee(@RequestBody onetomany.MenteeSurvey.Mentee mentee, HttpServletRequest request) {
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

        return new ResponseEntity<>("Mentee created successfully", HttpStatus.CREATED);
    }
}
