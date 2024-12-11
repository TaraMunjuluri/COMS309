package onetomany.Users;



import com.fasterxml.jackson.annotation.JsonIgnore;
import onetomany.Achievements.Achievement;
import onetomany.Interests.Interests;
import onetomany.Laptops.Laptop;
import onetomany.Phones.Phone;

import javax.persistence.*;
import java.sql.Blob;
import java.util.*;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Table(name = "users")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private String username;
    private String password;
    private String name;
    private String emailId;
    private Date joiningDate;
    private boolean ifActive;
    private String extension;


    private String appMode;


    private String major;

    //demo 4
    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language = Language.ENGLISH; // Default language
    //demo 4

    private String classification;
    @JsonIgnore
    @Lob
    private Blob avatar;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "laptop_id")
    private Laptop laptop;

    @Column(name = "first_login_completed", columnDefinition = "boolean default false")
    private boolean firstLoginCompleted = false;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "user_achievements",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "achievement_id"))
    private Set<Achievement> achievements = new HashSet<>();


    public Set<Achievement> getAchievements() {
        return achievements;
    }


    public void setAchievements(Set<Achievement> achievements) {
        this.achievements = achievements;
    }


    public boolean hasLoggedInBefore() {
        return firstLoginCompleted;
    }

    // Getter and setter for firstLoginCompleted
    public boolean isFirstLoginCompleted() {
        return firstLoginCompleted;
    }

    public void setFirstLoginCompleted(boolean firstLoginCompleted) {
        this.firstLoginCompleted = firstLoginCompleted;
    }
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_phones",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "phone_id"))
    private List<Phone> phones = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_interests",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id", referencedColumnName = "id")
    )
    private Set<Interests> interests = new HashSet<>();


    // Add getters and setters for interests
    public Set<Interests> getInterests() {
        return interests;
    }
    public void setInterests(Set<Interests> interests) {
        this.interests = interests;
    }


//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Message> messages;
//
//
//    // Getters and Setters for `messages`
//    public List<Message> getMessages() {
//        return messages;
//    }


//    public void setMessages(List<Message> messages) {
//        this.messages = messages;
//    }
//



    // Constructors
    public User(String name, String emailId, Date joiningDate) {
        this.name = name;
        this.emailId = emailId;
        this.joiningDate = joiningDate;
        this.ifActive = true;
    }


    public User() {
    }


    // Getters and Setters

    //demo 4
    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    // Optional: Add a method to set language by code
    public void setLanguageByCode(String languageCode) {
        this.language = Language.fromCode(languageCode);
    }

    //demo 4
    private String theme = "light"; // Default to light mode

    // Getters and setters
    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getEmailId() {
        return emailId;
    }


    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }


    public Date getJoiningDate() {
        return joiningDate;
    }


    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = joiningDate;
    }


    public boolean getIsActive() {
        return ifActive;
    }


    public void setIfActive(boolean ifActive) {
        this.ifActive = ifActive;
    }


    public Laptop getLaptop() {
        return laptop;
    }


    public void setLaptop(Laptop laptop) {
        this.laptop = laptop;
    }


    public List<Phone> getPhones() {
        return phones;
    }


    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }


    public Blob getAvatar() {
        return avatar;
    }


    public void setAvatar(Blob avatar) {
        this.avatar = avatar;
    }


    public String getExtension() {
        return extension;
    }


    public void setExtension(String extension) {
        this.extension = extension;
    }


    public void addPhone(Phone phone) {
        this.phones.add(phone);
    }


    public String getAppMode() {
        return appMode; // Getter for appMode
    }


    public void setAppMode(String appMode) {
        this.appMode = appMode; // Setter for appMode
    }


    public String getMajor() {
        return major;
    }


    public void setMajor(String major) {
        this.major = major;
    }


    public String getClassification() {
        return classification;
    }


    public void setClassification(String classification) {
        this.classification = classification;
    }
}
