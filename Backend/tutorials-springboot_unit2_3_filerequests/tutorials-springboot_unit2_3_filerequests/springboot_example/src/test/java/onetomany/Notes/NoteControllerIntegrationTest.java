package onetomany.Notes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}
