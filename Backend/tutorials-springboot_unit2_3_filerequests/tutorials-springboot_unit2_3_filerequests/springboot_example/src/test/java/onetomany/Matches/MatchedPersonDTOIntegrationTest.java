package onetomany.Matches;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MatchedPersonDTOTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        String username = "testUser";
        String classification = "SENIOR";
        String major = "Computer Science";

        // Act
        MatchedPersonDTO dto = new MatchedPersonDTO(username, classification, major);

        // Assert
        assertEquals(username, dto.getUsername());
        assertEquals(classification, dto.getClassification());
        assertEquals(major, dto.getMajor());
    }

    @Test
    void testSetters() {
        // Arrange
        MatchedPersonDTO dto = new MatchedPersonDTO("", "", "");

        // Act
        dto.setUsername("newUser");
        dto.setClassification("JUNIOR");
        dto.setMajor("Physics");

        // Assert
        assertEquals("newUser", dto.getUsername());
        assertEquals("JUNIOR", dto.getClassification());
        assertEquals("Physics", dto.getMajor());
    }

    @Test
    void testNullValues() {
        // Arrange & Act
        MatchedPersonDTO dto = new MatchedPersonDTO(null, null, null);

        // Assert
        assertNull(dto.getUsername());
        assertNull(dto.getClassification());
        assertNull(dto.getMajor());
    }
}