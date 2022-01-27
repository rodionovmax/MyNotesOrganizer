package com.gb.mynoteorganizer.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
    private OnNoteClickListener onNoteClickListener;
    private OnPopupMenuClickListener onPopupMenuClickListener;

    // Реализация метода заполнения адаптера recycleview заметками
    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    // Реализация метода удаления заметки в адаптере recycleview
    public void delete(List<Note> notes, int position) {
        this.notes = notes;
        notifyItemRemoved(position);
    }

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }

    public interface OnPopupMenuClickListener {
        void onPopupMenuClick(int command, Note note, int position);
    }

    public void setOnNoteClickListener(OnNoteClickListener noteClickListener) {
        this.onNoteClickListener = noteClickListener;
    }

    public void setOnPopupMenuClickListener(OnPopupMenuClickListener popupMenuClickListener) {
        this.onPopupMenuClickListener = popupMenuClickListener;
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.note_item, parent, false);
        return new NoteHolder(view, onNoteClickListener, onPopupMenuClickListener);
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

    public class NoteHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {

        private TextView title;
        private TextView description;
        private TextView tvDate;
        private TextView tvImportance;
        private ImageView noteMenu;
        private PopupMenu popupMenu;
        private Note note;

        public NoteHolder(@NonNull View itemView, OnNoteClickListener onNoteClickListener, OnPopupMenuClickListener onPopupMenuClickListener) {
            super(itemView);
            title = itemView.findViewById(R.id.note_title);
            description = itemView.findViewById(R.id.note_description);
            tvDate = itemView.findViewById(R.id.note_date);
            tvImportance = itemView.findViewById(R.id.note_importance);

            // Инициализируем и надуваем noteMenu
            noteMenu = itemView.findViewById(R.id.note_menu);
            popupMenu = new PopupMenu(itemView.getContext(), noteMenu);
            popupMenu.inflate(R.menu.context);

            // Listener на нажатие заметки
            itemView.setOnClickListener(view -> onNoteClickListener.onNoteClick(note));

            // Listener на нажатие noteMenu
            noteMenu.setOnClickListener(view -> popupMenu.show());
            popupMenu.setOnMenuItemClickListener(this);
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

        public Note getNote() {
            return note;
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

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.context_add_new:
                    onPopupMenuClickListener.onPopupMenuClick(R.id.context_add_new, null, getAdapterPosition());
                    return true;
                case R.id.context_edit:
                    onPopupMenuClickListener.onPopupMenuClick(R.id.context_edit, note, getAdapterPosition());
                    return true;
                case R.id.context_delete:
                    onPopupMenuClickListener.onPopupMenuClick(R.id.context_delete, note, getAdapterPosition());
                    return true;
                default:
                    return false;
            }
        }
    }
}
