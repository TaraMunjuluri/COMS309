package onetomany.PotentialFrends;

import onetomany.Interests.InterestsRepository;
import onetomany.PotentialFriends.PotentialFriend;
import onetomany.PotentialFriends.PotentialFriendController;
import onetomany.PotentialFriends.PotentialFriendRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PotentialFriendControllerTest {

    @Mock
    private PotentialFriendRepository potentialFriendRepository;

    @Mock
    private InterestsRepository interestsRepository;

    @InjectMocks
    private PotentialFriendController potentialFriendController;

    private PotentialFriend testPotentialFriend;
    private Object[] testSharedInterest;

    @BeforeEach
    public void setUp() {
        // Set up test potential friend
        testPotentialFriend = new PotentialFriend();
        testPotentialFriend.setId(1L);
        testPotentialFriend.setUserId(1L);
        testPotentialFriend.setPotentialFriendId(2L);
        testPotentialFriend.setSharedInterestsCount(3);
        testPotentialFriend.setCommonInterestCount(3);

        // Set up test shared interest array
        testSharedInterest = new Object[]{2L, 3L};
    }

    @Test
    public void testUpdatePotentialFriends_Success() {
        // Arrange
        List<Object[]> sharedInterests = Arrays.asList(new Object[][]{testSharedInterest});
        when(interestsRepository.findUsersWithSharedInterests(1L))
                .thenReturn(sharedInterests);
        when(potentialFriendRepository.save(any(PotentialFriend.class)))
                .thenReturn(testPotentialFriend);

        // Act
        ResponseEntity<?> response = potentialFriendController.updatePotentialFriends(1L);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Potential friends updated successfully", response.getBody());
        verify(potentialFriendRepository).deleteByUserId(1L);
        verify(potentialFriendRepository).save(any(PotentialFriend.class));
    }

    @Test
    public void testGetPotentialFriends_Success() {
        // Arrange
        List<PotentialFriend> expectedFriends = Arrays.asList(testPotentialFriend);
        when(potentialFriendRepository.findByUserId(1L)).thenReturn(expectedFriends);

        // Act
        ResponseEntity<List<PotentialFriend>> response = potentialFriendController.getPotentialFriends(1L);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(expectedFriends, response.getBody());
    }
}