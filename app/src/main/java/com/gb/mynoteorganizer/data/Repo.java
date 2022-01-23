package com.gb.mynoteorganizer.data;

import java.util.Date;
import java.util.List;

public interface Repo {

    int create(Note note);
    int create(String title, String description, Date date, int importance);
    Note read(int id);
    void update(Note note);
    void delete(int id);
    void delete(Note note);

    List<Note> getAll();
}
