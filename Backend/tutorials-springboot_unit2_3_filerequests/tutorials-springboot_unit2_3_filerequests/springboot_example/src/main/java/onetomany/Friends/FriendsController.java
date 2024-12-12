package onetomany.Friends;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendsController {

    @Autowired
    private FriendsRepository friendsRepository;

    @Autowired
    private onetomany.Friends.PotentialFriendRepository potentialFriendRepository;

    // Add a friend from potential friends list
    @PostMapping("/{userId}/add/{friendId}")
    public ResponseEntity<?> addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        // Check if they're already friends
        if (friendsRepository.existsByUserIdAndFriendId(userId, friendId)) {
            return ResponseEntity.badRequest().body("Already friends!");
        }

        // Get potential friend record to get shared interests count
        onetomany.Friends.PotentialFriend potentialFriend = potentialFriendRepository
                .findByUserIdAndPotentialFriendId(userId, friendId)
                .orElseThrow(() -> new RuntimeException("Not in potential friends list"));

        // Create friendship records (bidirectional)
        Friends friendship1 = new Friends(userId, friendId, potentialFriend.getSharedInterestsCount());
        Friends friendship2 = new Friends(friendId, userId, potentialFriend.getSharedInterestsCount());

        friendsRepository.save(friendship1);
        friendsRepository.save(friendship2);

        return ResponseEntity.ok("Friend added successfully");
    }

    // Get all friends for a user
    @GetMapping("/{userId}/list")
    public ResponseEntity<List<Friends>> getFriends(@PathVariable Long userId) {
        List<Friends> friends = friendsRepository.findByUserId(userId);
        return ResponseEntity.ok(friends);
    }

    // Remove a friend
    @DeleteMapping("/{userId}/remove/{friendId}")
    public ResponseEntity<?> removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        friendsRepository.deleteByUserIdAndFriendId(userId, friendId);
        friendsRepository.deleteByUserIdAndFriendId(friendId, userId);
        return ResponseEntity.ok("Friend removed successfully");
    }
}