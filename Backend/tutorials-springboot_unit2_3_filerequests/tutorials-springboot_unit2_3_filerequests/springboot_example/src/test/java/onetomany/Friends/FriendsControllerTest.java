package onetomany.Friends;

import onetomany.PotentialFriends.PotentialFriend;
import onetomany.PotentialFriends.PotentialFriendRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FriendsControllerTest {

    @Mock
    private FriendsRepository friendsRepository;

    @Mock
    private PotentialFriendRepository potentialFriendRepository;

    @InjectMocks
    private FriendsController friendsController;

    private Friends testFriendship;
    private PotentialFriend testPotentialFriend;

    @BeforeEach
    public void setUp() {
        // Set up test friendship
        testFriendship = new Friends(1L, 2L, 3);
        testFriendship.setId(1L);
        testFriendship.setFriendsSince(LocalDateTime.now());

        // Set up test potential friend
        testPotentialFriend = new PotentialFriend();
        testPotentialFriend.setUserId(1L);
        testPotentialFriend.setPotentialFriendId(2L);
        testPotentialFriend.setSharedInterestsCount(3);
    }

    @Test
    public void testAddFriend_Success() {
        // Arrange
        when(friendsRepository.existsByUserIdAndFriendId(1L, 2L)).thenReturn(false);
        when(potentialFriendRepository.findByUserIdAndPotentialFriendId(1L, 2L))
                .thenReturn(Optional.of(testPotentialFriend));
        when(friendsRepository.save(any(Friends.class))).thenReturn(testFriendship);

        // Act
        ResponseEntity<?> response = friendsController.addFriend(1L, 2L);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Friend added successfully", response.getBody());
        verify(friendsRepository, times(2)).save(any(Friends.class)); // Should save twice for bidirectional friendship
    }

    @Test
    public void testAddFriend_AlreadyFriends() {
        // Arrange
        when(friendsRepository.existsByUserIdAndFriendId(1L, 2L)).thenReturn(true);

        // Act
        ResponseEntity<?> response = friendsController.addFriend(1L, 2L);

        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Already friends!", response.getBody());
    }

    @Test
    public void testAddFriend_NotInPotentialFriends() {
        // Arrange
        when(friendsRepository.existsByUserIdAndFriendId(1L, 2L)).thenReturn(false);
        when(potentialFriendRepository.findByUserIdAndPotentialFriendId(1L, 2L))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = friendsController.addFriend(1L, 2L);

        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        assertTrue(response.getBody().toString().contains("Not in potential friends list"));
    }

    @Test
    public void testGetFriends_Success() {
        // Arrange
        List<Friends> expectedFriends = Arrays.asList(testFriendship);
        when(friendsRepository.findByUserId(1L)).thenReturn(expectedFriends);

        // Act
        ResponseEntity<List<Friends>> response = friendsController.getFriends(1L);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(expectedFriends, response.getBody());
    }

    @Test
    public void testGetFriends_NoFriends() {
        // Arrange
        when(friendsRepository.findByUserId(1L)).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<List<Friends>> response = friendsController.getFriends(1L);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    public void testRemoveFriend_Success() {
        // Act
        ResponseEntity<?> response = friendsController.removeFriend(1L, 2L);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Friend removed successfully", response.getBody());
        verify(friendsRepository).deleteByUserIdAndFriendId(1L, 2L);
        verify(friendsRepository).deleteByUserIdAndFriendId(2L, 1L);
    }
}

// Add entity test class
@ExtendWith(MockitoExtension.class)
class FriendsTest {

    private Friends friends;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        testDateTime = LocalDateTime.now();
        friends = new Friends();
    }

    @Test
    void testConstructor() {
        Friends friends = new Friends(1L, 2L, 3);
        assertEquals(1L, friends.getUserId());
        assertEquals(2L, friends.getFriendId());
        assertEquals(3, friends.getSharedInterestsCount());
    }

    @Test
    void testSetAndGetId() {
        Long id = 1L;
        friends.setId(id);
        assertEquals(id, friends.getId());
    }

    @Test
    void testSetAndGetUserId() {
        Long userId = 1L;
        friends.setUserId(userId);
        assertEquals(userId, friends.getUserId());
    }

    @Test
    void testSetAndGetFriendId() {
        Long friendId = 2L;
        friends.setFriendId(friendId);
        assertEquals(friendId, friends.getFriendId());
    }

    @Test
    void testSetAndGetSharedInterestsCount() {
        Integer count = 3;
        friends.setSharedInterestsCount(count);
        assertEquals(count, friends.getSharedInterestsCount());
    }

    @Test
    void testSetAndGetFriendsSince() {
        friends.setFriendsSince(testDateTime);
        assertEquals(testDateTime, friends.getFriendsSince());
    }
}