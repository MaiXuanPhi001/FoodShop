package com.example.duan1_nhom3.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.duan1_nhom3.Dao.FoodDAO;
import com.example.duan1_nhom3.R;
import com.example.duan1_nhom3.fragment.FragmentFood;
import com.example.duan1_nhom3.fragment.FragmentOption;
import com.example.duan1_nhom3.fragment.FragmentOrder;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.duan1_nhom3.fragment.FragmentTable;
public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FoodDAO foodDAO = new FoodDAO(this);

        // Hàm Ánh xạ
        init();

        // Đổ fragment_table vào FragmentContainerView
        fragmentManager.beginTransaction().replace(R.id.fragmnet_context, FragmentTable.newInstance(null,null)).commit();

        // Hàm bắt sự kiện bottomNavigation
        bottomNavigationOnclick();
    }

    // Ánh xạ
    private void init() {
        bottomNavigationView    = findViewById(R.id.bottom_navi);
        fragmentManager         = getSupportFragmentManager();
    }

    // Bắt sự kiện bottomNavigation
    public void bottomNavigationOnclick() {
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener((item) ->{
            switch (item.getItemId()){
                case R.id.nav_Fast_Food:
                    fragmentManager.beginTransaction().replace(R.id.fragmnet_context, FragmentTable.newInstance(null,null)).commit();
                    break;
                case R.id.nav_Water:
                    fragmentManager.beginTransaction().replace(R.id.fragmnet_context, FragmentFood.newInstance(null,null)).commit();
                    break;
                case R.id.nav_order:
                    fragmentManager.beginTransaction().replace(R.id.fragmnet_context, FragmentOrder.newInstance(null,null)).commit();
                    break;
                case R.id.nav_Combo:
                    fragmentManager.beginTransaction().replace(R.id.fragmnet_context, FragmentOption.newInstance(null, null)).commit();
                    break;
            }
            return true;
        });
    }

    public  void myIntent(){
        Intent intent = new Intent(MainActivity.this, FoodBoardActivity.class);
        startActivityForResult(intent, 999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999){
            Log.e("code", requestCode+"");
            fragmentManager.beginTransaction().replace(R.id.fragmnet_context, FragmentTable.newInstance(null,null)).commit();
        }
    }
}