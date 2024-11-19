package onetomany.Achievements;

import onetomany.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("/achievements")
@Tag(name = "Achievements Controller", description = "Manage Achievements and their associations with users.")
public class AchievementController {

    @Autowired
    private final AchievementService achievementService;

    @Autowired
    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @Operation(summary = "Test the achievements endpoint")
    @ApiResponse(responseCode = "200", description = "Endpoint is reachable")
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return new ResponseEntity<>("Test Successful", HttpStatus.OK);
    }

    @Operation(summary = "Get all achievements")
    @ApiResponse(responseCode = "200", description = "List of achievements retrieved successfully")
    @GetMapping
    public ResponseEntity<List<Achievement>> getAllAchievements() {
        List<Achievement> achievements = achievementService.getAllAchievements();
        return new ResponseEntity<>(achievements, HttpStatus.OK);
    }

    @Operation(summary = "Get achievement by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Achievement found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Achievement.class))),
            @ApiResponse(responseCode = "404", description = "Achievement not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Achievement> getAchievementById(
            @Parameter(description = "ID of the achievement to retrieve", required = true) @PathVariable Integer id) {
        Achievement achievement = achievementService.getAchievementById(id);
        if (achievement != null) {
            return new ResponseEntity<>(achievement, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Create a new achievement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Achievement created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Achievement.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input provided", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Achievement> createAchievement(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Details of the achievement to create", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Achievement.class,
                                    example = "{ \"title\": \"Achievement Title\", \"description\": \"Achievement Description\" }")))
            @RequestBody Achievement achievement) {
        Achievement createdAchievement = achievementService.createAchievement(achievement);
        return new ResponseEntity<>(createdAchievement, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an achievement by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Achievement updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Achievement.class))),
            @ApiResponse(responseCode = "404", description = "Achievement not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Achievement> updateAchievement(
            @Parameter(description = "ID of the achievement to update", required = true) @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated achievement details", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Achievement.class,
                                    example = "{ \"title\": \"Updated Title\", \"description\": \"Updated Description\" }")))
            @RequestBody Achievement achievement) {
        Achievement updatedAchievement = achievementService.updateAchievement(id, achievement);
        if (updatedAchievement != null) {
            return new ResponseEntity<>(updatedAchievement, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete an achievement by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Achievement deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Achievement not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAchievement(
            @Parameter(description = "ID of the achievement to delete", required = true) @PathVariable Integer id) {
        boolean isDeleted = achievementService.deleteAchievement(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Award an achievement to a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Achievement awarded to user"),
            @ApiResponse(responseCode = "404", description = "User or achievement not found", content = @Content)
    })
    @PostMapping("/{achievementId}/users/{userId}")
    public ResponseEntity<Void> awardAchievementToUser(
            @Parameter(description = "ID of the achievement to award", required = true) @PathVariable Integer achievementId,
            @Parameter(description = "ID of the user to receive the achievement", required = true) @PathVariable Integer userId) {
        try {
            achievementService.awardAchievementToUser(userId, achievementId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Remove an achievement from a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Achievement removed from user"),
            @ApiResponse(responseCode = "404", description = "User or achievement not found", content = @Content)
    })
    @DeleteMapping("/{achievementId}/users/{userId}")
    public ResponseEntity<Void> removeAchievementFromUser(
            @Parameter(description = "ID of the achievement to remove", required = true) @PathVariable Integer achievementId,
            @Parameter(description = "ID of the user to remove the achievement from", required = true) @PathVariable Integer userId) {
        try {
            achievementService.removeAchievementFromUser(userId, achievementId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get all achievements for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Achievements retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/users/{userId}")
    public ResponseEntity<Set<Achievement>> getUserAchievements(
            @Parameter(description = "ID of the user to retrieve achievements for", required = true) @PathVariable Integer userId) {
        try {
            Set<Achievement> achievements = achievementService.getUserAchievements(userId);
            return new ResponseEntity<>(achievements, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get all users who have a specific achievement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Achievement not found", content = @Content)
    })
    @GetMapping("/{achievementId}/users")
    public ResponseEntity<Set<User>> getAchievementUsers(
            @Parameter(description = "ID of the achievement to retrieve users for", required = true) @PathVariable Integer achievementId) {
        try {
            Set<User> users = achievementService.getAchievementUsers(achievementId);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
