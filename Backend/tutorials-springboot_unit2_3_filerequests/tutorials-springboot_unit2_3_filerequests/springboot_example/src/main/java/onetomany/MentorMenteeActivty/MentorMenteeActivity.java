package onetomany.Matches;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mentor_mentee_activity")
public class MentorMenteeActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private MatchedPairMentorMentee matchedPair;

    @Column(name = "last_interaction")
    private LocalDateTime lastInteraction;

    @Column(name = "last_login_mentor")
    private LocalDateTime lastLoginMentor;

    @Column(name = "last_login_mentee")
    private LocalDateTime lastLoginMentee;

    @Column(name = "meeting_notes", length = 1000)
    private String meetingNotes;

    @Column(name = "meeting_date")
    private LocalDateTime meetingDate;

    @Column(name = "next_meeting_date")
    private LocalDateTime nextMeetingDate;

    @Column(name = "interaction_type")
    @Enumerated(EnumType.STRING)
    private InteractionType interactionType;

    public enum InteractionType {
        MEETING, EMAIL, CHAT, OTHER
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MatchedPairMentorMentee getMatchedPair() { return matchedPair; }
    public void setMatchedPair(MatchedPairMentorMentee matchedPair) { this.matchedPair = matchedPair; }

    public LocalDateTime getLastInteraction() { return lastInteraction; }
    public void setLastInteraction(LocalDateTime lastInteraction) { this.lastInteraction = lastInteraction; }

    public LocalDateTime getLastLoginMentor() { return lastLoginMentor; }
    public void setLastLoginMentor(LocalDateTime lastLoginMentor) { this.lastLoginMentor = lastLoginMentor; }

    public LocalDateTime getLastLoginMentee() { return lastLoginMentee; }
    public void setLastLoginMentee(LocalDateTime lastLoginMentee) { this.lastLoginMentee = lastLoginMentee; }

    public String getMeetingNotes() { return meetingNotes; }
    public void setMeetingNotes(String meetingNotes) { this.meetingNotes = meetingNotes; }

    public LocalDateTime getMeetingDate() { return meetingDate; }
    public void setMeetingDate(LocalDateTime meetingDate) { this.meetingDate = meetingDate; }

    public LocalDateTime getNextMeetingDate() { return nextMeetingDate; }
    public void setNextMeetingDate(LocalDateTime nextMeetingDate) { this.nextMeetingDate = nextMeetingDate; }

    public InteractionType getInteractionType() { return interactionType; }
    public void setInteractionType(InteractionType interactionType) { this.interactionType = interactionType; }
}