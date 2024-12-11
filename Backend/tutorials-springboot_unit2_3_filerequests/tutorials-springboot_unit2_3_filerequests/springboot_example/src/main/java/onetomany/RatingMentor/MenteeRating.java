//package onetomany.RatingMentor;
//
//import onetomany.MenteeSurvey.Mentee;
//import onetomany.MentorSurvey.Mentor;
//import onetomany.Users.User;
//
//import javax.persistence.*;
//
//@Entity
//public class MenteeRating {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "mentee_id", nullable = false)
//    private Mentee mentee;
//
//    @ManyToOne
//    @JoinColumn(name = "mentor_id", nullable = false)
//    private Mentor mentor;
//
//    @Column(nullable = false)
//    private int rating; // Rating from 1 to 5
//
//    // Default constructor
//    public MenteeRating() {
//    }
//
//    // Constructor with fields
//    public MenteeRating(Mentee mentee, Mentor mentor, int rating) {
//        this.mentee = mentee;
//        this.mentor = mentor;
//        this.rating = rating;
//    }
//
//    // Getters and Setters
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Mentee getMentee() {
//        return mentee;
//    }
//
//    public void setMentee(Mentee mentee) {
//        this.mentee = mentee;
//    }
//
//    public Mentor getMentor() {
//        return mentor;
//    }
//
//    public void setMentor(Mentor mentor) {
//        this.mentor = mentor;
//    }
//
//    public int getRating() {
//        return rating;
//    }
//
//    public void setRating(int rating) {
//        if (rating < 1 || rating > 5) {
//            throw new IllegalArgumentException("Rating must be between 1 and 5.");
//        }
//        this.rating = rating;
//    }
//
//
//}


package onetomany.RatingMentor;

import onetomany.MenteeSurvey.Mentee;
import onetomany.MentorSurvey.Mentor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mentee_ratings",
        uniqueConstraints = @UniqueConstraint(columnNames = {"mentee_id", "mentor_id"}))
public class MenteeRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mentee_id", nullable = false)
    private Mentee mentee;

    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    private Mentor mentor;

    @Column(nullable = false)
    private int rating; // Rating from 1 to 5

    @Column(nullable = false)
    private LocalDateTime ratingDate;

    // Default constructor
    public MenteeRating() {
    }

    // Constructor with fields
    public MenteeRating(Mentee mentee, Mentor mentor, int rating) {
        this.mentee = mentee;
        this.mentor = mentor;
        setRating(rating);
        this.ratingDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mentee getMentee() {
        return mentee;
    }

    public void setMentee(Mentee mentee) {
        this.mentee = mentee;
    }

    public Mentor getMentor() {
        return mentor;
    }

    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        this.rating = rating;
    }

    public LocalDateTime getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate(LocalDateTime ratingDate) {
        this.ratingDate = ratingDate;
    }
}