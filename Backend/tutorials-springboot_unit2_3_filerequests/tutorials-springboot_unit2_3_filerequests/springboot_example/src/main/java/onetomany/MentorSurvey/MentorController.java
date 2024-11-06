package onetomany.MentorSurvey;

import onetomany.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import onetomany.services.MatchMentorMenteeService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/mentor")
public class MentorController {

    @Autowired
    MentorRepository mentorRepository;

    @Autowired
    private MatchMentorMenteeService matchMentorMenteeService; // Injecting the service

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

        // Trigger matching logic
        matchMentorMenteeService.findMatches();

        return new ResponseEntity<>("Mentor created successfully", HttpStatus.CREATED);
    }

    // Get all mentors
    @GetMapping("/all")
    public ResponseEntity<List<Mentor>> getAllMentors() {
        List<Mentor> mentors = mentorRepository.findAll();
        return ResponseEntity.ok(mentors);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMentor(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            return new ResponseEntity<>("User not logged in", HttpStatus.UNAUTHORIZED);
        }

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // Find the existing mentor record for the logged-in user
        Mentor existingMentor = mentorRepository.findByUser(loggedInUser);
        if (existingMentor == null) {
            return new ResponseEntity<>("Mentor not found", HttpStatus.NOT_FOUND);
        }

        // Delete the mentor record
        mentorRepository.delete(existingMentor);

        return new ResponseEntity<>("Mentor deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateMentor(@RequestBody Mentor updatedMentor, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        // Check if the user is logged in
        if (session == null || session.getAttribute("loggedInUser") == null) {
            return new ResponseEntity<>("User not logged in", HttpStatus.UNAUTHORIZED);
        }

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // Find the existing mentor record for the logged-in user
        Mentor existingMentor = mentorRepository.findByUser(loggedInUser);
        if (existingMentor == null) {
            return new ResponseEntity<>("Mentor not found", HttpStatus.NOT_FOUND);
        }

        // Update the mentor's details
        existingMentor.setMajor(updatedMentor.getMajor());
        existingMentor.setClassification(updatedMentor.getClassification());
        existingMentor.setAreaOfMentorship(updatedMentor.getAreaOfMentorship());

        // Save the updated mentor information
        mentorRepository.save(existingMentor);

        // Trigger matching logic after updating
        matchMentorMenteeService.findMatches();

        return new ResponseEntity<>("Mentor updated successfully", HttpStatus.OK);
    }

    @PostMapping
    public Mentor saveMentor(@RequestBody Mentor mentor) {
        Mentor savedMentor = mentorRepository.save(mentor);

        // Trigger matching after saving a mentor
        matchMentorMenteeService.findMatches();

        return savedMentor;
    }
}
