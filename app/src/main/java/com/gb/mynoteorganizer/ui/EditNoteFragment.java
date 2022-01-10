package com.gb.mynoteorganizer.ui;

import android.app.DatePickerDialog;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.gb.mynoteorganizer.R;
import com.gb.mynoteorganizer.data.Constants;
import com.gb.mynoteorganizer.data.Note;
import com.gb.mynoteorganizer.data.Repo;
import com.gb.mynoteorganizer.data.RepoImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditNoteFragment extends Fragment implements View.OnClickListener {

    private static final String NOTE = "NOTE";
    private EditText title;
    private EditText description;
    private EditText evDate;
    private Button saveNote;
    private Note note;
    private Date date;
    private DatePickerDialog datePicker;

    private int id = -1;

    private Repo repo = RepoImpl.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_note, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = view.findViewById(R.id.edit_note_title);
        description = view.findViewById(R.id.edit_note_description);
        evDate = view.findViewById(R.id.date);
        saveNote = view.findViewById(R.id.edit_note_update_btn);

        Bundle args = getArguments();
        if (args != null) {
            note = (Note) args.getSerializable(NOTE);
            id = note.getId();
            title.setText(note.getTitle());
            description.setText(note.getDescription());
            evDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(note.getDate()));
        }

        // Сделать кнопку неактивной если title пустой
        setButtonActiveIfTitleNotEmpty();
        title.addTextChangedListener(titleTextWatcher);

        saveNote.setOnClickListener(this);

        evDate.setOnClickListener(view1 -> showDatePicker());
    }

    private void showDatePicker() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        datePicker = new DatePickerDialog(requireActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        evDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            date = sdf.parse(evDate.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);
        datePicker.show();
    }

    @Override
    public void onClick(View view) {

        Note updatedNote = new Note(id, title.getText().toString(), description.getText().toString(), date);

        if (id == -1) {
            repo.create(updatedNote);
        } else {
            repo.update(updatedNote);
        }

        NotesListFragment notesListFragment = new NotesListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.NOTE_NEW, true);
        notesListFragment.setArguments(bundle);

        // Если ориентация портретная - перейти на фрагмент лист
        // Если ориентация ландшафтная - перейти на фрагмент лист и удалить текущий фрагмент
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.notes_list_fragment_holder, notesListFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.notes_list_fragment_holder, notesListFragment)
                    .remove(this)
                    .commit();
        }

    }


    // Слушатель изменения текста в title
    private TextWatcher titleTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            setButtonActiveIfTitleNotEmpty();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void setButtonActiveIfTitleNotEmpty() {
        String titleInput = title.getText().toString().trim();
        saveNote.setEnabled(!titleInput.isEmpty());
    }


}