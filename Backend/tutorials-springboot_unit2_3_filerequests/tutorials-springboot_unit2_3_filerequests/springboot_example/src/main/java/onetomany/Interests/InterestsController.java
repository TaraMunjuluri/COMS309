package onetomany.Interests;

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

    @PostMapping("/{interestId}/users/{userId}")
    public ResponseEntity<?> addUserToInterest(
            @PathVariable Long interestId,
            @PathVariable int userId) {
        Interests interest = interestsRepository.findById(interestId).orElse(null);
        User user = userRepository.findById(userId);  // Remove the cast

        if (interest == null || user == null) {
            return ResponseEntity.notFound().build();
        }

        interest.addUser(user);  // Remove the cast
        interestsRepository.save(interest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{userId}/interests")
    public ResponseEntity<?> addInterestToUser(
            @PathVariable long userId,
            @RequestBody Interests interest) {

        User user = userRepository.findById(userId).orElse(null);
        if(user == null) {
            return ResponseEntity.notFound().build();
        }

        // Update user's major and classification from the interest
        user.setMajor(interest.getMajor());
        user.setClassification(interest.getClassification());

        // Save the interest first
        Interests savedInterest = interestsRepository.save(interest);

        // Add the interest to user and user to interest
        user.getInterests().add(savedInterest);
        savedInterest.getUsers().add(user);

        // Save both user and interest
        interestsRepository.save(savedInterest);
        userRepository.save(user);

        return ResponseEntity.ok(savedInterest);
    }

    @DeleteMapping("/{interestId}/users/{userId}")
    public ResponseEntity<?> removeUserFromInterest(
            @PathVariable Long interestId,
            @PathVariable int userId) {
        Interests interest = interestsRepository.findById(interestId).orElse(null);
        User user = userRepository.findById(userId);  // Remove the cast
        if (interest == null || user == null) {
            return ResponseEntity.notFound().build();
        }

        interest.removeUser(user);  // Remove the cast
        interestsRepository.save(interest);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/options")
    public Map<String, Object> getInterestOptions() {
        Map<String, Object> options = new HashMap<>();
        options.put("classifications", Interests.Classification.values());
        options.put("interests", Arrays.asList(
                 "Technology","Gaming", "Fitness", "Sports",
                "Music", "Art", "Photography", "Travel",
                "Reading", "Movies"

        ));
        return options;
    }
    @GetMapping("/{interestId}/users")
    public ResponseEntity<Set<User>> getUsersForInterest(@PathVariable Long interestId) {
        return interestsRepository.findById(interestId)
                .map(interest -> ResponseEntity.ok(interest.getUsers()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/major/{major}")
    public List<Interests> getInterestsByMajor(@PathVariable String major) {
        return interestsRepository.findByMajor(major);
    }

    @GetMapping("/classification/{classification}")
    public List<Interests> getInterestsByClassification(@PathVariable String classification) {
        return interestsRepository.findByClassification(classification);
    }
}