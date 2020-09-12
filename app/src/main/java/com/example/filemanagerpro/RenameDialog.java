package com.example.filemanagerpro;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

public class RenameDialog extends AppCompatDialogFragment {
    String path;
    int pos;
    String pre_name;
    EditText name;
    OnClick listener;

    RenameDialog(String path, String name, int pos) {
        this.pos = pos;
        this.path = path;
        this.pre_name = name;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (OnClick) context;
    }

    public interface OnClick{
        void onClick(String pre_name, String name, String path, int pos);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.rename_layout, null);
        name = view.findViewById(R.id.name);
        builder.setView(view)
                .setTitle("Options")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onClick(pre_name, name.getText().toString(), path, pos);
                    }
                });

        return builder.create();
    }

}
