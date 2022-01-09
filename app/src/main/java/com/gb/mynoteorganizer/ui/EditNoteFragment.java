package com.gb.mynoteorganizer.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gb.mynoteorganizer.R;
import com.gb.mynoteorganizer.data.Note;

public class EditNoteFragment extends Fragment {

    private static final String NOTE = "NOTE";
    private EditText title;
    private EditText description;
    private Button saveNote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_note, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            Note note = (Note) args.getSerializable(NOTE);

            title = view.findViewById(R.id.edit_note_title);
            description = view.findViewById(R.id.edit_note_description);
            saveNote = view.findViewById(R.id.edit_note_update_btn);

            title.setText(note.getTitle());
            description.setText(note.getDescription());
        }







    }
}