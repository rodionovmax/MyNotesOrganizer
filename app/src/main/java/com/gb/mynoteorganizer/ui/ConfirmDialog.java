package com.gb.mynoteorganizer.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.gb.mynoteorganizer.R;


public class ConfirmDialog extends DialogFragment {

    private OnConfirmationDialogClickListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        listener = (OnConfirmationDialogClickListener) context;
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.exit_dialog_title)
                .setMessage(R.string.exit_dialog_message)
                .setCancelable(true);

        builder.setNegativeButton(R.string.button_text_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.setPositiveButton(R.string.button_text_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onConfirm();
                dialogInterface.dismiss();
            }
        });

        return builder.create();
    }

    public interface OnConfirmationDialogClickListener {
        void onConfirm();
    }
}
