package com.gb.mynoteorganizer.ui;

import static com.gb.mynoteorganizer.data.Constants.NOTE;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.gb.mynoteorganizer.recycler.NotesAdapter;

import java.text.SimpleDateFormat;


public class NotesListFragment extends Fragment implements NotesAdapter.OnNoteClickListener {

    private static final String TAG = "myLogger";
    private Repo repo = RepoImpl.getInstance();
    private NotesAdapter adapter;
    private Note note;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "List onCreateView() called with: inflater = [" + inflater + "], container = [" + container + "], savedInstanceState = [" + savedInstanceState + "]");

        return inflater.inflate(R.layout.fragment_notes_list, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated() called with: view = [List], savedInstanceState = [" + savedInstanceState + "]");

        adapter = new NotesAdapter();
        adapter.setNotes(repo.getAll());

        adapter.setOnClickListener(this);

        RecyclerView recyclerView = view.findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

//        Bundle bundle = getArguments();
//        if (savedInstanceState != null) {
//            if (isLandscape()) {
//                showLandEditNotes(bundle);
//            }
//        }
    }

    @Override
    public void onNoteClick(Note note) {
        Bundle args = new Bundle();
        args.putSerializable(NOTE, note);
        createEditNoteFragment(args);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    /*
     * Method to create a new note
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.main_create) {
            createEditNoteFragment(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createEditNoteFragment(Bundle bundle) {
        if (isLandscape()) {
            showLandEditNotes(bundle);
        } else {
            showPortEditNotes(bundle);
        }
    }

    private void showLandEditNotes(Bundle bundle) {
        Fragment editNoteFragment = new EditNoteFragment();
        if (bundle != null) {
            editNoteFragment.setArguments(bundle);
        }

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.edit_note_container_land, editNoteFragment)
                .commit();
    }

    private void showPortEditNotes(Bundle bundle) {
        Fragment editNoteFragment = new EditNoteFragment();
        if (bundle != null) {
            editNoteFragment.setArguments(bundle);
        }

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.notes_list_fragment_holder, editNoteFragment)
                .addToBackStack(null)
                .commit();
    }

    public boolean isLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "List onDestroy() called");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "List onDestroyView() called");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "List onAttach() called with: context = [" + context + "]");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "List onDetach() called");
    }
}

