package com.gb.mynoteorganizer.ui;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.gb.mynoteorganizer.R;
import com.gb.mynoteorganizer.data.Constants;
import com.gb.mynoteorganizer.data.Note;

public class NotesListActivity extends BaseActivity implements INoteListActivity {

    private Note note = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        if (savedInstanceState != null) {
            note = (Note) savedInstanceState.getSerializable(Constants.NOTE);
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            replaceNotesListPort(false);
        } else {
            replaceNotesListLand();
            replaceEditNoteLand(note);
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
    public void replaceNotesListPort(boolean isNoteNew) {
        replace(R.id.notes_list_fragment_holder, NotesListFragment.newInstance(isNoteNew));
    }

    @Override
    public void replaceEditNotePort(Note note) {
        replace(R.id.notes_list_fragment_holder, EditNoteFragment.newInstance(note));
    }

    @Override
    public void replaceNotesListLand() {
        replace(R.id.notes_list_fragment_holder, NotesListFragment.newInstance(false));
    }

    @Override
    public void replaceEditNoteLand(Note note) {
        replace(R.id.edit_note_container_land, EditNoteFragment.newInstance(note));
    }

    @Override
    public void removeEditNoteFragment() {
        if (getSupportFragmentManager().findFragmentById(R.id.edit_note_container_land) != null) {
            remove(getSupportFragmentManager().findFragmentById(R.id.edit_note_container_land));
        }
    }

    @Override
    public void saveNote(Note note) {
        this.note = note;
    }

}

// Интерфейс добавления фрагментов
interface INoteListActivity {
    void replaceNotesListPort(boolean isNoteNew);
    void replaceEditNotePort(Note note);
    void replaceNotesListLand();
    void replaceEditNoteLand(Note note);
    void removeEditNoteFragment();
    void saveNote(Note note);
}