package com.gb.mynoteorganizer.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gb.mynoteorganizer.R;
import com.gb.mynoteorganizer.data.Note;
import com.gb.mynoteorganizer.data.Repo;
import com.gb.mynoteorganizer.data.RepoImpl;
import com.gb.mynoteorganizer.recycler.NotesAdapter;


public class NotesListFragment extends Fragment implements NotesAdapter.OnNoteClickListener {

    private static final String NOTE = "NOTE";
    private Repo repo = RepoImpl.getInstance();
    private NotesAdapter adapter;
    private RecyclerView notesList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notes_list, container, false);

        // 1. get a reference to recyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.notes_list);

        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // this is data for recycler view
        fillRepo();

        // 3. create an adapter
        adapter = new NotesAdapter();
        adapter.setNotes(repo.getAll());

        // 4. set adapter
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(this);

        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//
//        fillRepo();
//
//        adapter = new NotesAdapter();
//        adapter.setNotes(repo.getAll());
//
//        adapter.setOnClickListener(this);
//
//        RecyclerView recyclerView = view.findViewById(R.id.notes_list);
////        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onNoteClick(Note note) {

        Fragment editNoteFragment = new EditNoteFragment();
        Bundle args = new Bundle();
        args.putSerializable(NOTE, note);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.notes_list, editNoteFragment)
                .addToBackStack(null)
                .commit();
    }

    private void fillRepo() {
        repo.create(new Note("Title 1", "Description 1"));
        repo.create(new Note("Title 2", "Description 2"));
        repo.create(new Note("Title 3", "Description 3"));
        repo.create(new Note("Title 4", "Description 4"));
        repo.create(new Note("Title 5", "Description 5"));
        repo.create(new Note("Title 6", "Description 6"));
        repo.create(new Note("Title 7", "Description 7"));
        repo.create(new Note("Title 8", "Description 8"));
        repo.create(new Note("Title 9", "Description 9"));
        repo.create(new Note("Title 10", "Description 10"));
        repo.create(new Note("Title 11", "Description 11"));
        repo.create(new Note("Title 12", "Description 12"));
        repo.create(new Note("Title 13", "Description 13"));
        repo.create(new Note("Title 14", "Description 14"));
        repo.create(new Note("Title 15", "Description 15"));
        repo.create(new Note("Title 16", "Description 16"));
    }


}












//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public NotesListFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment NotesListFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static NotesListFragment newInstance(String param1, String param2) {
//        NotesListFragment fragment = new NotesListFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }