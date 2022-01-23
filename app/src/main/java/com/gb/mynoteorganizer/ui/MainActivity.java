package com.gb.mynoteorganizer.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.gb.mynoteorganizer.R;
import com.gb.mynoteorganizer.data.Constants;
import com.gb.mynoteorganizer.data.Note;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements
        InterfaceMainActivity,
        ConfirmDialog.OnConfirmationDialogClickListener {

    private Note note = null;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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