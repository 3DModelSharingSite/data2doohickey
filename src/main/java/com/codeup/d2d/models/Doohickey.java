package com.codeup.d2d.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "doohickeys")
public class Doohickey {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "A doohickey must have a title!")
    @Size(min = 3, message = "A title must be at least 3 characters.")
    private String title;

    @Column(columnDefinition = "text not null")
    @NotBlank(message = "You must have a description!")
    private String description;

    @Column
    private long views;

    @Column
    private long downloads;

    @CreationTimestamp
    private Date created_at;

    @ManyToOne
    @JoinColumn (name = "author_id")
    @JsonManagedReference
    private User author;


    public Doohickey() {
    }

    public Doohickey(Doohickey copy) {
        id = copy.id; // This line is SUPER important! Many things won't work if it's absent
        title = copy.title;
        description = copy.description;
        views = copy.views;
        downloads = copy.downloads;
        created_at=copy.created_at;
        author=copy.author;
    }

    public Doohickey(String title, String description, long views, long downloads, Date created_at, User author) {
        this.title = title;
        this.description = description;
        this.views = views;
        this.downloads = downloads;
        this.created_at = created_at;
        this.author=author;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public long getDownloads() {
        return downloads;
    }

    public void setDownloads(long downloads) {
        this.downloads = downloads;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
