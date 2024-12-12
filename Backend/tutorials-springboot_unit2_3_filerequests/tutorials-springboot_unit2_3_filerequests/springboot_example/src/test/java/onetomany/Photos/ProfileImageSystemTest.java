package onetomany.Photos;

import onetomany.Matches.MatchedPairRepository;
import onetomany.PotentialFriends.PotentialFriendRepository;
import onetomany.Users.User;
import onetomany.Users.UserRepository;
import onetomany.images.Image;
import onetomany.images.ImageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProfileImageSystemTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private MatchedPairRepository matchedPairRepository;

    @Autowired
    private PotentialFriendRepository potentialFriendRepository;

    private User testUser;
    private String baseUrl;
    private Path uploadDirectory = Paths.get("./uploads/images/test");

    @BeforeEach
    void setUp() throws IOException {
        baseUrl = "http://localhost:" + port + "/api/images";

        // Create test directory
        Files.createDirectories(uploadDirectory);

        // Create test user
        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("password");
        testUser = userRepository.save(testUser);

        // Create test image file
        Path testImagePath = uploadDirectory.resolve("test-image.jpg");
        Files.write(testImagePath, "test image content".getBytes());
    }

    @AfterEach
    @Transactional
    void cleanup() throws IOException {
        try {
            // Delete in correct order to respect foreign key constraints
            imageRepository.deleteAll();
            potentialFriendRepository.deleteAll();
            matchedPairRepository.deleteAll();
            userRepository.deleteAll();

            // Clean up test files
            if (Files.exists(uploadDirectory)) {
                Files.walk(uploadDirectory)
                        .sorted((a, b) -> b.compareTo(a)) // Delete files before directories
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                System.err.println("Error deleting path: " + path);
                            }
                        });
            }
        } catch (Exception e) {
            System.err.println("Cleanup error: " + e.getMessage());
        }
    }

    @Test
    void testImageUploadAndRetrieval() throws IOException {
        // Create test image file
        Path testImagePath = uploadDirectory.resolve("test-upload.jpg");
        Files.write(testImagePath, "test content".getBytes());

        // Prepare upload request
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(testImagePath.toFile()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);

        // Upload image
        ResponseEntity<Image> uploadResponse = restTemplate.postForEntity(
                baseUrl + "/upload/" + testUser.getId(),
                requestEntity,
                Image.class
        );

        assertTrue(uploadResponse.getStatusCode().is2xxSuccessful());
        assertNotNull(uploadResponse.getBody());
        assertNotNull(uploadResponse.getBody().getImageId());

        // Verify retrieval
        ResponseEntity<Image> retrieveResponse = restTemplate.getForEntity(
                baseUrl + "/" + uploadResponse.getBody().getImageId(),
                Image.class
        );

        assertTrue(retrieveResponse.getStatusCode().is2xxSuccessful());
        assertEquals(uploadResponse.getBody().getImageLink(),
                retrieveResponse.getBody().getImageLink());
    }

    @Test
    void testInvalidImageUpload() {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        // Don't add any file

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/upload/" + testUser.getId(),
                requestEntity,
                String.class
        );

        assertTrue(response.getStatusCode().is4xxClientError());
    }

    @Test
    void testDeleteImageUnauthorized() {
        // Upload an image first
        Path testImagePath = uploadDirectory.resolve("test-delete.jpg");
        try {
            Files.write(testImagePath, "test content".getBytes());
        } catch (IOException e) {
            fail("Failed to create test image file");
        }

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(testImagePath.toFile()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);

        ResponseEntity<Image> uploadResponse = restTemplate.postForEntity(
                baseUrl + "/upload/" + testUser.getId(),
                requestEntity,
                Image.class
        );

        assertTrue(uploadResponse.getStatusCode().is2xxSuccessful());
        assertNotNull(uploadResponse.getBody());

        // Try to delete with wrong user ID
        HttpEntity<String> deleteRequest = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/999/" + uploadResponse.getBody().getImageId(),
                HttpMethod.DELETE,
                deleteRequest,
                String.class
        );

        assertTrue(response.getStatusCode().is4xxClientError() ||
                response.getStatusCode().is5xxServerError());
    }
}