package com.codeup.d2d.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, unique = true, length = 255)
    @NotBlank(message = "You must have an email!")
    @Email(message="[${validatedValue}] is not a valid email")
    @JsonIgnore
    private String email;

    @Column(nullable = false, unique = true, length = 255)
    @NotBlank(message = "You must have a username!")
    @Size(min = 3, message = "A username must be at least 3 characters.")
    private String username;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "You must have a password!")
    @Size(min = 8, message = "A password must be at least 8 characters.")
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private boolean enabled;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
    @JsonBackReference
    private List<Doohickey> doohickeyList;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_favorites",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "model_id")}
    )
    private List<Doohickey> favorites;

    @Column
    private String photoURL;

    @JsonIgnore
    @Transient
    private String cnfmpassword;

    @OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "user")
    private UserDetails userDetails;

    public User() {
    }

    public User(User copy) {
        id = copy.id; // This line is SUPER important! Many things won't work if it's absent
        email = copy.email;
        username = copy.username;
        password = copy.password;
        cnfmpassword = copy.cnfmpassword;
        enabled=copy.enabled;
        favorites=copy.favorites;
        doohickeyList=copy.doohickeyList;
        photoURL=copy.photoURL;
    }

    public User(String email, String username, String password, boolean enabled, List<Doohickey> favorites, List<Doohickey> doohickeyList) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.favorites = favorites;
        this.doohickeyList = doohickeyList;
    }

    public User(String email, String username, String password, boolean enabled) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCnfmpassword() {
        return cnfmpassword;
    }

    public void setCnfmpassword(String cnfmpassword) {
        this.cnfmpassword = cnfmpassword;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Doohickey> getDoohickeyList() {
        return doohickeyList;
    }

    public void setDoohickeyList(List<Doohickey> doohickeyList) {
        this.doohickeyList = doohickeyList;
    }

    public List<Doohickey> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Doohickey> favorites) {
        this.favorites = favorites;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public String getPhotoURL() {
        if(photoURL == null){
            return "/icons/user-regular.svg";
        }
        return "https://data2doohickey.s3.us-east-2.amazonaws.com/"+photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}