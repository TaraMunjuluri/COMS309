
package onetomany.Achievements;


import onetomany.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Set;


@CrossOrigin
@RestController
@RequestMapping("/achievements")
public class AchievementController {


    @Autowired
    private final AchievementService achievementService;


    @Autowired
    public AchievementController(AchievementService achievementService) {
        System.out.println("AchievementController initialized on server");
        this.achievementService = achievementService;
    }


    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return new ResponseEntity<>("Test Successful", HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<Achievement>> getAllAchievements() {
        List<Achievement> achievements = achievementService.getAllAchievements();
        return new ResponseEntity<>(achievements, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Achievement> getAchievementById(@PathVariable Integer id) {
        Achievement achievement = achievementService.getAchievementById(id);
        if (achievement != null) {
            return new ResponseEntity<>(achievement, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping
    public ResponseEntity<Achievement> createAchievement(@RequestBody Achievement achievement) {
        Achievement createdAchievement = achievementService.createAchievement(achievement);
        return new ResponseEntity<>(createdAchievement, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Achievement> updateAchievement(@PathVariable Integer id, @RequestBody Achievement achievement) {
        Achievement updatedAchievement = achievementService.updateAchievement(id, achievement);
        if (updatedAchievement != null) {
            return new ResponseEntity<>(updatedAchievement, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAchievement(@PathVariable Integer id) {
        boolean isDeleted = achievementService.deleteAchievement(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }




    @PostMapping("/{achievementId}/users/{userId}")
    public ResponseEntity<Void> awardAchievementToUser(
            @PathVariable Integer achievementId,
            @PathVariable Integer userId) {
        try {
            achievementService.awardAchievementToUser(userId, achievementId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{achievementId}/users/{userId}")
    public ResponseEntity<Void> removeAchievementFromUser(
            @PathVariable Integer achievementId,
            @PathVariable Integer userId) {
        try {
            achievementService.removeAchievementFromUser(userId, achievementId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/users/{userId}")
    public ResponseEntity<Set<Achievement>> getUserAchievements(@PathVariable Integer userId) {
        try {
            Set<Achievement> achievements = achievementService.getUserAchievements(userId);
            return new ResponseEntity<>(achievements, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{achievementId}/users")
    public ResponseEntity<Set<User>> getAchievementUsers(@PathVariable Integer achievementId) {
        try {
            Set<User> users = achievementService.getAchievementUsers(achievementId);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
