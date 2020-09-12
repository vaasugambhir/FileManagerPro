package com.example.filemanagerpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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
        mList.setLayoutManager(new LinearLayoutManager(this));
        adapter.add(mNames, mPaths, mIsFile);
        mList.setAdapter(adapter);
    }

    private void check() {
        if (!checkPerm(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    private void getFiles() {
        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        File[] files = root.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.getName().charAt(0) != '.') {
                mNames.add(file.getName());
                mPaths.add(file.getAbsolutePath());
                if ((new File(mPaths.get(mPaths.size() - 1))).isFile()) {
                    mIsFile.add(true);
                }
                else
                    mIsFile.add(false);
            }
        }
        for (boolean b : mIsFile)
            System.out.println(b);
        System.out.println(mIsFile.size());
        System.out.println(mPaths.size());
        System.out.println(mNames.size());
    }

    public boolean checkPerm (String perm) {
        int check = ContextCompat.checkSelfPermission(this, perm);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
}