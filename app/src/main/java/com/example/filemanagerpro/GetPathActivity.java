package com.example.filemanagerpro;

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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class GetPathActivity extends AppCompatActivity {

    private RecyclerView mList;
    private GetAdapter adapter;
    private ArrayList<String> mNames, mPaths;
    private ArrayList<Boolean> mIsFile;
    private String oldPath, name;
    private int pos;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_path);

        oldPath = "";
        name = "";
        pos = 0;

        button = findViewById(R.id.select_path);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            oldPath = bundle.getString("path");
            name = bundle.getString("name");
            pos = bundle.getInt("pos");
        }

        mNames = new ArrayList<>();
        mPaths = new ArrayList<>();
        mList = findViewById(R.id.list1);
        mIsFile = new ArrayList<>();
        adapter = new GetAdapter(this);

        check();
        getFiles();
        declare();
    }

    public void select(View view) {
        String path = button.getText().toString();
        if (path.equals(""))
            Toast.makeText(this, "Do click on a folder", Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent();
            intent.putExtra("path", path);
            intent.putExtra("oldPath", oldPath);
            intent.putExtra("pos", pos);
            intent.putExtra("name", name);
            setResult(RESULT_OK, intent);
            finish();
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

    private void declare() {
        mList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mList.setLayoutManager(layoutManager);
        adapter.add(mNames, mPaths, mIsFile);
        mList.setAdapter(adapter);
    }

    public boolean checkPerm(String perm) {
        int check = ContextCompat.checkSelfPermission(this, perm);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    private void check() {
        if (!checkPerm(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if (!checkPerm(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
    }
}