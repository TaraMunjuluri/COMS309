package onetomany.PotentialFriends;

import onetomany.Interests.InterestsRepository;
import onetomany.Users.User;
import onetomany.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PotentialFriendsService {

    @Autowired
    private InterestsRepository interestsRepository;

    @Autowired
    private onetomany.Friends.PotentialFriendRepository potentialFriendsRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private onetomany.Friends.PotentialFriendRepository potentialFriendRepository;
    public void updatePotentialFriends() {
        List<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            Long userId = user.getId();

            // Clear existing potential friends for this user
            potentialFriendRepository.deleteByUserId(userId);

            // Find users with shared interests
            List<Long> potentialFriendIds = interestsRepository.findPotentialFriendIds(userId);

            // Save potential friends
            for (Long friendId : potentialFriendIds) {
                if (!friendId.equals(userId)) { // Avoid self-reference
                    onetomany.Friends.PotentialFriend potentialFriend = new onetomany.Friends.PotentialFriend();
                    potentialFriend.setUserId(userId);
                    potentialFriend.setPotentialFriendId(friendId);
                    potentialFriendsRepository.save(potentialFriend);
                }
            }
        }
    }


}
