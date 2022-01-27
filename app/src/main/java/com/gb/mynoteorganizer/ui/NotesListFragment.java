package com.gb.mynoteorganizer.ui;

import static com.gb.mynoteorganizer.data.Constants.NOTE_NEW;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.gb.mynoteorganizer.R;
import com.gb.mynoteorganizer.data.Constants;
import com.gb.mynoteorganizer.data.Note;
import com.gb.mynoteorganizer.data.Repo;
import com.gb.mynoteorganizer.data.RepoImpl;
import com.gb.mynoteorganizer.data.SharedPref;
import com.gb.mynoteorganizer.data.SharedPrefsRepo;
import com.gb.mynoteorganizer.recycler.NotesAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NotesListFragment extends Fragment
        implements NotesAdapter.OnNoteClickListener,
        NotesAdapter.OnPopupMenuClickListener,
        NoteDialog.NoteDialogController,
        SharedPrefsRepo
{

    private static final String TAG = "myLogger";
    private final Repo repo = RepoImpl.getInstance();
    private NotesAdapter adapter;
    private Gson gson = new Gson();
    List<Note> savedNotes = new ArrayList<>();


    private InterfaceMainActivity listener;

    // Создаем статический экземпляр лист фрагмента чтобы вызывать его в активити
    public static Fragment newInstance(boolean isNoteNew) {
        Fragment fragment = new NotesListFragment();
        if (isNoteNew) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(NOTE_NEW, true);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "List onAttach() called with: context = [" + context + "]");

        // Для инициализации интерфейса. Чтобы не привязывать интерфейс к конструктору
        if (context instanceof InterfaceMainActivity) {
            listener = (InterfaceMainActivity) context;
        }

        SharedPref.init(context.getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "List onCreateView() called");

        return inflater.inflate(R.layout.fragment_notes_list, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated() called with: view = [List], savedInstanceState = [" + savedInstanceState + "]");

        // Получаем notes из shared preferences
        savedNotes = getNotes();

        adapter = new NotesAdapter();
        adapter.setNotes(savedNotes);

        adapter.setOnNoteClickListener(this);
        adapter.setOnPopupMenuClickListener(this);

        RecyclerView recyclerView = view.findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        // Для удаления заметки по swipe
        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(0, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                NotesAdapter.NoteHolder holder = (NotesAdapter.NoteHolder) viewHolder;
                Note note = holder.getNote();
                repo.delete(note);
                adapter.delete(repo.getAll(), position);

                // Сохраняем изменение List<Notes> в shared preferences
                String notesString = gson.toJson(repo.getAll());
                SharedPref.write(Constants.NOTES_KEY, notesString);
            }
        });
        touchHelper.attachToRecyclerView(recyclerView);
    }

    // Реализуем клик на заметку
    @Override
    public void onNoteClick(Note note) {
        createEditFragment(note);
    }


    // Создание top bar menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    // Создание новой заметки по клику на top bar menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.main_create) {
            createEditFragment(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createEditFragment(Note note) {
        if (isLandscape()) {
            listener.replaceEditLand(note);
        } else {
            listener.replaceEditPort(note);
        }
    }

    public boolean isLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    // Реализуем клик на элемент контекстного меню
    @Override
    public void onPopupMenuClick(int command, Note note, int position) {
        if (command == R.id.context_add_new) {
            createDialog(null);
        } else if (command == R.id.context_edit) {
            createDialog(note);
        } else if (command == R.id.context_delete) {
            repo.delete(note);
            adapter.delete(repo.getAll(), position);

            // Сохраняем изменение List<Notes> в shared preferences
            String notesString = gson.toJson(repo.getAll());
            SharedPref.write(Constants.NOTES_KEY, notesString);
        } else {
            throw new IllegalArgumentException("Undefined command argument was received");
        }
    }

    // Создание диалог фрагмента
    private void createDialog(Note note) {
        NoteDialog.getInstance(note, this)
                .show(
                        requireActivity().getSupportFragmentManager(),
                        Constants.NOTE
                );
    }

    // Реализуем методы интерфейса NoteDialogController
    @Override
    public void update(Note note) {
        repo.update(note);
        adapter.setNotes(repo.getAll());

        // Сохраняем изменение List<Notes> в shared preferences
        String notesString = gson.toJson(repo.getAll());
        SharedPref.write(Constants.NOTES_KEY, notesString);
    }

    @Override
    public void create(String title, String description, Date date, int importance) {
        repo.create(title, description, date, importance);
        adapter.setNotes(repo.getAll());

        // Сохраняем изменение List<Notes> в shared preferences
        String notesString = gson.toJson(repo.getAll());
        SharedPref.write(Constants.NOTES_KEY, notesString);
    }

    @Override
    public ArrayList<Note> getNotes() {
        String sharedPrefsNotes = SharedPref.read(Constants.NOTES_KEY, "");
        ArrayList<Note> notes =  gson.fromJson(sharedPrefsNotes, new TypeToken<List<Note>>(){}.getType());
        if (notes == null) {
            notes = new ArrayList<>();
        }
        return notes;
    }
}

