package onetomany.MentorSurvey;

import onetomany.ExceptionHandlers.MentorAlreadyExistsException;
import onetomany.ExceptionHandlers.MentorNotFoundException;
import onetomany.ExceptionHandlers.UnauthorizedException;
import onetomany.Users.User;
import onetomany.services.MatchMentorMenteeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MentorControllerTest {

    @Mock
    private MentorRepository mentorRepository;

    @Mock
    private MatchMentorMenteeService matchMentorMenteeService;

    @InjectMocks
    private MentorController mentorController;

    private MockHttpServletRequest request;
    private MockHttpSession session;
    private User testUser;
    private Mentor testMentor;

    @BeforeEach
    public void setUp() {
        // Setup mock HTTP request and session
        request = new MockHttpServletRequest();
        session = new MockHttpSession();

        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        // Setup test mentor
        testMentor = new Mentor();
        testMentor.setId(1L);
        testMentor.setUser(testUser);
        testMentor.setMajor("Computer Science");
        testMentor.setClassification(Mentor.Classification.SENIOR);
        testMentor.setAreaOfMentorship(Mentor.AreaOfMentorship.CAREER);

        // Add user to session
        session.setAttribute("loggedInUser", testUser);
        request.setSession(session);
    }



    @Test
    public void testCreateMentor_AlreadyExists() {
        // Arrange
        when(mentorRepository.findByUser(testUser)).thenReturn(testMentor);

        // Act & Assert
        assertThrows(MentorAlreadyExistsException.class, () -> {
            mentorController.createMentor(testMentor, request);
        });
    }

    @Test
    public void testCreateMentor_Unauthorized() {
        // Arrange
        request.setSession(null);

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> {
            mentorController.createMentor(testMentor, request);
        });
    }

    @Test
    public void testGetAllMentors() {
        // Arrange
        List<Mentor> mentors = Arrays.asList(testMentor);
        when(mentorRepository.findAll()).thenReturn(mentors);

        // Act
        List<Mentor> result = mentorController.getAllMentors();

        // Assert
        assertEquals(1, result.size());
        assertEquals(testMentor.getId(), result.get(0).getId());
    }



    @Test
    public void testUpdateMentor_NotFound() {
        // Arrange
        when(mentorRepository.findByUser(testUser)).thenReturn(null);

        // Act & Assert
        assertThrows(MentorNotFoundException.class, () -> {
            mentorController.updateMentor(testMentor, request);
        });
    }

    @Test
    public void testDeleteMentor_Success() {
        // Arrange
        when(mentorRepository.findByUser(testUser)).thenReturn(testMentor);
        doNothing().when(mentorRepository).delete(testMentor);

        // Act
        String result = mentorController.deleteMentor(request);

        // Assert
        assertEquals("Mentor deleted successfully", result);
        verify(mentorRepository).delete(testMentor);
    }

    @Test
    public void testDeleteMentor_NotFound() {
        // Arrange
        when(mentorRepository.findByUser(testUser)).thenReturn(null);

        // Act & Assert
        assertThrows(MentorNotFoundException.class, () -> {
            mentorController.deleteMentor(request);
        });
    }
}

@ExtendWith(MockitoExtension.class)
class MentorTest {

    private Mentor mentor;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        mentor = new Mentor(user, "Computer Science",
                Mentor.Classification.SENIOR,
                Mentor.AreaOfMentorship.CAREER);
    }

    @Test
    void testConstructor() {
        assertEquals("Computer Science", mentor.getMajor());
        assertEquals(Mentor.Classification.SENIOR, mentor.getClassification());
        assertEquals(Mentor.AreaOfMentorship.CAREER, mentor.getAreaOfMentorship());
        assertEquals(user, mentor.getUser());
    }

    @Test
    void testSetAndGetId() {
        mentor.setId(1L);
        assertEquals(1L, mentor.getId());
    }

    @Test
    void testSetAndGetMajor() {
        mentor.setMajor("Physics");
        assertEquals("Physics", mentor.getMajor());
    }

    @Test
    void testSetAndGetClassification() {
        mentor.setClassification(Mentor.Classification.JUNIOR);
        assertEquals(Mentor.Classification.JUNIOR, mentor.getClassification());
    }

    @Test
    void testSetAndGetAreaOfMentorship() {
        mentor.setAreaOfMentorship(Mentor.AreaOfMentorship.EDUCATION);
        assertEquals(Mentor.AreaOfMentorship.EDUCATION, mentor.getAreaOfMentorship());
    }

    @Test
    void testSetAndGetUser() {
        User newUser = new User();
        newUser.setId(2L);
        mentor.setUser(newUser);
        assertEquals(newUser, mentor.getUser());
    }
}