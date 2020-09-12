package com.example.filemanagerpro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

    private final Context mContext;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mPaths = new ArrayList<>();
    private ArrayList<Boolean> mIsFile = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> paths = new ArrayList<>();
    private ArrayList<Boolean> isFile = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private FileAdapter adapter;
    private View mView;

    interface ItemClickListener{
        void onClick(View view, int pos);
    }

    void add(ArrayList<String> names, ArrayList<String> paths, ArrayList<Boolean> isFile) {
        mNames.addAll(names);
        mPaths.addAll(paths);
        mIsFile.addAll(isFile);
    }

    FileAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.file_layout, parent, false);
        mView = v;
        return new FileViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final FileViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        String n = mNames.get(position);
        holder.name.setText(n);
        final File newFile = new File(mPaths.get(position));
        final File[] folder = newFile.listFiles();

        //recyclerView.getRecycledViewPool().setMaxRecycledViews(TYPE_CAROUSEL, 0);


        if (mIsFile.get(position)) {
            System.out.println(mNames.get(position));
            holder.right.setVisibility(View.GONE);
            holder.file.setVisibility(View.VISIBLE);
            holder.folder.setVisibility(View.GONE);
        }

        System.out.println("---");
        for (boolean b : mIsFile)
            System.out.println("F " + b);
        System.out.println("---");

        holder.setListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                if (newFile.isDirectory()) {
                    if (Objects.requireNonNull(newFile.listFiles()).length == 0) {
                        Toast.makeText(mContext, "empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (holder.down.getVisibility() == View.GONE && holder.right.getVisibility() == View.VISIBLE) {
                        names.clear();
                        isFile.clear();
                        paths.clear();
                        assert folder!=null;
                        for (File file : folder) {
                            names.add(file.getName());
                            paths.add(file.getPath());
                            if ((new File(paths.get(paths.size() - 1))).isFile()) {
                                isFile.add(true);
                            }
                            else
                                isFile.add(false);
                        }
                        holder.down.setVisibility(View.VISIBLE);
                        holder.right.setVisibility(View.GONE);
                        recyclerView = view.findViewById(R.id.inside_rec);
                        recyclerView.setHasFixedSize(true);
                        layoutManager = new LinearLayoutManager(mContext);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter = new FileAdapter(mContext);
                        adapter.add(names, paths, isFile);
                        recyclerView.setAdapter(adapter);
                    }
                    else if (holder.right.getVisibility() == View.GONE && holder.down.getVisibility() == View.VISIBLE) {
                        names.clear();
                        paths.clear();
                        isFile.clear();
                        holder.down.setVisibility(View.GONE);
                        holder.right.setVisibility(View.VISIBLE);
                        recyclerView = view.findViewById(R.id.inside_rec);
                        recyclerView.setHasFixedSize(true);
                        layoutManager = new LinearLayoutManager(mContext);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter = new FileAdapter(mContext);
                        adapter.add(names, paths, isFile);
                        recyclerView.setAdapter(adapter);
                    }
                    else if (holder.right.getVisibility() == View.GONE && holder.down.getVisibility() == View.GONE) {
                        System.out.println();
                    }
                }
                else {
                    String x = mNames.get(position);
                    String y = x.substring(x.lastIndexOf('.')+1);
                    switch (y.toLowerCase()) {
                        case "pdf": {
                            File file = new File(mPaths.get(position));
                            Uri uri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", file);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(uri, "application/pdf");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            mContext.startActivity(intent);
                            break;
                        }
                        case "jpg":

                        case "jpeg":

                        case "png": {
                            File file = new File(mPaths.get(position));
                            Uri uri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", file);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(uri, "image/*");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            mContext.startActivity(intent);
                            break;
                        }
                        case "mp3": {
                            File file = new File(mPaths.get(position));
                            Uri uri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", file);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(uri, "audio/*");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            mContext.startActivity(intent);
                            break;
                        }
                        case "mp4":
                            File file = new File(mPaths.get(position));
                            Uri uri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", file);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(uri, "video/*");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            mContext.startActivity(intent);
                            break;
                        default:
                            Toast.makeText(mContext, "Sorry, cannot open file type", Toast.LENGTH_SHORT).show();
                            break;
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
        ItemClickListener listener;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.file_folder_name);
            down = itemView.findViewById(R.id.down_img);
            right = itemView.findViewById(R.id.right_img);
            file = itemView.findViewById(R.id.file);
            folder = itemView.findViewById(R.id.folder);
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
