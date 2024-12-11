package onetomany.Users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Optional;
import onetomany.Laptops.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private LaptopRepository laptopRepository;

//    @Test
//    public void testGetUserById_UserNotFound() throws Exception {
//        // Arrange: Mock the UserRepository to throw an exception when looking for a non-existent user
//        when(userRepository.findById(999L))
//                .thenThrow(new IllegalArgumentException("User not found with ID: 999"));
//
//        // Act & Assert: Perform the GET request and expect a 400 status with the appropriate message
//        mockMvc.perform(get("/api/users/999")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("User not found with ID: 999"));
//    }
//
//    @Test
//    public void testGetUserById_UserFound() throws Exception {
//        // Arrange: Mock the UserRepository to return a user object when a valid ID is provided
//        User mockUser = new User( "testname", "testuser@example.com", new Date());
//        when(userRepository.findById(1L)).thenAnswer(invocation -> Optional.of(mockUser));
//        // Act & Assert: Perform the GET request and expect a 200 status with the user details in JSON format
//        mockMvc.perform(get("/api/users/1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json("{"
//                        + "\"id\": 1,"
//                        + "\"username\": \"testuser\","
//                        + "\"email\": \"testuser@example.com\""
//                        + "}"));
//    }

//    @Test
//    public void testGetAllUsers() throws Exception {
//        // Test case to get all users (GET /users)
//        mockMvc.perform(get("/users"))
//                .andExpect(status().isOk()) // status 200 OK
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray()) // check if response is an array
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists()) // check if an ID exists in the first user
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").exists()); // check if a name exists in the first user
//    }
}

