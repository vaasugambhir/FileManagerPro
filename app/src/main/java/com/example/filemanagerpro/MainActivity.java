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

    interface ItemClickListener{
        void onClick(View view, int pos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNames = new ArrayList<>();
        mPaths = new ArrayList<>();
        mList = findViewById(R.id.list);
        adapter = new FileAdapter(this);

        check();
        getFiles();
        declare();
    }

    private void declare() {
        mList.setLayoutManager(new LinearLayoutManager(this));
        adapter.add(mNames, mPaths);
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
            mNames.add(file.getName());
            mPaths.add(file.getAbsolutePath());
        }
    }

    public boolean checkPerm (String perm) {
        int check = ContextCompat.checkSelfPermission(this, perm);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
}