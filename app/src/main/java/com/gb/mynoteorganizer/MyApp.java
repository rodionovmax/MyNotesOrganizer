package com.gb.mynoteorganizer;

import android.app.Application;

import com.gb.mynoteorganizer.data.Note;
import com.gb.mynoteorganizer.data.Repo;
import com.gb.mynoteorganizer.data.RepoImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyApp extends Application {

    private Repo repo = RepoImpl.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();

        fillRepo();

    }

    private void fillRepo() {
        repo.create(new Note("Title 1", "Description 1"));
        repo.create(new Note("Title 2", "Description 2"));
        repo.create(new Note("Title 3", "Description 3"));
        repo.create(new Note("Title 4", "Description 4"));
        repo.create(new Note("Title 5", "Description 5"));
        repo.create(new Note("Title 6", "Description 6"));
        repo.create(new Note("Title 7", "Description 7"));
        repo.create(new Note("Title 8", "Description 8"));
        repo.create(new Note("Title 9", "Description 9"));
        repo.create(new Note("Title 10", "Description 10"));
        repo.create(new Note("Title 11", "Description 11"));
        repo.create(new Note("Title 12", "Description 12"));
        repo.create(new Note("Title 13", "Description 13"));
        repo.create(new Note("Title 14", "Description 14"));
        repo.create(new Note("Title 15", "Description 15"));
        repo.create(new Note("Title 16", "Description 16"));
    }
}
