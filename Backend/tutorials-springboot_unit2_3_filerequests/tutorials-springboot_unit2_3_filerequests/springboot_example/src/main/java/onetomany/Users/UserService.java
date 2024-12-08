package onetomany.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUserLanguage(Long userId, String languageCode) {
        // Fetch user from the database
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Set the user's language preference (assuming you have a `language` field in the `User` entity)
        user.setLanguage(Language.valueOf(languageCode));

        // Save and return the updated user
        return userRepository.save(user);
    }
}
