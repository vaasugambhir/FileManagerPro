package com.example.filemanagerpro;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.io.IOException;
import java.util.Objects;

public class OptionsDialog extends AppCompatDialogFragment {

    private int pos;
    private Button[] buttons;
    private int X;
    private Context context;
    private String path, names;
    private OptionsDialogListener listener;
    private FileAdapter adapter;

    OptionsDialog(int pos, Context context,String path, String names, FileAdapter adapter) {
        this.pos = pos;
        this.context = context;
        this.path = path;
        this.names = names;
        this.adapter = adapter;
    }

    public interface OptionsDialogListener {
        void applyX(int X, int pos, String path, String names, FileAdapter adapter) throws IOException;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (OptionsDialogListener) this.context;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        X = -1;
        buttons = new Button[4];
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        builder.setView(view)
                .setTitle("Options")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            listener.applyX(-1, pos, path, names,adapter);
                        } catch (IOException e) {
                            System.out.println("HERE" + e.getMessage());
                        }
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            listener.applyX(X, pos, path, names,adapter);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        for (int i = 0; i < 4; i++) {
            String id = "button_" + i;
            int resID = getResources().getIdentifier(id, "id", getActivity().getPackageName());
            buttons[i] = view.findViewById(resID);
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int x;
                    switch (v.getId()) {
                        case R.id.button_0: {
                            x = 0;
                            buttons[0].setBackgroundColor(context.getResources().getColor(R.color.colorFiles));
                            buttons[1].setBackgroundColor(Color.GRAY);
                            buttons[2].setBackgroundColor(Color.GRAY);
                            buttons[3].setBackgroundColor(Color.GRAY);
                            break;
                        }
                        case R.id.button_1: {
                            x = 1;
                            buttons[1].setBackgroundColor(context.getResources().getColor(R.color.colorFiles));
                            buttons[0].setBackgroundColor(Color.GRAY);
                            buttons[2].setBackgroundColor(Color.GRAY);
                            buttons[3].setBackgroundColor(Color.GRAY);
                            break;
                        }
                        case R.id.button_2: {
                            x = 2;
                            buttons[2].setBackgroundColor(context.getResources().getColor(R.color.colorFiles));
                            buttons[1].setBackgroundColor(Color.GRAY);
                            buttons[0].setBackgroundColor(Color.GRAY);
                            buttons[3].setBackgroundColor(Color.GRAY);
                            break;
                        }
                        case R.id.button_3: {
                            x = 3;
                            buttons[3].setBackgroundColor(context.getResources().getColor(R.color.colorFiles));
                            buttons[1].setBackgroundColor(Color.GRAY);
                            buttons[2].setBackgroundColor(Color.GRAY);
                            buttons[0].setBackgroundColor(Color.GRAY);
                            break;
                        }
                        default: {
                            x = -1;
                            break;
                        }
                    }
                    assign(x);
                }
            });
        }
        return builder.create();
    }

    private void assign(int x) {
        this.X = x;
    }
}
