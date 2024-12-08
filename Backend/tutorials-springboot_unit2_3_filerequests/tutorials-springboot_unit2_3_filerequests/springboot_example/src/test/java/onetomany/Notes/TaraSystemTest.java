package onetomany.Notes;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TaraSystemTest {

    // Mock the ServerEndpointExporter to bypass WebSocket initialization
    @MockBean
    private ServerEndpointExporter serverEndpointExporter;

    @Test
    public void testCreateNote() {
        RestTemplate restTemplate = new RestTemplate();

        // Create a new Note object
        Note newNote = new Note();
        newNote.setTitle("Test Note");
        newNote.setContent("This is a test note.");

        // Make a POST request to create a new note
        ResponseEntity<Note> response = restTemplate.postForEntity(
                "http://localhost:8080/api/notes/1",  // Replace with your endpoint
                newNote,
                Note.class
        );

        // Assertions to verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Test Note", response.getBody().getTitle(), "Title should match");
    }

    @Test
    public void testGetAllNotesForUser() {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:8080/api/notes/user/1", // Replace with your endpoint
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
    }

   // @Test
//    public void testUpdateNote() {
//        RestTemplate restTemplate = new RestTemplate();
//
//        Note updatedNote = new Note();
//        updatedNote.setTitle("Updated Test Note");
//        updatedNote.setContent("Updated content for the test note.");
//
//        ResponseEntity<Note> response = restTemplate.exchange(
//                "http://localhost:8080/api/notes/1/13", // Replace userId/noteId with valid IDs
//                HttpMethod.PUT,
//                new HttpEntity<>(updatedNote),
//                Note.class
//        );
//
//        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
//        assertEquals("Updated Test Note", response.getBody().getTitle(), "Updated title should match");
//    }

//    @Test
//    public void testDeleteNote() {
//        RestTemplate restTemplate = new RestTemplate();
//
//        ResponseEntity<Void> response = restTemplate.exchange(
//                "http://localhost:8080/api/notes/1/14", // Replace userId/noteId with valid IDs
//                HttpMethod.DELETE,
//                null,
//                Void.class
//        );
//
//        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
//    }
}
