package onetomany.MenteeSurvey;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import onetomany.ExceptionHandlers.MenteeAlreadyExistsException;
import onetomany.ExceptionHandlers.UnauthorizedException;
import onetomany.Users.User;
import onetomany.services.MatchMentorMenteeService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Operation(summary = "Create a new mentee for the logged-in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mentee created successfully"),
            @ApiResponse(responseCode = "401", description = "User not logged in", content = @Content),
            @ApiResponse(responseCode = "409", description = "User is already a mentee", content = @Content)
    })
    @PostMapping("/create")
    public String createMentee(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the mentee to create",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Mentee.class))
            )
            @RequestBody Mentee mentee, HttpServletRequest request) {

        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            throw new UnauthorizedException("User not logged in");
        }

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // Check if the user is already a mentee
        Mentee existingMentee = menteeRepository.findByUser(loggedInUser);
        if (existingMentee != null) {
            throw new MenteeAlreadyExistsException("User is already a mentee");
        }

        // Associate the mentee with the logged-in user
        mentee.setUser(loggedInUser);

        // Save the new mentee
        menteeRepository.save(mentee);

        // Trigger matching logic
        matchMentorMenteeService.findNewMatches();

        return "Mentee created successfully";
    }

    @Operation(summary = "Retrieve all mentees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mentees retrieved successfully")
    })
    @GetMapping("/all")
    public List<Mentee> getAllMentees() {
        return menteeRepository.findAll();
    }

    @Operation(summary = "Save a new mentee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mentee saved successfully")
    })
    @PostMapping
    public Mentee saveMentee(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the mentee to save",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Mentee.class))
            )
            @RequestBody Mentee mentee) {

        // Save the mentee
        Mentee savedMentee = menteeRepository.save(mentee);

        // Trigger matching after saving a mentee
        matchMentorMenteeService.findNewMatches();

        return savedMentee;
    }
}
