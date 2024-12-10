package onetomany.Interests;

import onetomany.Users.User;
import onetomany.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/interests")
public class InterestsController {

    @Autowired
    private InterestsRepository interestsRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/users/{userId}/interests")
    public ResponseEntity<String> addInterestsForUser(
            @PathVariable Long userId, @RequestBody List<String> interests) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        for (String interestName : interests) {
            Interests interest = interestsRepository.findByName(interestName)
                    .orElseGet(() -> {
                        // Create a new interest with classification, user, and name
                        Interests newInterest = new Interests();
                        newInterest.setName(interestName);
                        newInterest.setClassification(user.getClassification());
                        newInterest.setUserId(user.getId());
                        return interestsRepository.save(newInterest);
                    });

            if (!user.getInterests().contains(interest)) {
                user.getInterests().add(interest);
            }
        }

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Interests added successfully.");
    }


    // Get interests for a user
    @GetMapping("/users/{userId}/interests")
    public ResponseEntity<Set<Interests>> getInterestsForUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return ResponseEntity.ok(user.getInterests());
    }
}
