package onetomany.PotentialFriends;

import onetomany.Interests.InterestsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class PotentialFriendController {

    @Autowired
    private PotentialFriendRepository potentialFriendRepository;

    @Autowired
    private InterestsRepository interestsRepository;

    @PostMapping("/update-potential-friends/{userId}")
    public ResponseEntity<?> updatePotentialFriends(@PathVariable Long userId) {
        try {
            potentialFriendRepository.deleteByUserId(userId);
            List<Object[]> sharedInterests = interestsRepository.findUsersWithSharedInterests(userId);

            for (Object[] result : sharedInterests) {
                Number friendId = (Number) result[0];
                Number sharedCount = (Number) result[1];

                PotentialFriend potentialFriend = new PotentialFriend();
                potentialFriend.setUserId(userId);
                potentialFriend.setPotentialFriendId(friendId.longValue());
                potentialFriend.setSharedInterestsCount(sharedCount.intValue());
                potentialFriend.setCommonInterestCount(sharedCount.intValue());
                potentialFriendRepository.save(potentialFriend);
            }

            return ResponseEntity.ok("Potential friends updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating potential friends: " + e.getMessage());
        }
    }

    @GetMapping("/potential-friends/{userId}")
    public ResponseEntity<List<PotentialFriend>> getPotentialFriends(@PathVariable Long userId) {
        List<PotentialFriend> potentialFriends = potentialFriendRepository.findByUserId(userId);
        return ResponseEntity.ok(potentialFriends);
    }
}