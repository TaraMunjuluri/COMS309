package onetomany.Matches;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MentorMenteeActivityControllerTest {

    @Mock
    private onetomany.Matches.MentorMenteeActivityRepository activityRepository;

    @Mock
    private MatchedPairRepository matchedPairRepository;

    @InjectMocks
    private onetomany.Matches.MentorMenteeActivityController activityController;

    private MatchedPairMentorMentee testMatch;
    private onetomany.Matches.MentorMenteeActivity testActivity;
    private LocalDateTime testDateTime;

    @BeforeEach
    public void setUp() {
        testDateTime = LocalDateTime.now();

        // Setup test match
        testMatch = new MatchedPairMentorMentee();
        testMatch.setId(1L);

        // Setup test activity
        testActivity = new onetomany.Matches.MentorMenteeActivity();
        testActivity.setId(1L);
        testActivity.setMatchedPair(testMatch);
        testActivity.setMeetingDate(testDateTime);
        testActivity.setMeetingNotes("Test meeting notes");
        testActivity.setInteractionType(onetomany.Matches.MentorMenteeActivity.InteractionType.MEETING);
    }

    @Test
    public void testLogMeeting_Success() {
        // Arrange
        when(matchedPairRepository.findById(1L)).thenReturn(Optional.of(testMatch));
        when(activityRepository.save(any(onetomany.Matches.MentorMenteeActivity.class))).thenReturn(testActivity);

        // Act
        ResponseEntity<?> response = activityController.logMeeting(1L, "Test meeting notes", testDateTime);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Meeting logged successfully", response.getBody());
        verify(activityRepository).save(any(onetomany.Matches.MentorMenteeActivity.class));
    }

    @Test
    public void testLogMeeting_MatchNotFound() {
        // Arrange
        when(matchedPairRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = activityController.logMeeting(99L, "Test notes", testDateTime);

        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        assertTrue(response.getBody().toString().contains("Match not found"));
    }

    @Test
    public void testGetActivityHistory_Success() {
        // Arrange
        List<onetomany.Matches.MentorMenteeActivity> activities = Arrays.asList(testActivity);
        when(matchedPairRepository.findById(1L)).thenReturn(Optional.of(testMatch));
        when(activityRepository.findByMatchedPairOrderByMeetingDateDesc(testMatch)).thenReturn(activities);

        // Act
        ResponseEntity<?> response = activityController.getActivityHistory(1L);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        List<onetomany.Matches.MentorMenteeActivity> returnedActivities = (List<onetomany.Matches.MentorMenteeActivity>) response.getBody();
        assertEquals(1, returnedActivities.size());
        assertEquals(testActivity.getMeetingNotes(), returnedActivities.get(0).getMeetingNotes());
    }

    @Test
    public void testGetActivityHistory_MatchNotFound() {
        // Arrange
        when(matchedPairRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = activityController.getActivityHistory(99L);

        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        assertTrue(response.getBody().toString().contains("Match not found"));
    }

    @Test
    public void testLogMeeting_NullMeetingDate() {
        // Act
        ResponseEntity<?> response = activityController.logMeeting(1L, "Test notes", null);

        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        assertTrue(response.getBody().toString().contains("Error"));
    }

    @Test
    public void testLogMeeting_EmptyNotes() {
        // Arrange
        when(matchedPairRepository.findById(1L)).thenReturn(Optional.of(testMatch));
        when(activityRepository.save(any(onetomany.Matches.MentorMenteeActivity.class))).thenReturn(testActivity);

        // Act
        ResponseEntity<?> response = activityController.logMeeting(1L, "", testDateTime);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        verify(activityRepository).save(any(onetomany.Matches.MentorMenteeActivity.class));
    }

    @Test
    public void testGetActivityHistory_NoActivities() {
        // Arrange
        when(matchedPairRepository.findById(1L)).thenReturn(Optional.of(testMatch));
        when(activityRepository.findByMatchedPairOrderByMeetingDateDesc(testMatch)).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<?> response = activityController.getActivityHistory(1L);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        List<onetomany.Matches.MentorMenteeActivity> returnedActivities = (List<onetomany.Matches.MentorMenteeActivity>) response.getBody();
        assertTrue(returnedActivities.isEmpty());
    }
}