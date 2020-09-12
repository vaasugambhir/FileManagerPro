package com.example.filemanagerpro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

    private final Context mContext;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mPaths = new ArrayList<>();

    void add(ArrayList<String> names, ArrayList<String> paths) {
        mNames = names;
        mPaths = paths;
    }

    FileAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.file_layout, parent, false);
        return new FileViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        holder.name.setText(mNames.get(position));
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public static class FileViewHolder extends RecyclerView.ViewHolder {

        ImageView down, right;
        TextView name;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.file_folder_name);
            down = itemView.findViewById(R.id.down_img);
            right = itemView.findViewById(R.id.right_img);
        }
    }
}
