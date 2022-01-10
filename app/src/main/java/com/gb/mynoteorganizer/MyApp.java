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
        Date date = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = sdf.parse("09/01/2022");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        repo.create(new Note("Title 1", "Description 1", date));
        repo.create(new Note("Title 2", "Description 2", date));
        repo.create(new Note("Title 3", "Description 3", date));
        repo.create(new Note("Title 4", "Description 4", date));
        repo.create(new Note("Title 5", "Description 5", date));
        repo.create(new Note("Title 6", "Description 6", date));
        repo.create(new Note("Title 7", "Description 7", date));
        repo.create(new Note("Title 8", "Description 8", date));
        repo.create(new Note("Title 9", "Description 9", date));
        repo.create(new Note("Title 10", "Description 10", date));
        repo.create(new Note("Title 11", "Description 11", date));
        repo.create(new Note("Title 12", "Description 12", date));
        repo.create(new Note("Title 13", "Description 13", date));
        repo.create(new Note("Title 14", "Description 14", date));
        repo.create(new Note("Title 15", "Description 15", date));
        repo.create(new Note("Title 16", "Description 16", date));
    }
}
