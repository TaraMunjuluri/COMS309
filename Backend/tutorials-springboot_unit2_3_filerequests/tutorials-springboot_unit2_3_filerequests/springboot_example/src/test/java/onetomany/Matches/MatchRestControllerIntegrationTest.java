package onetomany.Matches;

import onetomany.Users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

class MatchedPairMentorMenteeTest {

    private MatchedPairMentorMentee matchedPair;
    private User mentor;
    private User mentee;

    @BeforeEach
    void setUp() {
        mentor = new User();
        mentor.setId(1L);
        mentor.setUsername("testMentor");

        mentee = new User();
        mentee.setId(2L);
        mentee.setUsername("testMentee");

        matchedPair = new MatchedPairMentorMentee(mentor, mentee, MatchedPairMentorMentee.Area.CAREER);
    }

    @Nested
    class ConstructorTests {
        @Test
        void testConstructor() {
            assertEquals(mentor, matchedPair.getMentor());
            assertEquals(mentee, matchedPair.getMentee());
            assertEquals(MatchedPairMentorMentee.Area.CAREER, matchedPair.getMatchedArea());
        }

        @Test
        void testDefaultConstructor() {
            MatchedPairMentorMentee emptyPair = new MatchedPairMentorMentee();
            assertNull(emptyPair.getMentor());
            assertNull(emptyPair.getMentee());
            assertNull(emptyPair.getMatchedArea());
        }

        @Test
        void testConstructorWithNullArea() {
            MatchedPairMentorMentee nullAreaPair = new MatchedPairMentorMentee(mentor, mentee, null);
            assertEquals(mentor, nullAreaPair.getMentor());
            assertEquals(mentee, nullAreaPair.getMentee());
            assertNull(nullAreaPair.getMatchedArea());
        }
    }

    @Nested
    class GetterSetterTests {
        @Test
        void testSetAndGetId() {
            matchedPair.setId(1L);
            assertEquals(1L, matchedPair.getId());
        }

        @Test
        void testSetAndGetMentor() {
            User newMentor = new User();
            newMentor.setId(3L);
            matchedPair.setMentor(newMentor);
            assertEquals(newMentor, matchedPair.getMentor());
        }

        @Test
        void testSetAndGetMentee() {
            User newMentee = new User();
            newMentee.setId(4L);
            matchedPair.setMentee(newMentee);
            assertEquals(newMentee, matchedPair.getMentee());
        }

        @Test
        void testSetAndGetMatchedArea() {
            matchedPair.setMatchedArea(MatchedPairMentorMentee.Area.EDUCATION);
            assertEquals(MatchedPairMentorMentee.Area.EDUCATION, matchedPair.getMatchedArea());
        }

        @Test
        void testSetNullMentor() {
            matchedPair.setMentor(null);
            assertNull(matchedPair.getMentor());
        }

        @Test
        void testSetNullMentee() {
            matchedPair.setMentee(null);
            assertNull(matchedPair.getMentee());
        }

        @Test
        void testSetNullMatchedArea() {
            matchedPair.setMatchedArea(null);
            assertNull(matchedPair.getMatchedArea());
        }
    }

    @Nested
    class AreaEnumTests {
        @Test
        void testAllAreaEnumValues() {
            MatchedPairMentorMentee.Area[] areas = MatchedPairMentorMentee.Area.values();
            assertEquals(3, areas.length);
            assertTrue(containsArea(areas, MatchedPairMentorMentee.Area.CAREER));
            assertTrue(containsArea(areas, MatchedPairMentorMentee.Area.EDUCATION));
            assertTrue(containsArea(areas, MatchedPairMentorMentee.Area.GENERAL));
        }

        @Test
        void testAreaEnumValuesString() {
            assertEquals("CAREER", MatchedPairMentorMentee.Area.CAREER.toString());
            assertEquals("EDUCATION", MatchedPairMentorMentee.Area.EDUCATION.toString());
            assertEquals("GENERAL", MatchedPairMentorMentee.Area.GENERAL.toString());
        }

        private boolean containsArea(MatchedPairMentorMentee.Area[] areas, MatchedPairMentorMentee.Area targetArea) {
            for (MatchedPairMentorMentee.Area area : areas) {
                if (area == targetArea) return true;
            }
            return false;
        }
    }

    @Nested
    class ValidationTests {
        @Test
        void testMatchedPairWithSameMentorAndMentee() {
            // While this might be allowed by the code, it's good to test this case
            MatchedPairMentorMentee samePair = new MatchedPairMentorMentee(mentor, mentor, MatchedPairMentorMentee.Area.CAREER);
            assertEquals(samePair.getMentor(), samePair.getMentee());
        }

        @Test
        void testSetMatchedAreaMultipleTimes() {
            matchedPair.setMatchedArea(MatchedPairMentorMentee.Area.CAREER);
            matchedPair.setMatchedArea(MatchedPairMentorMentee.Area.EDUCATION);
            matchedPair.setMatchedArea(MatchedPairMentorMentee.Area.GENERAL);
            assertEquals(MatchedPairMentorMentee.Area.GENERAL, matchedPair.getMatchedArea());
        }
    }
}
