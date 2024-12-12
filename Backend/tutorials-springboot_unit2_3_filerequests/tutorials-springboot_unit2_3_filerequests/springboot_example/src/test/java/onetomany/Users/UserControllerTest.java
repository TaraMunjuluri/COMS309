// Aelia Tests

package onetomany.Users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
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

    @Test
    void testLogin_InvalidCredentials() {
        // Arrange
        User loginUser = new User();
        loginUser.setEmailId("test@example.com");
        loginUser.setPassword("wrongpassword");

        User existingUser = new User();
        existingUser.setEmailId("test@example.com");
        existingUser.setPassword("correctpassword");

        when(userRepository.findByEmailId("test@example.com")).thenReturn(existingUser);

        // Act
        ResponseEntity<Map<String, Object>> response = userController.login(loginUser, request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("message")).isEqualTo("Invalid credentials");

        verify(userRepository, times(1)).findByEmailId("test@example.com");
    }
}
