package com.gb.mynoteorganizer.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gb.mynoteorganizer.R;
import com.gb.mynoteorganizer.data.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteHolder> {

    private List<Note> notes = new ArrayList<>();

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }

    private OnNoteClickListener listener;

    public void setOnClickListener(OnNoteClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.note_item, parent, false);
        return new NoteHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note note = notes.get(position);
        holder.bind(note);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class NoteHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private Note note;


        public NoteHolder(@NonNull View itemView, NotesAdapter.OnNoteClickListener listener) {
            super(itemView);
            title = itemView.findViewById(R.id.note_title);
            description = itemView.findViewById(R.id.note_description);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onNoteClick(note);
                }
            });
        }

        void bind(Note note) {
            this.note = note;
            title.setText(note.getTitle());
            description.setText(note.getDescription());
        }
    }
}
