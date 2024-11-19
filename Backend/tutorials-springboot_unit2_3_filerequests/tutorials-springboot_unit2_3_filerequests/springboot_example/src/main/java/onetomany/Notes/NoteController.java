package onetomany.Notes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import onetomany.ExceptionHandlers.NoteNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;

    @Operation(summary = "Create a new note for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Note created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Note.class))),
            @ApiResponse(responseCode = "500", description = "Error creating note", content = @Content)
    })
    @PostMapping("/{userId}")
    public Note createNote(
            @Parameter(description = "ID of the user creating the note", required = true) @PathVariable Integer userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the note to create", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Note.class)))
            @RequestBody Note note) {

        note.setUserId(userId);
        note.setCreatedDate(LocalDateTime.now());
        note.setLastModified(LocalDateTime.now());
        return noteRepository.save(note);
    }

    @Operation(summary = "Get all notes for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notes retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Note.class)))
    })
    @GetMapping("/user/{userId}")
    public List<Note> getUserNotes(
            @Parameter(description = "ID of the user to retrieve notes for", required = true) @PathVariable Integer userId) {
        return noteRepository.findByUserIdOrderByLastModifiedDesc(userId);
    }

    @Operation(summary = "Get a specific note by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Note retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Note.class))),
            @ApiResponse(responseCode = "404", description = "Note not found", content = @Content)
    })
    @GetMapping("/{noteId}")
    public Note getNote(
            @Parameter(description = "ID of the note to retrieve", required = true) @PathVariable Integer noteId) {

        return noteRepository.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException("Note not found with ID: " + noteId));
    }

    @Operation(summary = "Update a note for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Note updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Note.class))),
            @ApiResponse(responseCode = "404", description = "Note not found", content = @Content)
    })
    @PutMapping("/{userId}/{noteId}")
    public Note updateNote(
            @Parameter(description = "ID of the user updating the note", required = true) @PathVariable Integer userId,
            @Parameter(description = "ID of the note to update", required = true) @PathVariable Integer noteId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated note details", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Note.class)))
            @RequestBody Note updatedNote) {

        Note existingNote = noteRepository.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException("Note not found with ID: " + noteId));

        if (!existingNote.getUserId().equals(userId)) {
            throw new IllegalArgumentException("User ID mismatch for note update");
        }

        existingNote.setTitle(updatedNote.getTitle());
        existingNote.setContent(updatedNote.getContent());
        existingNote.setLastModified(LocalDateTime.now());
        return noteRepository.save(existingNote);
    }

    @Operation(summary = "Delete a note for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Note deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Note not found", content = @Content)
    })
    @DeleteMapping("/{userId}/{noteId}")
    public void deleteNote(
            @Parameter(description = "ID of the user deleting the note", required = true) @PathVariable Integer userId,
            @Parameter(description = "ID of the note to delete", required = true) @PathVariable Integer noteId) {

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException("Note not found with ID: " + noteId));

        if (!note.getUserId().equals(userId)) {
            throw new IllegalArgumentException("User ID mismatch for note deletion");
        }

        noteRepository.deleteById(noteId);
    }

    @Operation(summary = "Search notes for a user by query")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notes retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Note.class))),
            @ApiResponse(responseCode = "500", description = "Error searching notes", content = @Content)
    })
    @GetMapping("/search/{userId}")
    public List<Note> searchNotes(
            @Parameter(description = "ID of the user to search notes for", required = true) @PathVariable Integer userId,
            @Parameter(description = "Search query for notes", required = true) @RequestParam String query) {

        return noteRepository.findByUserIdAndTitleContainingIgnoreCase(userId, query);
    }
}
