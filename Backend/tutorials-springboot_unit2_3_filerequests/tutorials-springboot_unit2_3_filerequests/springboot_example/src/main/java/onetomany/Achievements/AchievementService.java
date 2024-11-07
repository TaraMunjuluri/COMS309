package onetomany.Achievements;


import onetomany.Users.User;
import onetomany.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.Optional;


@Service
public class AchievementService {


    @Autowired
    private AchievementRepository achievementRepository;


    @Autowired
    private UserRepository userRepository;


    // Existing CRUD methods
    public Achievement createAchievement(Achievement achievement) {
        return achievementRepository.save(achievement);
    }


    public List<Achievement> getAllAchievements() {
        return achievementRepository.findAll();
    }


    public Achievement getAchievementById(Integer id) {
        return achievementRepository.findById(id).orElse(null);
    }


    public Achievement updateAchievement(Integer id, Achievement achievementDetails) {
        Optional<Achievement> achievementOpt = achievementRepository.findById(id);
        if (!achievementOpt.isPresent()) {
            return null;
        }


        Achievement achievement = achievementOpt.get();
        achievement.setName(achievementDetails.getName());
        achievement.setDescription(achievementDetails.getDescription());
        return achievementRepository.save(achievement);
    }


    public boolean deleteAchievement(Integer id) {
        Optional<Achievement> achievementOpt = achievementRepository.findById(id);
        if (!achievementOpt.isPresent()) {
            return false;
        }


        achievementRepository.delete(achievementOpt.get());
        return true;
    }


    // New methods for managing user achievements
    @Transactional
    public boolean awardAchievementToUser(Integer userId, Integer achievementId) {
        Optional<User> userOpt = Optional.ofNullable(userRepository.findById(userId));
        Optional<Achievement> achievementOpt = achievementRepository.findById(achievementId);


        if (!userOpt.isPresent() || !achievementOpt.isPresent()) {
            return false;
        }


        User user = userOpt.get();
        Achievement achievement = achievementOpt.get();


        Set<Achievement> userAchievements = user.getAchievements();
        userAchievements.add(achievement);
        user.setAchievements(userAchievements);


        userRepository.save(user);
        return true;
    }




    @Transactional
    public boolean removeAchievementFromUser(Integer userId, Integer achievementId) {
        Optional<User> userOpt = Optional.ofNullable(userRepository.findById(userId));
        Optional<Achievement> achievementOpt = achievementRepository.findById(achievementId);


        if (!userOpt.isPresent() || !achievementOpt.isPresent()) {
            return false;
        }


        User user = userOpt.get();
        Achievement achievement = achievementOpt.get();


        Set<Achievement> userAchievements = user.getAchievements();
        userAchievements.remove(achievement);
        user.setAchievements(userAchievements);


        userRepository.save(user);
        return true;
    }


    public Set<Achievement> getUserAchievements(Integer userId) {
        Optional<User> userOpt = Optional.ofNullable(userRepository.findById(userId));
        return userOpt.map(User::getAchievements).orElse(null);
    }


    public Set<User> getAchievementUsers(Integer achievementId) {
        Optional<Achievement> achievementOpt = achievementRepository.findById(achievementId);
        return achievementOpt.map(Achievement::getUsers).orElse(null);
    }
    @PostConstruct
    public void testRepository() {
        // Test repository by fetching all achievements
        List<Achievement> achievements = achievementRepository.findAll();
        System.out.println("Loaded achievements: " + achievements);
    }
//    @PostConstruct
//    public void init() {
//        if (achievementRepository.count() == 0) {
//            achievementRepository.save(new Achievement("First Login", "Awarded for first login"));
//            achievementRepository.save(new Achievement("Profile Complete", "Awarded for completing profile"));
//        }
//    }
}
