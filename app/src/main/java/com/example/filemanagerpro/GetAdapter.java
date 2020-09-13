package com.example.filemanagerpro;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class GetAdapter extends RecyclerView.Adapter<GetAdapter.MyViewHolder> {

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mPaths = new ArrayList<>();
    private ArrayList<Boolean> mIsFile = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> paths = new ArrayList<>();
    private ArrayList<Boolean> isFile = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private GetAdapter adapter;
    private Context mContext;
    private static String path;

    public interface ItemClickListener{
        void onClick(View v, int pos);
    }

    void add(ArrayList<String> names, ArrayList<String> paths, ArrayList<Boolean> isFile) {
        mNames.addAll(names);
        mPaths.addAll(paths);
        mIsFile.addAll(isFile);
    }

    public GetAdapter (Context context) {
        path = "";
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.get_layout, parent, false);
        return new GetAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        String n = mNames.get(position);
        holder.name.setText(n);
        final File newFile = new File(mPaths.get(position));
        final File[] folder = newFile.listFiles();

        if (mIsFile.get(position)) {
            holder.right.setVisibility(View.GONE);
            holder.file.setVisibility(View.VISIBLE);
            holder.folder.setVisibility(View.GONE);
        }

        holder.setListener(new GetAdapter.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Button button = ((Activity)mContext).findViewById(R.id.select_path);
                path = mPaths.get(position);
                button.setText(path);
                if (newFile.isDirectory()) {
                    if (Objects.requireNonNull(newFile.listFiles()).length == 0) {
                        Toast.makeText(mContext, "empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (holder.down.getVisibility() == View.GONE && holder.right.getVisibility() == View.VISIBLE) {
                        names.clear();
                        isFile.clear();
                        paths.clear();
                        assert folder != null;
                        for (File file : folder) {
                            names.add(file.getName());
                            paths.add(file.getPath());
                            if ((new File(paths.get(paths.size() - 1))).isFile()) {
                                isFile.add(true);
                            } else
                                isFile.add(false);
                        }
                        holder.down.setVisibility(View.VISIBLE);
                        holder.right.setVisibility(View.GONE);
                        recyclerView = view.findViewById(R.id.inside_rec1);
                        recyclerView.setHasFixedSize(true);
                        layoutManager = new LinearLayoutManager(mContext);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter = new GetAdapter(mContext);
                        adapter.add(names, paths, isFile);
                        recyclerView.setAdapter(adapter);
                    } else if (holder.right.getVisibility() == View.GONE && holder.down.getVisibility() == View.VISIBLE) {
                        names.clear();
                        paths.clear();
                        isFile.clear();
                        holder.down.setVisibility(View.GONE);
                        holder.right.setVisibility(View.VISIBLE);
                        recyclerView = view.findViewById(R.id.inside_rec1);
                        recyclerView.setHasFixedSize(true);
                        layoutManager = new LinearLayoutManager(mContext);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter = new GetAdapter(mContext);
                        adapter.add(names, paths, isFile);
                        recyclerView.setAdapter(adapter);
                    } else if (holder.right.getVisibility() == View.GONE && holder.down.getVisibility() == View.GONE) {
                        System.out.println();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return getFolderCount();
    }

    private int getFolderCount() {
        return mNames.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView down, right, file, folder;
        TextView name;
        GetAdapter.ItemClickListener listener;
        LinearLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.file_folder_name1);
            down = itemView.findViewById(R.id.down_img1);
            right = itemView.findViewById(R.id.right_img1);
            file = itemView.findViewById(R.id.file1);
            folder = itemView.findViewById(R.id.folder1);
            layout = itemView.findViewById(R.id.layer1);
        }

        public void setListener(GetAdapter.ItemClickListener listener) {
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    public static String getPath() {
        return path;
    }
}
