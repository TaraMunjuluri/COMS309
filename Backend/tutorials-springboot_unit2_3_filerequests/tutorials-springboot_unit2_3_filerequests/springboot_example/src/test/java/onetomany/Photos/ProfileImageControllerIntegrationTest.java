package onetomany.Photos;

import onetomany.ProfileImages.ProfileImageController;
import onetomany.Users.UserRepository;
import onetomany.images.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileImageController.class)
class ProfileImageControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private onetomany.images.ImageRepository imageRepository;

    private Image testImage;
    private MockMultipartFile testFile;

    @BeforeEach
    void setUp() {
        testImage = new Image(1, "/uploads/images/test.jpg");
        testImage.setImageId(1);
        testImage.setUploadDate(LocalDateTime.now());

        testFile = new MockMultipartFile(
                "file",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );
    }

    @Nested
    class EntityTests {
        @Test
        void testImageConstructor() {
            Image image = new Image(1, "/test/path.jpg");
            assertNotNull(image);
            assertEquals(1, image.getUserId());
            assertEquals("/test/path.jpg", image.getImageLink());
            assertNotNull(image.getUploadDate());
        }

        @Test
        void testImageGettersAndSetters() {
            Image image = new Image();
            image.setImageId(1);
            image.setUserId(1);
            image.setImageLink("/test/path.jpg");
            LocalDateTime now = LocalDateTime.now();
            image.setUploadDate(now);

            assertEquals(1, image.getImageId());
            assertEquals(1, image.getUserId());
            assertEquals("/test/path.jpg", image.getImageLink());
            assertEquals(now, image.getUploadDate());
        }
    }

    @Nested
    class GetImageTests {
        @Test
        void testGetUserImages_Success() throws Exception {
            when(userRepository.existsById(1L)).thenReturn(true);

            mockMvc.perform(get("/api/images/user/1"))
                    .andExpect(status().isOk());
        }

        @Test
        void testGetUserImages_NotFound() throws Exception {
            when(imageRepository.findByUserId(99)).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/api/images/user/99")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testGetUserImages_UserNotFound() throws Exception {
            when(userRepository.existsById(2L)).thenReturn(false);

            mockMvc.perform(get("/api/images/user/2")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string(""));
        }

        @Test
        void testGetImageById_Success() throws Exception {
            when(imageRepository.findById(1)).thenReturn(Optional.of(testImage));

            mockMvc.perform(get("/api/images/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.imageLink").value("/uploads/images/test.jpg"));
        }

        @Test
        void testGetImageById_NotFound() throws Exception {
            when(imageRepository.findById(99)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/images/99"))
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    class UploadTests {
        @Test
        void testUploadImage_UserNotFound() throws Exception {
            when(userRepository.existsById(1L)).thenReturn(false);

            mockMvc.perform(multipart("/api/images/upload/1").file(testFile))
                    .andExpect(status().isNotFound());
        }




    }

    @Nested
    class DeleteTests {
        @Test
        void testDeleteImage_Success() throws Exception {
            when(imageRepository.findById(1)).thenReturn(Optional.of(testImage));

            mockMvc.perform(delete("/api/images/1/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Image deleted successfully"));
        }

        @Test
        void testDeleteImage_NotFound() throws Exception {
            when(imageRepository.findById(99)).thenReturn(Optional.empty());

            mockMvc.perform(delete("/api/images/1/99"))
                    .andExpect(status().isInternalServerError());
        }


    }

    @Nested
    class PathTests {
        @Test
        void testGetImagePath_Success() throws Exception {
            when(imageRepository.findById(1)).thenReturn(Optional.of(testImage));

            mockMvc.perform(get("/api/images/path/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("/uploads/images/test.jpg"));
        }

        @Test
        void testGetImagePath_NotFound() throws Exception {
            when(imageRepository.findById(99)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/images/path/99"))
                    .andExpect(status().isInternalServerError());
        }
    }
}