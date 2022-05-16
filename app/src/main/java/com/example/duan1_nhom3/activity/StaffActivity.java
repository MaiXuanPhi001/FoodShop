package com.example.duan1_nhom3.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.duan1_nhom3.Dao.StaffDAO;
import com.example.duan1_nhom3.Model.Food;
import com.example.duan1_nhom3.Model.Staff;
import com.example.duan1_nhom3.R;
import com.example.duan1_nhom3.adapter.StaffAdapter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StaffActivity extends AppCompatActivity {

    RecyclerView rvStaff;
    StaffDAO dao;
    List<Staff> list = new ArrayList<>();
    StaffAdapter adapter;
    ImageView ivAddStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        rvStaff = findViewById(R.id.rv_Staff);
        ivAddStaff = findViewById(R.id.ivAddStaff);

        dao = new StaffDAO(this);
        list = dao.getAllStaff();

        adapter = new StaffAdapter(this, (ArrayList<Staff>) list, this);
        rvStaff.setLayoutManager(new LinearLayoutManager(this));
        rvStaff.setAdapter(adapter);

        ivAddStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffActivity.this, AddStaffActivity.class);
                intent.setAction("AddStaff");
                activityResultLauncher.launch(intent);
            }
        });
    }

    public ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getData() != null){
                        list.clear();
                        list.addAll(dao.getAllStaff());
                        adapter.notifyDataSetChanged();
                    }
                }
            }
    );
}