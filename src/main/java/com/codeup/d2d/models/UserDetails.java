package com.codeup.d2d.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class UserDetails {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    private String location;

    @Column(nullable = false, columnDefinition = "int default 3")
    private int classification;

    @Column(nullable = false, columnDefinition = "text")
    private String bio;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public UserDetails() {
    }

    public UserDetails(String location, int classification, String bio) {
        this.location = location;
        this.classification = classification;
        this.bio = bio;
    }

    public UserDetails(UserDetails copy) {
        id = copy.id; // This line is SUPER important! Many things won't work if it's absent
        location = copy.location;
        classification = copy.classification;
        bio = copy.bio;
        user = copy.user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getClassification() {
        return classification;
    }

    public String getClassificationName(){
        String[] Classifications = new String[7];
        Classifications[0] = "Artist";
        Classifications[1] = "Designer";
        Classifications[2] = "Engineer";
        Classifications[3] = "Maker";
        Classifications[4] = "Professional";
        Classifications[5] = "Student";
        Classifications[6] = "Teacher";

        return Classifications[this.classification];

    }

    public void setClassification(int classification) {
        this.classification = classification;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}