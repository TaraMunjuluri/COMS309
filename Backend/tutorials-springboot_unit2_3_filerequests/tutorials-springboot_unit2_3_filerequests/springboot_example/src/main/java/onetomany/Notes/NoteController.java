package onetomany.Notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;  // Updated repository name

    @PostMapping("/{userId}")
    public ResponseEntity<?> createNote(@PathVariable Integer userId, @RequestBody Note note) {
        try {
            note.setUserId(userId);
            note.setCreatedDate(LocalDateTime.now());
            note.setLastModified(LocalDateTime.now());
            Note savedNote = noteRepository.save(note);
            return ResponseEntity.ok(savedNote);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating note: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserNotes(@PathVariable Integer userId) {
        try {
            List<Note> notes = noteRepository.findByUserIdOrderByLastModifiedDesc(userId);
            return ResponseEntity.ok(notes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving notes: " + e.getMessage());
        }
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<?> getNote(@PathVariable Integer noteId) {
        try {
            Optional<Note> note = noteRepository.findById(noteId);
            return note.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving note: " + e.getMessage());
        }
    }

    @PutMapping("/{userId}/{noteId}")
    public ResponseEntity<?> updateNote(@PathVariable Integer userId,
                                        @PathVariable Integer noteId,
                                        @RequestBody Note updatedNote) {
        try {
            Optional<Note> existingNote = noteRepository.findById(noteId);
            if (existingNote.isPresent() && existingNote.get().getUserId().equals(userId)) {
                Note note = existingNote.get();
                note.setTitle(updatedNote.getTitle());
                note.setContent(updatedNote.getContent());
                note.setLastModified(LocalDateTime.now());
                return ResponseEntity.ok(noteRepository.save(note));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating note: " + e.getMessage());
        }
    }

    @DeleteMapping("/{userId}/{noteId}")
    public ResponseEntity<?> deleteNote(@PathVariable Integer userId,
                                        @PathVariable Integer noteId) {
        try {
            Optional<Note> note = noteRepository.findById(noteId);
            if (note.isPresent() && note.get().getUserId().equals(userId)) {
                noteRepository.deleteById(noteId);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting note: " + e.getMessage());
        }
    }

    @GetMapping("/search/{userId}")
    public ResponseEntity<?> searchNotes(@PathVariable Integer userId,
                                         @RequestParam String query) {
        try {
            List<Note> notes = noteRepository.findByUserIdAndTitleContainingIgnoreCase(userId, query);
            return ResponseEntity.ok(notes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error searching notes: " + e.getMessage());
        }
    }
}