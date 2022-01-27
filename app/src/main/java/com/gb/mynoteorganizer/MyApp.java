package com.gb.mynoteorganizer;

import android.app.Application;

import com.gb.mynoteorganizer.data.Constants;
import com.gb.mynoteorganizer.data.Note;
import com.gb.mynoteorganizer.data.Repo;
import com.gb.mynoteorganizer.data.RepoImpl;
import com.gb.mynoteorganizer.data.SharedPref;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// Расширяем класс Application чтобы создать список заметок в самом начале.
// Нужен чтобы заметки не дублировались при поворотах экрана и создании/редактировании заметок
public class MyApp extends Application {

    private final Repo repo = RepoImpl.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();

//        fillRepo();

    }

    private void fillRepo() {
        Date date = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = sdf.parse("09-01-2022");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int not_important = 0;

        repo.create(new Note("Title 1", "Description 1", date, not_important));
        repo.create(new Note("Title 2", "Description 2", date, not_important));
        repo.create(new Note("Title 3", "Description 3", date, not_important));
    }
}
