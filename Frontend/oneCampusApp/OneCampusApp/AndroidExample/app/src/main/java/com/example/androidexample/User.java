package com.example.androidexample;

public class User {

    private String name;
    private String major;
    private String classification;
    private int userId;
    private int matchId;
    private String profileImageUrl;

    // Constructor
    public User(String name, String major, String classification, int userId) {
        this.name = name;
        this.major = major;
        this.classification = classification;
        this.userId = userId;
        this.matchId = matchId;
        this.profileImageUrl = profileImageUrl;

    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getMatchId() {
        return matchId;
    }
    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }
    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
