package com.codeup.d2d.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name="doohickey_id")
    private Doohickey doohickey;

    @Column(unique = true)
    private String s3_key;

    @Column
    private boolean isModel;


    public File(){
    }

    public File(Doohickey doohickey, String s3_key, boolean isModel) {
        this.doohickey = doohickey;
        this.s3_key = s3_key;
        this.isModel = isModel;
    }

    public File(File copy) {
        id=copy.id;
        s3_key=copy.s3_key;
        isModel=copy.isModel;
        doohickey=copy.doohickey;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Doohickey getDoohickey() {
        return doohickey;
    }

    public void setDoohickey(Doohickey doohickey) {
        this.doohickey = doohickey;
    }

    public String getS3_key() {
        return s3_key;
    }

    public void setS3_key(String s3_key) {
        this.s3_key = s3_key;
    }

    public boolean isModel() {
        return isModel;
    }

    public void setModel(boolean model) {
        isModel = model;
    }
}
