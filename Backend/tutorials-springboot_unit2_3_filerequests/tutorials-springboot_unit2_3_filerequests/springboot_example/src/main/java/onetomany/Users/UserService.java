package onetomany.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import onetomany.Achievements.AchievementService;
import javax.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AchievementService achievementService;

    public User saveUserLanguage(Long userId, String languageCode) {
        // Fetch user from the database
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Set the user's language preference (assuming you have a `language` field in the `User` entity)
        user.setLanguage(Language.valueOf(languageCode));

        // Save and return the updated user
        return userRepository.save(user);
    }
    @Transactional
    public User registerNewUser(User user) {
        // Save the user first
        User savedUser = userRepository.save(user);

        // Award Profile Completion Achievement (id=12)
        try {
            achievementService.awardAchievementToUser((int) savedUser.getId(), 12);
        } catch (Exception e) {
            // Log the error or handle it appropriately
            System.err.println("Could not award profile completion achievement: " + e.getMessage());
        }

        return savedUser;
    }

    @Transactional
    public User loginUser(String username) {
        User user = userRepository.findByUsername(username);

        // Check if this is the user's first login
        if (user != null && !user.hasLoggedInBefore()) {
            // Mark the user as having logged in
            user.setFirstLoginCompleted(true);
            userRepository.save(user);

            // Award First Login Achievement (id=11)
            try {
                achievementService.awardAchievementToUser((int) user.getId(), 11);
            } catch (Exception e) {
                // Log the error or handle it appropriately
                System.err.println("Could not award first login achievement: " + e.getMessage());
            }
        }

        return user;
    }
}
