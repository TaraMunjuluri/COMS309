package onetomany.PotentialFriends;

import onetomany.Friends.PotentialFriend;

import onetomany.Interests.InterestsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class PotentialFriendController {

    @Autowired
    private onetomany.Friends.PotentialFriendRepository potentialFriendRepository;

    @Autowired
    private InterestsRepository interestsRepository;

    @PostMapping("/update-potential-friends/{userId}")
    public ResponseEntity<String> updatePotentialFriends(@PathVariable Long userId) {
        // Delete existing potential friends for this user
        List<onetomany.Friends.PotentialFriend> existingFriends = potentialFriendRepository.findByUserId(userId);
        existingFriends.forEach(friend -> potentialFriendRepository.delete(friend));
        // Find users with shared interests
        List<Object[]> sharedInterests = interestsRepository.findUsersWithSharedInterests(userId);

        // Create new potential friend entries
        for (Object[] result : sharedInterests) {
            Long friendId = (Long) result[0];
            Long sharedCount = (Long) result[1];

            onetomany.Friends.PotentialFriend potentialFriend = new PotentialFriend();
            potentialFriend.setUserId(userId);
            potentialFriend.setPotentialFriendId(friendId);
            potentialFriend.setSharedInterestsCount(sharedCount.intValue());
            potentialFriendRepository.save(potentialFriend);
        }

        return ResponseEntity.ok("Potential friends updated successfully");
    }

    @GetMapping("/potential-friends/{userId}")
    public ResponseEntity<List<onetomany.Friends.PotentialFriend>> getPotentialFriends(@PathVariable Long userId) {
        List<onetomany.Friends.PotentialFriend> potentialFriends = potentialFriendRepository.findByUserId(userId);
        return ResponseEntity.ok(potentialFriends);
    }
}