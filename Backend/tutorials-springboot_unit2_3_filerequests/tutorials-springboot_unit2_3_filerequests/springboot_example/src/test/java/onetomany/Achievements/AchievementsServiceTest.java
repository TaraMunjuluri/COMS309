//Aelia Tests

package onetomany.Achievements;

import onetomany.Users.User;
import onetomany.Users.UserController;
import onetomany.Users.UserRepository;
import onetomany.Users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockHttpServletRequest request;

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

    @BeforeEach
    void setUp2() {
        request = new MockHttpServletRequest();
    }

    @Test
    void testSignup_Success() {
        // Arrange
        User newUser = new User();
        newUser.setEmailId("test@example.com");
        newUser.setUsername("testuser");
        newUser.setPassword("password123");

        when(userRepository.findByEmailId("test@example.com")).thenReturn(null);
        when(userRepository.findByUsername("testuser")).thenReturn(null);
        when(userService.registerNewUser(newUser)).thenReturn(newUser);

        // Act
        ResponseEntity<Map<String, Object>> response = userController.signup(newUser);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("message")).isEqualTo("Signup successful");
        assertThat(response.getBody().get("user")).isEqualTo(newUser);

        verify(userRepository, times(1)).findByEmailId("test@example.com");
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(userService, times(1)).registerNewUser(newUser);
    }



}