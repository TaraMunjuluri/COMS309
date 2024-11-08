package onetomany.images;

import javax.persistence.*;
import onetomany.Users.User;
import java.time.LocalDateTime;

@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Integer imageId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "image_link", nullable = false)
    private String imageLink;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

    // Constructors
    public Image() {}

    public Image(Integer userId, String imageLink) {
        this.userId = userId;
        this.imageLink = imageLink;
        this.uploadDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }
}