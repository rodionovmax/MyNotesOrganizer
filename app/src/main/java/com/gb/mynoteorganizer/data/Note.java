package com.gb.mynoteorganizer.data;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable {

    private int id;
    private String title;
    private String description;
    private Date date;
    private String importance;

    public Note(int id, String title, String description, Date date, String importance) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.importance = importance;
    }

    public Note(String title, String description, Date date, String importance) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.importance = importance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Date getDate() {
        return date;
    }

    public Date setDate(Date date) {
        this.date = date;
        return date;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }
}
