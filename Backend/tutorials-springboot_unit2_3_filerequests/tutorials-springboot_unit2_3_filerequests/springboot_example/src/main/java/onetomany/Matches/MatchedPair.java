package onetomany.matches;

import onetomany.MentorSurvey.Mentor;
import onetomany.MenteeSurvey.Mentee;
import onetomany.Users.User;

import javax.persistence.*;

@Entity
public class MatchedPair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    private User mentor; // Now references User

    @ManyToOne
    @JoinColumn(name = "mentee_id", nullable = false)
    private User mentee; // Now references User

    @Enumerated(EnumType.STRING)
    private Area matchedArea;

    public MatchedPair() {}

    public MatchedPair(User mentor, User mentee, Area matchedArea) {
        this.mentor = mentor;
        this.mentee = mentee;
        this.matchedArea = matchedArea;
    }

    // Getters and setters

    public enum Area {
        CAREER, EDUCATION, GENERAL
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getMentor() {
        return mentor;
    }

    public void setMentor(User mentor) {
        this.mentor = mentor;
    }

    public User getMentee() {
        return mentee;
    }

    public void setMentee(User mentee) {
        this.mentee = mentee;
    }

    public Area getMatchedArea() {
        return matchedArea;
    }

    public void setMatchedArea(Area matchedArea) {
        this.matchedArea = matchedArea;
    }
}
