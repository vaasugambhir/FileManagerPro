package com.example.filemanagerpro;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements OptionsDialog.OptionsDialogListener, RenameDialog.OnClick {

    private RecyclerView mList;
    private FileAdapter adapter;
    private ArrayList<String> mNames, mPaths;
    private ArrayList<Boolean> mIsFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNames = new ArrayList<>();
        mPaths = new ArrayList<>();
        mList = findViewById(R.id.list);
        mIsFile = new ArrayList<>();
        adapter = new FileAdapter(this);

        check();
        getFiles();
        declare();
    }


    private void declare() {
        mList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mList.setLayoutManager(layoutManager);
        adapter.add(mNames, mPaths, mIsFile);
        mList.setAdapter(adapter);
    }

    private void check() {
        if (!checkPerm(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if (!checkPerm(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
    }

    private void getFiles() {
        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        File[] files = root.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.getName().charAt(0) != '.') {
                mNames.add(file.getName());
                mPaths.add(file.getPath());
                if ((new File(mPaths.get(mPaths.size() - 1))).isFile()) mIsFile.add(true);
                else mIsFile.add(false);
            }
        }
    }

    public boolean checkPerm(String perm) {
        int check = ContextCompat.checkSelfPermission(this, perm);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void applyX(int X, int pos, String path, String name, FileAdapter adapter) throws IOException {
        switch (X) {
            case 0: {
                delete(pos, path, name, adapter);
                break;
            }
            case 1: {
                RenameDialog renameDialog = new RenameDialog(path, name, pos);
                renameDialog.show(getSupportFragmentManager(), "rename dialog");
                break;
            }
            case 2: {
                Intent intent = new Intent(this, GetPathActivity.class);
                intent.putExtra("path", path);
                intent.putExtra("pos", pos);
                intent.putExtra("name", name);
                startActivityForResult(intent, 1);
                break;
            }
        }
    }

    private void move(int pos, String path, String name, String newPath) {
        if (newPath.equals("")) {
            Toast.makeText(this, "move failed", Toast.LENGTH_SHORT).show();
        } else {
            File oldFile = new File(path);
            File newFile = new File(newPath);
            newPath = newPath + "/" + name;
            System.out.println(newPath);
            System.out.println(path);
            System.out.println(name);
            if (oldFile.renameTo(newFile)) {
                Toast.makeText(this, name + " moved to " + newPath, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "file move failed", Toast.LENGTH_SHORT).show();
            }

            adapter.notifyDataSetChanged();
        }
    }

    private void delete(int pos, String p, String n, FileAdapter adapter1) throws IOException {
        File file = new File(p);
        if (!file.exists()) {
            Toast.makeText(this, "File DNE", Toast.LENGTH_SHORT).show();
            return;
        }
        System.out.println(n + " " + p);
        if (file.delete()) {
            Toast.makeText(this, n + " got deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "deletion failed", Toast.LENGTH_SHORT).show();
            throw new IOException("failed to delete " + file);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(String pre_name, String name, String oldPath, int pos) {

        File oldFile = new File(oldPath);
        if (oldFile.isFile()) {
            String newPath = new File(oldPath).getParent() + "/" + name + pre_name.substring(pre_name.lastIndexOf('.'));
            System.out.println(oldPath);
            System.out.println(newPath);
            File newFile = new File(newPath);
            if (oldFile.renameTo(newFile)) {
                Toast.makeText(this, pre_name + " renamed to " + name, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "file rename failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            String newPath = new File(oldPath).getParent() + "/" + name;
            System.out.println(oldPath);
            System.out.println(newPath);
            File newFile = new File(newPath);
            if (oldFile.renameTo(newFile)) {
                Toast.makeText(this, pre_name + " renamed to " + name, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "file rename failed", Toast.LENGTH_SHORT).show();
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                assert data != null;

                String newPath = data.getStringExtra("path");
                String oldPath = data.getStringExtra("oldPath");
                String name = data.getStringExtra("name");
                int pos = Objects.requireNonNull(data.getExtras()).getInt("pos");

                move(pos, oldPath, name, newPath);
            }
        }
    }
}