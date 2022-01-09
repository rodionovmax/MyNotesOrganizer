package com.gb.mynoteorganizer.data;

import java.util.List;

public interface Repo {

    int create(Note note);
    Note read(int id);
    void update(Note note);
    void delete(int id);

    List<Note> getAll();
}
