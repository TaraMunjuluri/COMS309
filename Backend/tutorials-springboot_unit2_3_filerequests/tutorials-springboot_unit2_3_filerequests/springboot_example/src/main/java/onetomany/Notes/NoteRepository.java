package onetomany.Notes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Integer> {
    List<Note> findByUserId(Integer userId);
    List<Note> findByUserIdOrderByLastModifiedDesc(Integer userId);
    void deleteByUserIdAndNoteId(Integer userId, Integer noteId);
    List<Note> findByUserIdAndTitleContainingIgnoreCase(Integer userId, String searchTerm);
}