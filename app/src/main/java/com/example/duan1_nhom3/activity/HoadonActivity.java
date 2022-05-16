package com.example.duan1_nhom3.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.duan1_nhom3.Dao.BillDao;
import com.example.duan1_nhom3.Model.Bill;
import com.example.duan1_nhom3.R;
import com.example.duan1_nhom3.adapter.Billadapter;

import java.util.List;

public class HoadonActivity extends AppCompatActivity {
    private TextView tv_table, tv_gia, tv_date;
    private BillDao billDao;
    List<Bill> listbill;
    RecyclerView Recy_Bill;
    private Billadapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoadon);

        tv_table = findViewById(R.id.tv_table);
        tv_gia= findViewById(R.id.tv_gia);
        tv_date=findViewById(R.id.tv_date);
        Recy_Bill= findViewById(R.id.Recy_Bill);

        billDao = new BillDao(this);
        listbill = billDao.getAllBill();
        adapter = new Billadapter(listbill, HoadonActivity.this);
        Recy_Bill.setLayoutManager(new LinearLayoutManager(HoadonActivity.this));
        Recy_Bill.setAdapter(adapter);
    }
}