package com.example.androidexample;
import java.time.LocalDateTime;

public class MenteeRating {
    private Long id;
    private Long menteeId;
    private Long mentorId;
    private int rating;
    private String ratingDate; // Store as String for simplicity in parsing

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMenteeId() { return menteeId; }
    public void setMenteeId(Long menteeId) { this.menteeId = menteeId; }

    public Long getMentorId() { return mentorId; }
    public void setMentorId(Long mentorId) { this.mentorId = mentorId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getRatingDate() { return ratingDate; }
    public void setRatingDate(String ratingDate) { this.ratingDate = ratingDate; }
}
