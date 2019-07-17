package com.codeup.d2d.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Doohickey> doohickeys;

    public Tag(){
    }

    public Tag(String name) {
        this.name = name;
    }

    public Tag(Tag copy) {
        id=copy.id;
        name=copy.name;
        doohickeys=copy.doohickeys;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Doohickey> getDoohickeys() {
        return doohickeys;
    }

    public void setDoohickeys(List<Doohickey> doohickeys) {
        this.doohickeys = doohickeys;
    }
}
