package onetomany.Notes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoteController.class)
public class NoteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteRepository noteRepository;

    @Test
    public void testCreateNote() throws Exception {
        Note note = new Note();
        note.setUserId(1);
        note.setTitle("Test Note");
        note.setContent("This is a test note.");

        // Mock save behavior
        Mockito.when(noteRepository.save(Mockito.any(Note.class))).thenReturn(note);

        // Perform POST request and verify response
        mockMvc.perform(post("/api/notes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(note)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Test Note")))
                .andExpect(jsonPath("$.content", is("This is a test note.")));
    }

    @Test
    public void testGetAllNotesForUser() throws Exception {
        Note note1 = new Note();
        note1.setUserId(1);
        note1.setTitle("Note 1");
        note1.setContent("Content 1");

        Note note2 = new Note();
        note2.setUserId(1);
        note2.setTitle("Note 2");
        note2.setContent("Content 2");

        // Mock repository behavior
        Mockito.when(noteRepository.findByUserIdOrderByLastModifiedDesc(1))
                .thenReturn(List.of(note1, note2));

        // Perform GET request and verify response
        mockMvc.perform(get("/api/notes/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Note 1")))
                .andExpect(jsonPath("$[1].title", is("Note 2")));
    }

    @Test
    public void testGetNote() throws Exception {
        Note note = new Note();
        note.setNoteId(1);
        note.setUserId(1);
        note.setTitle("Sample Note");
        note.setContent("Sample Content");

        // Mock repository behavior
        Mockito.when(noteRepository.findById(1)).thenReturn(java.util.Optional.of(note));

        // Perform GET request and verify response
        mockMvc.perform(get("/api/notes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Sample Note")))
                .andExpect(jsonPath("$.content", is("Sample Content")));

        // Test for non-existent note
        Mockito.when(noteRepository.findById(2)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/notes/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateNote() throws Exception {
        Note existingNote = new Note();
        existingNote.setNoteId(1);
        existingNote.setUserId(1);
        existingNote.setTitle("Old Title");
        existingNote.setContent("Old Content");

        Note updatedNote = new Note();
        updatedNote.setTitle("New Title");
        updatedNote.setContent("New Content");

        // Mock repository behavior
        Mockito.when(noteRepository.findById(1)).thenReturn(java.util.Optional.of(existingNote));
        Mockito.when(noteRepository.save(Mockito.any(Note.class))).thenReturn(updatedNote);

        mockMvc.perform(put("/api/notes/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedNote)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("New Title")))
                .andExpect(jsonPath("$.content", is("New Content")));

        // Test for non-existent note
        Mockito.when(noteRepository.findById(2)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(put("/api/notes/1/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedNote)))
                .andExpect(status().isNotFound());

        // Test for user ID mismatch
        existingNote.setUserId(2); // User mismatch
        Mockito.when(noteRepository.findById(1)).thenReturn(java.util.Optional.of(existingNote));

        mockMvc.perform(put("/api/notes/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedNote)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteNote() throws Exception {
        Note note = new Note();
        note.setNoteId(1);
        note.setUserId(1);

        Mockito.when(noteRepository.findById(1)).thenReturn(Optional.of(note));

        // Test successful deletion
        mockMvc.perform(delete("/api/notes/1/1"))
                .andExpect(status().isNoContent()); // Expect 204 status
    }


    @Test
    public void testSearchNotes() throws Exception {
        Note note1 = new Note();
        note1.setUserId(1);
        note1.setTitle("Test Note 1");
        note1.setContent("Test Content 1");

        Note note2 = new Note();
        note2.setUserId(1);
        note2.setTitle("Test Note 2");
        note2.setContent("Test Content 2");

        // Mock repository behavior
        Mockito.when(noteRepository.findByUserIdAndTitleContainingIgnoreCase(1, "Test"))
                .thenReturn(List.of(note1, note2));

        mockMvc.perform(get("/api/notes/search/1?query=Test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Test Note 1")))
                .andExpect(jsonPath("$[1].title", is("Test Note 2")));

        // Test for no results
        Mockito.when(noteRepository.findByUserIdAndTitleContainingIgnoreCase(1, "NotExist"))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/notes/search/1?query=NotExist")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
