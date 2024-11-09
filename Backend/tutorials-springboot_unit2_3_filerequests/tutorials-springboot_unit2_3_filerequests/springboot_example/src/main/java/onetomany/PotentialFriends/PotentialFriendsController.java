package onetomany.PotentialFriends;

import onetomany.Users.User;
import onetomany.Users.UserRepository;
import onetomany.Interests.Interests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/potential-friends")
public class PotentialFriendsController {

    @Autowired
    private PotentialFriendsRepository potentialFriendsRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/update/{userId}")
    public ResponseEntity<?> updatePotentialFriends(@PathVariable long userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Clear existing potential friends
        potentialFriendsRepository.deleteByUserId(userId);

        // Get current user's interests
        Set<Interests> userInterests = user.getInterests();
        List<String> userInterestsList = new ArrayList<>();
        for (Interests interest : userInterests) {
            userInterestsList.addAll(interest.getInterests());
        }

        // Find potential friends
        List<User> allUsers = userRepository.findAll();
        for (User otherUser : allUsers) {
            if (otherUser.getId() != userId) {
                Set<String> commonInterests = new HashSet<>();

                // Find common interests
                for (Interests otherInterest : otherUser.getInterests()) {
                    for (String interest : otherInterest.getInterests()) {
                        if (userInterestsList.contains(interest)) {
                            commonInterests.add(interest);
                        }
                    }
                }

                // If there are common interests, create a potential friend
                if (!commonInterests.isEmpty()) {
                    PotentialFriends potentialFriend = new PotentialFriends(userId, otherUser.getId(), commonInterests);
                    potentialFriend.setUser(user);
                    potentialFriend.setPotentialFriend(otherUser);
                    potentialFriendsRepository.save(potentialFriend);
                }
            }
        }

        return getPotentialFriends(userId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPotentialFriends(@PathVariable long userId) {
        List<PotentialFriends> potentialFriends = potentialFriendsRepository.findByUserIdOrderByCommonInterestCountDesc(userId);

        List<Map<String, Object>> response = new ArrayList<>();
        for (PotentialFriends pf : potentialFriends) {
            Map<String, Object> pfData = new HashMap<>();
            User potentialFriend = pf.getPotentialFriend();

            pfData.put("id", pf.getId());
            pfData.put("userId", pf.getUserId());
            pfData.put("potentialFriendId", pf.getPotentialFriendId());
            pfData.put("name", potentialFriend.getName());
            pfData.put("major", potentialFriend.getMajor());
            pfData.put("classification", potentialFriend.getClassification());
            pfData.put("commonInterests", pf.getCommonInterests());
            pfData.put("commonInterestCount", pf.getCommonInterestCount());

            response.add(pfData);
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePotentialFriend(@PathVariable Long id) {
        potentialFriendsRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}