package onetomany.MentorSurvey;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import onetomany.ExceptionHandlers.MentorAlreadyExistsException;
import onetomany.ExceptionHandlers.MentorNotFoundException;
import onetomany.ExceptionHandlers.UnauthorizedException;
import onetomany.Users.User;
import onetomany.services.MatchMentorMenteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/mentor")
public class MentorController {

    @Autowired
    MentorRepository mentorRepository;

    @Autowired
    private MatchMentorMenteeService matchMentorMenteeService;

    @Operation(summary = "Create a new mentor for the logged-in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mentor created successfully"),
            @ApiResponse(responseCode = "401", description = "User not logged in", content = @Content),
            @ApiResponse(responseCode = "409", description = "User is already a mentor", content = @Content)
    })
    @PostMapping("/create")
    public String createMentor(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the mentor to create",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Mentor.class))
            )
            @RequestBody Mentor mentor, HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            throw new UnauthorizedException("User not logged in");
        }

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // Check if the user is already a mentor
        Mentor existingMentor = mentorRepository.findByUser(loggedInUser);
        if (existingMentor != null) {
            throw new MentorAlreadyExistsException("User is already a mentor");
        }

        // Associate the mentor with the logged-in user
        mentor.setUser(loggedInUser);

        // Save the new mentor
        mentorRepository.save(mentor);

        // Trigger matching logic
        matchMentorMenteeService.findNewMatches();

        return "Mentor created successfully";
    }

    @Operation(summary = "Retrieve all mentors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mentors retrieved successfully")
    })
    @GetMapping("/all")
    public List<Mentor> getAllMentors() {
        return mentorRepository.findAll();
    }

    @Operation(summary = "Delete the logged-in user's mentor record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mentor deleted successfully"),
            @ApiResponse(responseCode = "401", description = "User not logged in", content = @Content),
            @ApiResponse(responseCode = "404", description = "Mentor not found", content = @Content)
    })
    @DeleteMapping("/delete")
    public String deleteMentor(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            throw new UnauthorizedException("User not logged in");
        }

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // Find the existing mentor record for the logged-in user
        Mentor existingMentor = mentorRepository.findByUser(loggedInUser);
        if (existingMentor == null) {
            throw new MentorNotFoundException("Mentor not found");
        }

        // Delete the mentor record
        mentorRepository.delete(existingMentor);

        return "Mentor deleted successfully";
    }

    @Operation(summary = "Update the logged-in user's mentor record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mentor updated successfully"),
            @ApiResponse(responseCode = "401", description = "User not logged in", content = @Content),
            @ApiResponse(responseCode = "404", description = "Mentor not found", content = @Content)
    })
    @PutMapping("/update")
    public String updateMentor(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated mentor details",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Mentor.class))
            )
            @RequestBody Mentor updatedMentor, HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            throw new UnauthorizedException("User not logged in");
        }

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // Find the existing mentor record for the logged-in user
        Mentor existingMentor = mentorRepository.findByUser(loggedInUser);
        if (existingMentor == null) {
            throw new MentorNotFoundException("Mentor not found");
        }

        // Update the mentor's details
        existingMentor.setMajor(updatedMentor.getMajor());
        existingMentor.setClassification(updatedMentor.getClassification());
        existingMentor.setAreaOfMentorship(updatedMentor.getAreaOfMentorship());

        // Save the updated mentor information
        mentorRepository.save(existingMentor);

        // Trigger matching logic after updating
        matchMentorMenteeService.findNewMatches();

        return "Mentor updated successfully";
    }

    @Operation(summary = "Save a new mentor record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mentor saved successfully")
    })
    @PostMapping
    public Mentor saveMentor(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the mentor to save",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Mentor.class))
            )
            @RequestBody Mentor mentor) {

        Mentor savedMentor = mentorRepository.save(mentor);

        // Trigger matching after saving a mentor
        matchMentorMenteeService.findNewMatches();

        return savedMentor;
    }
}
