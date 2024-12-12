package onetomany.Matches;

import onetomany.MenteeSurvey.Mentee;
import onetomany.MenteeSurvey.MenteeRepository;
import onetomany.MentorSurvey.MentorRepository;
import onetomany.Users.User;
import onetomany.Users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MatchRestController.class)
class MatchRestControllerTest {

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

    private User testUser;
    private User testMentor;
    private User testMentee;
    private MatchedPairMentorMentee testMatch;
    private Mentee menteeSurvey;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");

        testMentor = new User();
        testMentor.setId(2L);
        testMentor.setUsername("testMentor");

        testMentee = new User();
        testMentee.setId(3L);
        testMentee.setUsername("testMentee");

        testMatch = new MatchedPairMentorMentee();
        testMatch.setId(1L);
        testMatch.setMentor(testMentor);
        testMatch.setMentee(testMentee);

        menteeSurvey = new Mentee();
        menteeSurvey.setUser(testMentee);
        menteeSurvey.setMajor("Computer Science");
    }




    @Test
    void testGetMatchedPersons_NoMatches() throws Exception {
        // Arrange
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(testUser));
        when(matchedPairRepository.findByMentor(any(User.class))).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/matches/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testGetMatchedPersons_MenteeWithoutSurvey() throws Exception {
        // Arrange
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(testUser));
        when(matchedPairRepository.findByMentor(any(User.class))).thenReturn(Arrays.asList(testMatch));
        when(menteeRepository.findByUser(any(User.class))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/matches/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}