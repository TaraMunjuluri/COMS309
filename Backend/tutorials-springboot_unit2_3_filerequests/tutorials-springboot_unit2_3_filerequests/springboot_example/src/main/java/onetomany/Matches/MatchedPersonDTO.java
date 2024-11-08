package onetomany.Matches;

public class MatchedPersonDTO {
    private String username;
    private String classification;
    private String major;

    public MatchedPersonDTO(String username, String classification, String major) {
        this.username = username;
        this.classification = classification;
        this.major = major;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
