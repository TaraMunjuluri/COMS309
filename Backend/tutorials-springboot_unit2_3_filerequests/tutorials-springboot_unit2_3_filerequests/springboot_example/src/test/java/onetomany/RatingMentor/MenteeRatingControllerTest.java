package onetomany.RatingMentor;

// Aelia Tests


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenteeRatingControllerTest {

    @Mock
    private MenteeRatingService menteeRatingService;

    @InjectMocks
    private MenteeRatingController menteeRatingController;

    private MenteeRating testRating;

    @BeforeEach
    public void setUp() {
        testRating = new MenteeRating();
        testRating.setId(1L);
        testRating.setRating(5);
    }

    @Test
    void testRateMentor_Success() {
        // Arrange
        Long menteeId = 1L;
        Long mentorId = 2L;
        int rating = 5;

        when(menteeRatingService.rateMentor(menteeId, mentorId, rating)).thenReturn(testRating);

        // Act
        ResponseEntity<?> response = menteeRatingController.rateMentor(menteeId, mentorId, rating);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(testRating);

        verify(menteeRatingService, times(1)).rateMentor(menteeId, mentorId, rating);
    }

    @Test
    void testGetMentorAverageRating_Success() {
        // Arrange
        Long mentorId = 2L;
        Double averageRating = 4.5;

        when(menteeRatingService.getMentorAverageRating(mentorId)).thenReturn(averageRating);

        // Act
        ResponseEntity<?> response = menteeRatingController.getMentorAverageRating(mentorId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(averageRating);

        verify(menteeRatingService, times(1)).getMentorAverageRating(mentorId);
    }
}
