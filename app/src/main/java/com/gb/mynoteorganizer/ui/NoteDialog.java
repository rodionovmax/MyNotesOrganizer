package com.gb.mynoteorganizer.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.gb.mynoteorganizer.R;
import com.gb.mynoteorganizer.data.Constants;
import com.gb.mynoteorganizer.data.Note;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;
import java.util.Objects;

public class NoteDialog extends DialogFragment {

    public static final String TAG = "myCustomLogger";
    private Note note;

    // Интерфейс диалог фрагмента который реализует добавление/изменение заметки в лист фрагменте
    interface NoteDialogController {
        void update(Note note);
        void create(String title, String description);
//        void create(String title, String description, Date date, int importance);
    }

    private NoteDialogController dialogController;

    @Override
    public void onAttach(@NonNull Context context) {
//        if (context instanceof NoteDialogController) {
//            dialogController = (NoteDialogController) context;
//        } else {
//            throw new IllegalStateException("Activity must implement controller");
//        }
        super.onAttach(context);
    }

    // Создаем статический экземпляр диалог фрагмента
    public static NoteDialog getInstance(Note note, NoteDialogController listener) {
        NoteDialog dialog = new NoteDialog(listener);
        if (note != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.NOTE, note);
            dialog.setArguments(bundle);
        }
        return dialog;
    }

    // Конструктор диалог фрагмента
    // Передаем слушатель интерфейса который реализует update и create в лист фрагменте
    private NoteDialog (NoteDialogController listener) {
        this.dialogController = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            note = (Note) bundle.getSerializable(Constants.NOTE);
        }

        String title = "";
        String description = "";
        Date date;
        int importance;
        // TODO: add date and importance

        if (note != null) {
            title = note.getTitle();
            description = note.getDescription();
            date = note.getDate();
            importance = note.getImportance();
        }

        View dialog = LayoutInflater.from(requireContext()).inflate(R.layout.note_dialog, null);

        TextInputLayout dialogTitle = dialog.findViewById(R.id.dialog_title);
        TextInputLayout dialogDescription = dialog.findViewById(R.id.dialog_description);

        if (dialogTitle.getEditText() != null) {
            dialogTitle.getEditText().setText(title);
        }

        Objects.requireNonNull(dialogDescription.getEditText()).setText(description);
        // TODO: set date picker and spinner for importance

        // Собирание диалогового окна
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        String buttonText = "";

        if (note == null) {
            buttonText = "Create";
            builder.setTitle("Create a note");
        } else {
            buttonText = "Edit";
            builder.setTitle("Edit a note");
        }

        builder
                .setView(dialog)
                .setCancelable(true)
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton(buttonText, (dialogInterface, i) -> {
                    if (note == null) {
                        dialogController.create(
                                dialogTitle.getEditText().getText().toString(),
                                dialogDescription.getEditText().getText().toString()
                                // TODO: add date and importance
                        );

                    } else {
                        note.setTitle(dialogTitle.getEditText().getText().toString());
                        note.setDescription(dialogDescription.getEditText().getText().toString());
                        note.setDate(new Date());  // dummy value
                        note.setImportance(1);  // dummy value
                        dialogController.update(note);
                    }
                    dialogInterface.dismiss();
                });

        return builder.create();
    }
}
