package onetomany.Mentee;

import com.fasterxml.jackson.databind.ObjectMapper;
import onetomany.MenteeSurvey.Mentee;
import onetomany.MenteeSurvey.MenteeController;
import onetomany.MenteeSurvey.MenteeRepository;
import onetomany.Users.User;
import onetomany.services.MatchMentorMenteeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenteeController.class)
public class MenteeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenteeRepository menteeRepository;

    @MockBean
    private MatchMentorMenteeService matchMentorMenteeService;

    @Test
    public void testCreateMentee_Success() throws Exception {
        // Mock session for logged-in user
        MockHttpSession session = new MockHttpSession();
        User user = new User();
        user.setId(1L);
        session.setAttribute("loggedInUser", user);

        Mentee mentee = new Mentee();
        mentee.setMajor("Computer Science");
        mentee.setClassification(Mentee.Classification.JUNIOR);
        mentee.setAreaOfMenteeship(Mentee.AreaOfMenteeship.CAREER);

        Mockito.when(menteeRepository.findByUser(user)).thenReturn(null);
        Mockito.when(menteeRepository.save(Mockito.any(Mentee.class))).thenReturn(mentee);

        mockMvc.perform(post("/mentee/create")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mentee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Mentee created successfully")));
    }

    @Test
    public void testCreateMentee_AlreadyExists() throws Exception {
        MockHttpSession session = new MockHttpSession();
        User user = new User();
        user.setId(1L);
        session.setAttribute("loggedInUser", user);

        Mentee existingMentee = new Mentee();
        Mockito.when(menteeRepository.findByUser(user)).thenReturn(existingMentee);

        Mentee mentee = new Mentee();
        mentee.setMajor("Computer Science");

        mockMvc.perform(post("/mentee/create")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mentee)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testCreateMentee_Unauthorized() throws Exception {
        Mentee mentee = new Mentee();
        mentee.setMajor("Computer Science");

        mockMvc.perform(post("/mentee/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mentee)))
                .andExpect(status().isUnauthorized());
    }
}
