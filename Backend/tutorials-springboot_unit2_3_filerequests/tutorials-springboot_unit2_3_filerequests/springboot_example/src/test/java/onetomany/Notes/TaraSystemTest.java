package onetomany.Notes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaraSystemTest {

    @MockBean
    private ServerEndpointExporter serverEndpointExporter;

    @Autowired
    private NoteRepository noteRepository;

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

        // Cleanup: Remove the created note
        noteRepository.deleteById(response.getBody().getNoteId());
    }

    @Test
    public void testGetAllNotesForUser() {
        RestTemplate restTemplate = new RestTemplate();

        // Seed test data
        Note note1 = new Note();
        note1.setUserId(1);
        note1.setTitle("Note 1");
        note1.setContent("Content for Note 1");
        noteRepository.save(note1);

        Note note2 = new Note();
        note2.setUserId(1);
        note2.setTitle("Note 2");
        note2.setContent("Content for Note 2");
        noteRepository.save(note2);

        // Make a GET request to retrieve all notes for user 1
        ResponseEntity<List> response = restTemplate.getForEntity(
                "http://localhost:8080/api/notes/user/1", // Replace with your endpoint
                List.class
        );

        // Assertions to verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().size() >= 2, "User should have at least 2 notes");

        // Cleanup: Remove the created notes
        noteRepository.deleteAll();
    }

    @Test
    public void testUpdateNote() {
        RestTemplate restTemplate = new RestTemplate();

        // Seed test data
        Note note = new Note();
        note.setUserId(1);
        note.setTitle("Original Note");
        note.setContent("Original content.");
        Note savedNote = noteRepository.save(note);

        // Create updated note
        Note updatedNote = new Note();
        updatedNote.setTitle("Updated Note");
        updatedNote.setContent("Updated content.");

        // Make a PUT request to update the note
        ResponseEntity<Note> response = restTemplate.exchange(
                "http://localhost:8080/api/notes/1/" + savedNote.getNoteId(), // Replace userId/noteId with valid IDs
                HttpMethod.PUT,
                new HttpEntity<>(updatedNote),
                Note.class
        );

        // Assertions to verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Updated Note", response.getBody().getTitle(), "Updated title should match");

        // Cleanup: Remove the updated note
        noteRepository.deleteById(savedNote.getNoteId());
    }

    @Test
    public void testDeleteNote() {
        RestTemplate restTemplate = new RestTemplate();

        // Seed test data
        Note note = new Note();
        note.setUserId(1);
        note.setTitle("Delete Me");
        note.setContent("This note will be deleted.");
        Note savedNote = noteRepository.save(note);

        // Make a DELETE request to delete the note
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:8080/api/notes/1/" + savedNote.getNoteId(), // Replace userId/noteId with valid IDs
                HttpMethod.DELETE,
                null,
                Void.class
        );

        // Assertions to verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");

        // Verify the note is deleted
        assertFalse(noteRepository.findById(savedNote.getNoteId()).isPresent(), "Note should be deleted");
    }
}
