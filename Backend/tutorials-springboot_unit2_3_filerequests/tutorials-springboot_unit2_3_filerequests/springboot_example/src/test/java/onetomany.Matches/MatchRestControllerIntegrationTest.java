package onetomany.Matches;

import onetomany.MenteeSurvey.MenteeRepository;
import onetomany.MentorSurvey.MentorRepository;
import onetomany.Users.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MatchRestController.class)
public class MatchRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MatchedPairRepository matchedPairRepository;

    @MockBean
    private MentorRepository mentorRepository;

    @MockBean
    private MenteeRepository menteeRepository;



    @Test
    public void testGetMatchedPersons_UserNotFound() throws Exception {
        // Arrange: Mock the UserRepository to throw an exception when looking for a non-existent user
        when(userRepository.findById(999L))
                .thenThrow(new IllegalArgumentException("User not found with ID: 999"));

        // Act & Assert: Perform the GET request and expect a 404 status with the appropriate message
        mockMvc.perform(get("/api/matches/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found with ID: 999"));
    }












}
