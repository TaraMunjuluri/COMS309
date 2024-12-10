package onetomany.Photos;

import onetomany.ProfileImages.ProfileImageController;
import onetomany.Users.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileImageController.class)
class ProfileImageControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private onetomany.images.ImageRepository imageRepository;

    @Test
    void testGetUserImages_Success() throws Exception {
        // Mock behavior for the repository
        when(userRepository.existsById(1L)).thenReturn(true);

        // Perform the test
        mockMvc.perform(get("/api/images/user/1"))
                .andExpect(status().isOk());
    }
    @Test
    void testGetUserImages_NotFound() throws Exception {
        // Mock repository to return an empty list for user ID 99
        when(imageRepository.findByUserId(Mockito.eq(99))).thenReturn(Collections.emptyList());

        // Perform the test and expect 404
        mockMvc.perform(get("/api/images/user/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void testGetUserImages_UserNotFound() throws Exception {
        // Mock behavior for the user repository
        when(userRepository.existsById(2L)).thenReturn(false);

        // Perform the test
        mockMvc.perform(get("/api/images/user/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

//    @Test
//    void testUploadImage_Success() throws Exception {
//        // Mock userRepository to indicate the user exists
//        when(userRepository.existsById(1L)).thenReturn(true);
//
//        // Mock MultipartFile
//        MockMultipartFile file = new MockMultipartFile(
//                "file",
//                "test.jpg",
//                MediaType.IMAGE_JPEG_VALUE,
//                "test data".getBytes()
//        );
//
//        // Perform test
//        mockMvc.perform(multipart("/api/images/upload/1").file(file))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.imageLink").exists());
//    }

    @Test
    void testUploadImage_UserNotFound() throws Exception {
        when(userRepository.existsById(1L)).thenReturn(false);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test data".getBytes()
        );

        mockMvc.perform(multipart("/api/images/upload/1").file(file))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetImageById_Success() throws Exception {
        // Mock repository to return an image
        onetomany.images.Image mockImage = new onetomany.images.Image(1, "/uploads/images/test.jpg");
        when(imageRepository.findById(1)).thenReturn(Optional.of(mockImage));

        // Perform test
        mockMvc.perform(get("/api/images/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imageLink").value("/uploads/images/test.jpg"));
    }





}
