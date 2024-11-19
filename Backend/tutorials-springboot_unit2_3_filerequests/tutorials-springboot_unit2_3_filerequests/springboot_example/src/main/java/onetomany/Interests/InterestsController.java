package onetomany.Interests;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import onetomany.ExceptionHandlers.InterestNotFoundException;
import onetomany.Users.User;
import onetomany.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/interests")
public class InterestsController {

    @Autowired
    private InterestsRepository interestsRepository;

    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Add a user to an interest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully added to the interest"),
            @ApiResponse(responseCode = "404", description = "User or Interest not found", content = @Content)
    })
    @PostMapping("/{interestId}/users/{userId}")
    public ResponseEntity<?> addUserToInterest(
            @Parameter(description = "ID of the interest to add the user to", required = true) @PathVariable Long interestId,
            @Parameter(description = "ID of the user to add to the interest", required = true) @PathVariable int userId) {
        Interests interest = interestsRepository.findById(interestId)
                .orElseThrow(() -> new InterestNotFoundException("Interest not found with ID: " + interestId));
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new InterestNotFoundException("User not found with ID: " + userId);
        }

        interest.addUser(user);
        interestsRepository.save(interest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Add an interest to a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Interest successfully added to the user"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PostMapping("/users/{userId}/interests")
    public ResponseEntity<?> addInterestToUser(
            @Parameter(description = "ID of the user to add the interest to", required = true) @PathVariable long userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the interest to add",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Interests.class))
            )
            @RequestBody Interests interest) {

        User user = userRepository.findById(userId);
        if (user == null) {
            throw new InterestNotFoundException("User not found with ID: " + userId);
        }

        user.setMajor(interest.getMajor());
        user.setClassification(interest.getClassification());

        Interests savedInterest = interestsRepository.save(interest);

        user.getInterests().add(savedInterest);
        savedInterest.getUsers().add(user);

        interestsRepository.save(savedInterest);
        userRepository.save(user);

        return ResponseEntity.ok(savedInterest);
    }

    @Operation(summary = "Remove a user from an interest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully removed from the interest"),
            @ApiResponse(responseCode = "404", description = "User or Interest not found", content = @Content)
    })
    @DeleteMapping("/{interestId}/users/{userId}")
    public ResponseEntity<?> removeUserFromInterest(
            @Parameter(description = "ID of the interest", required = true) @PathVariable Long interestId,
            @Parameter(description = "ID of the user to remove from the interest", required = true) @PathVariable int userId) {
        Interests interest = interestsRepository.findById(interestId)
                .orElseThrow(() -> new InterestNotFoundException("Interest not found with ID: " + interestId));
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new InterestNotFoundException("User not found with ID: " + userId);
        }

        interest.removeUser(user);
        interestsRepository.save(interest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get options for classifications and interests")
    @ApiResponse(responseCode = "200", description = "Options retrieved successfully")
    @GetMapping("/options")
    public Map<String, Object> getInterestOptions() {
        Map<String, Object> options = new HashMap<>();
        options.put("classifications", Interests.Classification.values());
        options.put("interests", Arrays.asList(
                "Technology", "Gaming", "Fitness", "Sports",
                "Music", "Art", "Photography", "Travel",
                "Reading", "Movies"
        ));
        return options;
    }

    @Operation(summary = "Get users associated with a specific interest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Interest not found", content = @Content)
    })
    @GetMapping("/{interestId}/users")
    public ResponseEntity<Set<User>> getUsersForInterest(
            @Parameter(description = "ID of the interest to retrieve users for", required = true) @PathVariable Long interestId) {
        Interests interest = interestsRepository.findById(interestId)
                .orElseThrow(() -> new InterestNotFoundException("Interest not found with ID: " + interestId));
        return ResponseEntity.ok(interest.getUsers());
    }

    @Operation(summary = "Get interests filtered by major")
    @ApiResponse(responseCode = "200", description = "Interests retrieved successfully")
    @GetMapping("/major/{major}")
    public List<Interests> getInterestsByMajor(
            @Parameter(description = "Major to filter interests by", required = true) @PathVariable String major) {
        return interestsRepository.findByMajor(major);
    }

    @Operation(summary = "Get interests filtered by classification")
    @ApiResponse(responseCode = "200", description = "Interests retrieved successfully")
    @GetMapping("/classification/{classification}")
    public List<Interests> getInterestsByClassification(
            @Parameter(description = "Classification to filter interests by", required = true) @PathVariable String classification) {
        return interestsRepository.findByClassification(classification);
    }

    @Operation(summary = "Get interests associated with a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Interests retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/users/{userId}/interests")
    public ResponseEntity<Set<Interests>> getInterestsForUser(
            @Parameter(description = "ID of the user to retrieve interests for", required = true) @PathVariable Long userId) {
        // Retrieve the User object or throw an exception
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InterestNotFoundException("User not found with ID: " + userId));

        // Get the interests associated with the user
        Set<Interests> userInterests = user.getInterests();

        return ResponseEntity.ok(userInterests);
    }
}
