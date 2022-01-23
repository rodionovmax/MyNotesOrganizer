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
import com.gb.mynoteorganizer.recycler.NotesAdapter;

import java.util.Date;


public class NotesListFragment extends Fragment
        implements NotesAdapter.OnNoteClickListener,
        NotesAdapter.OnPopupMenuClickListener,
        NoteDialog.NoteDialogController
{

    private static final String TAG = "myLogger";
    private final Repo repo = RepoImpl.getInstance();
    private NotesAdapter adapter;

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

        adapter = new NotesAdapter();
        adapter.setNotes(repo.getAll());

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
            }
        });
        touchHelper.attachToRecyclerView(recyclerView);
    }

    // Реализуем клик на земетку
    @Override
    public void onNoteClick(Note note) {

        // переименовать в то что реализуется
        createEditNoteFragment(note);
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
            createEditNoteFragment(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createEditNoteFragment(Note note) {
        if (isLandscape()) {
            listener.replaceEditNoteLand(note);
//            showLandEditNotes(note);
        } else {
            listener.replaceEditNotePort(note);
//            showPortEditNotes(bundle);
        }
    }

//    private void showLandEditNotes(Bundle bundle) {
//        Fragment editNoteFragment = new EditNoteFragment();
//        if (bundle != null) {
//            editNoteFragment.setArguments(bundle);
//        }
//
//        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//        fragmentManager
//                .beginTransaction()
//                .replace(R.id.edit_note_container_land, editNoteFragment)
//                .commit();
//    }



//    private void showPortEditNotes(Bundle bundle) {
//        Fragment editNoteFragment = new EditNoteFragment();
//        if (bundle != null) {
//            editNoteFragment.setArguments(bundle);
//        }
//
//        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//        fragmentManager
//                .beginTransaction()
//                .replace(R.id.notes_list_fragment_holder, editNoteFragment)
//                .addToBackStack(null)
//                .commit();
//    }

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
    }

    @Override
    public void create(String title, String description, Date date, int importance) {
        repo.create(title, description, date, importance);
        adapter.setNotes(repo.getAll());
    }

}

