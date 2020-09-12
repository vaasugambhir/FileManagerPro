package com.example.filemanagerpro;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

    private final Context mContext;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mPaths = new ArrayList<>();

    interface ItemClickListener{
        void onClick(View view, int pos);
    }

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
    public void onBindViewHolder(@NonNull final FileViewHolder holder, int position) {
        String n = mNames.get(position);
        holder.name.setText(n);
        /*
        if ((new File(mPaths.get(position))).isFile()) {
            holder.right.setVisibility(View.GONE);
        }
         */
        holder.setListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                File newFile = new File(mPaths.get(pos));
                if (newFile.isDirectory()) {
                    File[] folder = newFile.listFiles();
                    ArrayList<String> names = new ArrayList<>();
                    ArrayList<String> paths = new ArrayList<>();
                    assert folder != null;
                    for (File file : folder) {
                        names.add(file.getName());
                        paths.add(file.getAbsolutePath());
                    }
                    if (holder.down.getVisibility() == View.GONE && holder.right.getVisibility() == View.VISIBLE) {
                        holder.down.setVisibility(View.VISIBLE);
                        holder.right.setVisibility(View.GONE);
                        holder.insideRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        FileAdapter adap = new FileAdapter(mContext);
                        adap.add(names, paths);
                        holder.insideRecyclerView.setAdapter(adap);
                    }
                    else if (holder.right.getVisibility() == View.GONE && holder.down.getVisibility() == View.VISIBLE) {
                        names.clear();
                        paths.clear();
                        holder.down.setVisibility(View.GONE);
                        holder.right.setVisibility(View.VISIBLE);
                        holder.insideRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        FileAdapter adap = new FileAdapter(mContext);
                        adap.add(names, paths);
                        holder.insideRecyclerView.setAdapter(adap);
                    }
                    else if (holder.right.getVisibility() == View.GONE && holder.down.getVisibility() == View.GONE) {
                        System.out.println();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public static class FileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView down, right, file, folder;
        TextView name;
        RecyclerView insideRecyclerView;
        ItemClickListener listener;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.file_folder_name);
            down = itemView.findViewById(R.id.down_img);
            right = itemView.findViewById(R.id.right_img);
            file = itemView.findViewById(R.id.file);
            folder = itemView.findViewById(R.id.folder);
            insideRecyclerView = itemView.findViewById(R.id.inside_rec);
        }

        public void setListener(ItemClickListener listener) {
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }
}
