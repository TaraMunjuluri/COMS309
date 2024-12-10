//Aelia Tests

package onetomany.Achievements;

import onetomany.Users.User;
import onetomany.Users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AchievementsServiceTest {

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private AchievementService achievementService;

    private Achievement testAchievement;
    private User testUser;

    @BeforeEach
    public void setUp() {
        testAchievement = new Achievement("Test Achievement", "Test Description");
        testAchievement.setId(1);

        testUser = new User();
        testUser.setId(1);
        testUser.setAchievements(new HashSet<>());
    }

    @Test
    public void testCreateAchievement() {
        when(achievementRepository.save(testAchievement)).thenReturn(testAchievement);

        Achievement created = achievementService.createAchievement(testAchievement);

        assertNotNull(created);
        assertEquals(testAchievement.getName(), created.getName());
        verify(achievementRepository).save(testAchievement);
    }


    @Test
    public void testGetAchievementById_Exists() {
        when(achievementRepository.findById(1)).thenReturn(Optional.of(testAchievement));

        Achievement retrieved = achievementService.getAchievementById(1);

        assertNotNull(retrieved);
        assertEquals(testAchievement.getName(), retrieved.getName());
    }


    @Test
    public void testUpdateAchievement_Exists() {
        Achievement updatedDetails = new Achievement("Updated Name", "Updated Description");

        when(achievementRepository.findById(1)).thenReturn(Optional.of(testAchievement));
        when(achievementRepository.save(testAchievement)).thenReturn(testAchievement);

        Achievement updated = achievementService.updateAchievement(1, updatedDetails);

        assertNotNull(updated);
        assertEquals("Updated Name", updated.getName());
        assertEquals("Updated Description", updated.getDescription());
    }

    @Test
    public void testUpdateAchievement_NotExists() {
        Achievement updatedDetails = new Achievement("Updated Name", "Updated Description");

        when(achievementRepository.findById(99)).thenReturn(Optional.empty());

        Achievement updated = achievementService.updateAchievement(99, updatedDetails);

        assertNull(updated);
    }

    @Test
    public void testDeleteAchievement_Exists() {
        when(achievementRepository.findById(1)).thenReturn(Optional.of(testAchievement));

        boolean deleted = achievementService.deleteAchievement(1);

        assertTrue(deleted);
        verify(achievementRepository).delete(testAchievement);
    }

    @Test
    public void testDeleteAchievement_NotExists() {
        when(achievementRepository.findById(99)).thenReturn(Optional.empty());

        boolean deleted = achievementService.deleteAchievement(99);

        assertFalse(deleted);
    }

    @Test
    public void testAwardAchievementToUser_Success() {
        when(userRepository.findById(1)).thenReturn(testUser);
        when(achievementRepository.findById(1)).thenReturn(Optional.of(testAchievement));
        when(userRepository.save(testUser)).thenReturn(testUser);

        boolean awarded = achievementService.awardAchievementToUser(1, 1);

        assertTrue(awarded);
        assertTrue(testUser.getAchievements().contains(testAchievement));
        verify(userRepository).save(testUser);
    }

    @Test
    public void testAwardAchievementToUser_UserNotFound() {
        when(userRepository.findById(99)).thenReturn(null);

        boolean awarded = achievementService.awardAchievementToUser(99, 1);

        assertFalse(awarded);
    }

    @Test
    public void testAwardAchievementToUser_AchievementNotFound() {
        when(userRepository.findById(1)).thenReturn(testUser);
        when(achievementRepository.findById(99)).thenReturn(Optional.empty());

        boolean awarded = achievementService.awardAchievementToUser(1, 99);

        assertFalse(awarded);
    }

    @Test
    public void testRemoveAchievementFromUser_Success() {
        testUser.getAchievements().add(testAchievement);

        when(userRepository.findById(1)).thenReturn(testUser);
        when(achievementRepository.findById(1)).thenReturn(Optional.of(testAchievement));
        when(userRepository.save(testUser)).thenReturn(testUser);

        boolean removed = achievementService.removeAchievementFromUser(1, 1);

        assertTrue(removed);
        assertFalse(testUser.getAchievements().contains(testAchievement));
        verify(userRepository).save(testUser);
    }



}