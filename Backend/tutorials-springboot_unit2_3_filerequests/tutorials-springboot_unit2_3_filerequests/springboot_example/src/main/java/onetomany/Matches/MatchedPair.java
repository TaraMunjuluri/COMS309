package onetomany.matches;

import onetomany.MentorSurvey.Mentor;
import onetomany.MenteeSurvey.Mentee;

import javax.persistence.*;

@Entity
public class MatchedPair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    private Mentor mentor;

    @ManyToOne
    @JoinColumn(name = "mentee_id", nullable = false)
    private Mentee mentee;

    @Enumerated(EnumType.STRING)
    private Area matchedArea;

    public MatchedPair() {}

    public MatchedPair(Mentor mentor, Mentee mentee, Area matchedArea) {
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

    public Mentor getMentor() {
        return mentor;
    }

    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }

    public Mentee getMentee() {
        return mentee;
    }

    public void setMentee(Mentee mentee) {
        this.mentee = mentee;
    }

    public Area getMatchedArea() {
        return matchedArea;
    }

    public void setMatchedArea(Area matchedArea) {
        this.matchedArea = matchedArea;
    }
}
