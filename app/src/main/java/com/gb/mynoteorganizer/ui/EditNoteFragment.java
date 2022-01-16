package com.gb.mynoteorganizer.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.gb.mynoteorganizer.R;
import com.gb.mynoteorganizer.data.Constants;
import com.gb.mynoteorganizer.data.Note;
import com.gb.mynoteorganizer.data.Repo;
import com.gb.mynoteorganizer.data.RepoImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditNoteFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String NOTE = "NOTE";
    private static final String TAG = "myLogger";
    private EditText evTitle;
    private EditText evDescription;
    private Button datePickerBtn;
    private TextView tvDate;
    private Button saveNote;
    private Note note;
    private Date date;
    private int importance;
    private DatePickerDialog datePicker;
    private Spinner spinner;

    private int id = -1;

    private Repo repo = RepoImpl.getInstance();

    private INoteListActivity listener;

    public static Fragment newInstance(Note note) {
        Fragment fragment = new EditNoteFragment();
        if (note != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(NOTE, note);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_note, container, false);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "Edit onAttach() called with: context = [" + context + "]");
        if (context instanceof INoteListActivity) {
            listener = (INoteListActivity) context;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "Edit onViewCreated() called with: view = [Edit], savedInstanceState = [" + savedInstanceState + "]");

        evTitle = view.findViewById(R.id.edit_note_title);
        evDescription = view.findViewById(R.id.edit_note_description);
        datePickerBtn = view.findViewById(R.id.date_picker_btn);
        tvDate = view.findViewById(R.id.date);
        spinner = view.findViewById(R.id.importance_spinner);
        saveNote = view.findViewById(R.id.edit_note_update_btn);

        // Adapter for spinner
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(), R.array.importance, R.layout.style_spinner_item);
        arrayAdapter.setDropDownViewResource(R.layout.style_spinner_item);
        spinner.setAdapter(arrayAdapter);

        Bundle args = getArguments();
        if (args != null) {
            note = (Note) args.getSerializable(NOTE);
            if (note != null) {
                id = note.getId();
                evTitle.setText(note.getTitle());
                evDescription.setText(note.getDescription());
                if (note.getDate() != null) {
                    tvDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(note.getDate()));
                }
                spinner.setSelection(note.getImportance());
            }
        }

        // Сделать кнопку неактивной если title пустой
        setButtonActiveIfTitleNotEmpty();
        evTitle.addTextChangedListener(titleTextWatcher);

        // Слушатель на кнопку
        saveNote.setOnClickListener(this);

        // Слушатель на Date Picker
        datePickerBtn.setOnClickListener(view1 -> showDatePicker());

        // Слушатель на Spinner
        spinner.setOnItemSelectedListener(this);

        listener.saveNote(note);
    }

    // Сохраняем title и description чтобы их можно было восстановить при повороте экрана
//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        Log.d(TAG, "Edit onSaveInstanceState() called with: outState = [" + outState + "]");
//        outState.putSerializable(Constants.NOTE, note);
//        super.onSaveInstanceState(outState);
//    }
//
//    // Восстанавливаем title и description
//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        if (savedInstanceState != null) {
//            if (note != null) {
//                Note saved_note = (Note) savedInstanceState.getSerializable(Constants.NOTE);
//
//                evTitle.setText(note.getTitle());
//                evDescription.setText(note.getDescription());
//
//                Log.d(TAG, "Edit onViewStateRestored: " + saved_note.getTitle());
//                Log.d(TAG, "Edit onViewStateRestored: " + saved_note.getDescription());
//            }
//        }
//    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "Edit onConfigurationChanged() called with: newConfig = [" + newConfig + "]");
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
                        tvDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        setDateFromTextView();
                    }
                }, year, month, day);
        datePicker.show();
    }

    @Override
    public void onClick(View view) {

        if (date == null) {
            setDateFromTextView();
        }

        Note updatedNote = new Note(id, evTitle.getText().toString(), evDescription.getText().toString(), date, importance);

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
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            listener.replaceNotesListPort(true);
        } else {
            listener.removeEditNoteFragment();
            listener.replaceNotesListLand();
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

    // Установить note date из значения полученного из textview
    private void setDateFromTextView() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = sdf.parse(tvDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setButtonActiveIfTitleNotEmpty() {
        String titleInput = evTitle.getText().toString().trim();
        saveNote.setEnabled(!titleInput.isEmpty());
    }

    // Слушатель spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        adapterView.getItemAtPosition(i).toString();
        importance = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Edit onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Edit onDestroy() called");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "Edit onDestroyView() called");
    }



    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "Edit onDetach() called");
    }


}