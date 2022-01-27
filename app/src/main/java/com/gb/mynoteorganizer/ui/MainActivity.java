package com.gb.mynoteorganizer.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.gb.mynoteorganizer.R;
import com.gb.mynoteorganizer.data.Constants;
import com.gb.mynoteorganizer.data.Note;
import com.gb.mynoteorganizer.data.Repo;
import com.gb.mynoteorganizer.data.RepoImpl;
import com.gb.mynoteorganizer.data.SharedPref;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements
        InterfaceMainActivity,
        ConfirmDialog.OnConfirmationDialogClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    private Note note = null;
    private FragmentManager fragmentManager;
    private DrawerLayout drawer;
    private Gson gson = new Gson();
    private final Repo repo = RepoImpl.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Получить заметки из shared preferences file
        Context mContext = getApplicationContext();
        SharedPref.init(mContext);

        String sharedPrefsNotes = SharedPref.read(Constants.NOTES_KEY, "");
        ArrayList<Note> savedNotes = gson.fromJson(sharedPrefsNotes, new TypeToken<List<Note>>(){}.getType());
        if (savedNotes == null) {
            savedNotes = new ArrayList<>();
        }

        // При первом запуске добавить заметки из файла в репо
        if (savedInstanceState == null) {
            if (!savedNotes.isEmpty()) {
                for (Note note : savedNotes) {
                    repo.create(note);
                }
            }
        }

        // Инициализируем тулбар и drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initDrawer(toolbar);

        fragmentManager = getSupportFragmentManager();

        // Добавление List fragment при первом запуске
        if (savedInstanceState == null) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.notes_list_fragment_holder, NotesListFragment.newInstance(false))
                    .commit();
        }

        if (savedInstanceState != null) {
            note = (Note) savedInstanceState.getSerializable(Constants.NOTE);
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            replaceListPort(false);
        } else {
            replaceListLand();
            replaceEditLand(note);
        }

    }

    private void initDrawer(Toolbar toolbar) {
        // Находим DrawerLayout
        drawer = findViewById(R.id.drawer_layout);

        // Создаем ActionBarDrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Обработка навигационного меню
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    // Сохраняем заметку перед поворотом экрана
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Constants.NOTE, note);
    }

    // Восстанавливаем заметку после поворота экрана
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        note = (Note) savedInstanceState.getSerializable(Constants.NOTE);
    }

    // Реализуем методы для добавления фрагментов
    @Override
    public void replaceListPort(boolean isNoteNew) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.notes_list_fragment_holder, NotesListFragment.newInstance(isNoteNew))
                .commit();
    }

    @Override
    public void replaceEditPort(Note note) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.notes_list_fragment_holder, EditNoteFragment.newInstance(note))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void replaceListLand() {
        fragmentManager
                .beginTransaction()
                .replace(R.id.notes_list_fragment_holder, NotesListFragment.newInstance(false))
                .commit();
    }

    @Override
    public void replaceEditLand(Note note) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.edit_note_fragment_holder, EditNoteFragment.newInstance(note))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void removeEditFragment() {
        if (fragmentManager.findFragmentById(R.id.edit_note_fragment_holder) != null) {
            fragmentManager
                    .beginTransaction()
                    .remove(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.edit_note_fragment_holder)))
                    .commit();
        }
    }

    @Override
    public void saveNote(Note note) {
        this.note = note;
    }

    // Переопределяем onBackPressed чтобы спросить подтверждение перед тем как закрыть приложение
    // Если в onBackStack больше нет фрагментов то показываем диалог
    // Реализован через диалог фрагмент чтобы алерт не пропадал при повороте экрана
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            showConfirmDialogFragment();
        } else {
            MainActivity.super.onBackPressed();
        }
    }

    // Показать диалог
    private void showConfirmDialogFragment() {
        new ConfirmDialog().show(getSupportFragmentManager(), null);
    }

    // Реализуем интерфейс OnConfirmationDialogClickListener
    @Override
    public void onConfirm() {
        Toast.makeText(MainActivity.this, "Bye-bye", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    // Реализуем клики на боковое меню
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_drawer_about:
                openAboutFragment();
                break;
            case R.id.action_drawer_exit:
                finish();
                break;
            case R.id.nav_share:
                Toast.makeText(MainActivity.this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_send:
                Toast.makeText(MainActivity.this, "Send", Toast.LENGTH_SHORT).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openAboutFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("")
                .add(R.id.notes_list_fragment_holder, new AboutFragment()).commit();
    }

    public ArrayList<Note> getNotes() {
        String sharedPrefsNotes = SharedPref.read(Constants.NOTES_KEY, "");
        ArrayList<Note> notes = gson.fromJson(sharedPrefsNotes, new TypeToken<List<Note>>(){}.getType());
        if (notes == null) {
            notes = new ArrayList<>();
        }
        return notes;
    }
}

// Интерфейс добавления фрагментов
interface InterfaceMainActivity {
    void replaceListPort(boolean isNoteNew);
    void replaceEditPort(Note note);
    void replaceListLand();
    void replaceEditLand(Note note);
    void removeEditFragment();
    void saveNote(Note note);
}