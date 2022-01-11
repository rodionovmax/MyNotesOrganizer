package com.gb.mynoteorganizer.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gb.mynoteorganizer.R;
import com.gb.mynoteorganizer.data.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        private TextView tvDate;
        private TextView tvImportance;
        private Note note;

        public NoteHolder(@NonNull View itemView, NotesAdapter.OnNoteClickListener listener) {
            super(itemView);
            title = itemView.findViewById(R.id.note_title);
            description = itemView.findViewById(R.id.note_description);
            tvDate = itemView.findViewById(R.id.note_date);
            tvImportance = itemView.findViewById(R.id.note_importance);
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

            if (note.getDate() != null) {
                tvDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(note.getDate()));
            } else {
                tvDate.setText("");
            }

            if (note.getImportance() != 0) {
                tvImportance.setText(setImportanceText(note.getImportance()));
            }

            // Color text depends on importance
            switch (note.getImportance()) {
                case 0:
                    ;
                    break;
                case 1:
                    tvImportance.setTextColor(ContextCompat.getColor(tvImportance.getContext(), R.color.orange));
                    break;
                case 2:
                    tvImportance.setTextColor(ContextCompat.getColor(tvImportance.getContext(), R.color.red));
                    break;
            }
        }

        private String setImportanceText(int i) {
            String importance;
            switch (i) {
                case 0:
                    importance = "Not important";
                    break;
                case 1:
                    importance = "Important";
                    break;
                case 2:
                    importance = "Critical";
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + i);
            }
            return importance;
        }

    }
}
