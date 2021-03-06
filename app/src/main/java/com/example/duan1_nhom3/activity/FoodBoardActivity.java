package com.example.duan1_nhom3.activity;

import static com.example.duan1_nhom3.utilities.Constants.BOARD_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan1_nhom3.Dao.BillDao;
import com.example.duan1_nhom3.Dao.BoardDAO;
import com.example.duan1_nhom3.Dao.FoodBoardDAO;
import com.example.duan1_nhom3.Dao.FoodDAO;
import com.example.duan1_nhom3.Model.Bill;
import com.example.duan1_nhom3.Model.Food;
import com.example.duan1_nhom3.Model.FoodBoard;
import com.example.duan1_nhom3.R;
import com.example.duan1_nhom3.adapter.FoodAdapter;
import com.example.duan1_nhom3.adapter.FoodNumberAdapter;
import com.example.duan1_nhom3.fragment.FragmentTable;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FoodBoardActivity extends AppCompatActivity {

    private EditText edTra;
    private TextView tvTra, tvPrice;
    private Button btThanhToan, btSave;
    private RecyclerView recyFood, recyFood2;
    private ChipNavigationBar chipNavigationBar;
    private List<Food> listFood;
    private List<FoodBoard> listFoodBoard;
    private FoodDAO foodDAO;
    private BoardDAO boardDAO;
    private BillDao billDao;
    private FoodBoardDAO foodBoardDAO;
    private FoodAdapter adapter;
    private FoodNumberAdapter adapter2;
    private DecimalFormat formater;
    private SearchView searchFood;
    private double tongTien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_board);

        innit();

        // ????? d??? li???u v??o recyclerView
        foodDAO = new FoodDAO(this);
        listFood = foodDAO.getAllFoodType("0", "1");
        adapter = new FoodAdapter(this, listFood);
        recyFood.setLayoutManager(new LinearLayoutManager(this));
        recyFood.setAdapter(adapter);

        // ????? d??? li???u v??o recyclerView2
        foodBoardDAO = new FoodBoardDAO(this);
        listFoodBoard = foodBoardDAO.getFoodByBoardId(BOARD_ID);
        adapter2 = new FoodNumberAdapter(FoodBoardActivity.this, listFoodBoard);
        recyFood2.setLayoutManager(new LinearLayoutManager(this));
        recyFood2.setAdapter(adapter2);

        // Hi???n T???ng ti???n c???a 1 b??n l??n textView
        formater = new DecimalFormat("###,###,### VND");
        tongTien = foodBoardDAO.getPriceBoard(BOARD_ID);
        tvPrice.setText(formater.format(tongTien));

        // Hi???n ti???n th???i l???i cho kh??ch h??ng
        caculatePrice();

        // B???t s??? ki???n n??t l??u
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentResul();
            }
        });

        // B???t s??? ki???n c???a button thanh to??n
        btThanhToanOnlick();

        // B???t s??? ki???n chipnavigationBar
        chipnavOnclick();

        // Ba???t s??? ki???n khi thay ?????i n???i dung c???a edit text
        edTraTextChanged();

        // B???t s??? ki???n search food
        searchFoodEvent();

    }

    private void searchFoodEvent() {
        searchFood.setQueryHint("Search food...");
        searchFood.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void edTraTextChanged() {
        edTra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // T??nh ti???n th???i l???i cho kh??ch h??ng
                caculatePrice();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void caculatePrice() {
        double tienNhan = 0;
        try {
            tienNhan = Double.parseDouble(edTra.getText().toString());
        }catch (Exception e){

        }
        double tienTra = tienNhan - tongTien;
        tvTra.setText(formater.format(tienTra));
    }

    private void chipnavOnclick() {
        chipNavigationBar.setItemSelected(R.id.nav_Fast_Food, true);
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.nav_Fast_Food:
                        listFood.clear();
                        listFood.addAll(foodDAO.getAllFoodType("0", "1"));
                        adapter.notifyDataSetChanged();
                        break;
                    case R.id.nav_Water:
                        listFood.clear();
                        listFood.addAll(foodDAO.getAllFoodType("1", "1"));
                        adapter.notifyDataSetChanged();
                        break;
                    case R.id.nav_Combo:
                        listFood.clear();
                        listFood.addAll(foodDAO.getAllFoodType("2", "1"));
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        });
    }

    private void btThanhToanOnlick() {
        btThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardDAO = new BoardDAO(FoodBoardActivity.this);
                billDao = new BillDao(FoodBoardActivity.this);

                // update status c???a BOARD_STATUS t??? 1 th??nh 0
                boardDAO.updateBoard(BOARD_ID, 0);

                // L???y th???i gian hi???n t???i
                Date day = new Date();
                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(day);

                Bill bill = new Bill(BOARD_ID, date, tongTien);
                billDao.insertBill(bill);

                Toast.makeText(FoodBoardActivity.this, "Check out successfully!", Toast.LENGTH_SHORT).show();

                intentResul();

            }
        });
    }

    private void innit() {
        edTra = findViewById(R.id.ed_tra);
        tvTra = findViewById(R.id.tv_tra);
        tvPrice = findViewById(R.id.tv_price);
        btThanhToan = findViewById(R.id.bt_thanhtoan);
        btSave = findViewById(R.id.bt_save);
        recyFood = findViewById(R.id.recy_food);
        recyFood2 = findViewById(R.id.recy_food2);
        searchFood = findViewById(R.id.searchView);
        chipNavigationBar = findViewById(R.id.chipnavibar_foodboard);
    }

    public void capNhatRecyFood2() {
        // Load l???i d??? li???u listFoodBoard
        listFoodBoard.clear();
        listFoodBoard.addAll(foodBoardDAO.getFoodByBoardId(BOARD_ID));
        adapter2.notifyDataSetChanged();

        // Hi???n T???ng ti???n c???a 1 b??n l??n textView
        formater = new DecimalFormat("###,###,### VND");
        tongTien = foodBoardDAO.getPriceBoard(BOARD_ID);
        tvPrice.setText(formater.format(tongTien));
        // Hi???n s??? ti???n th???i c???a kh??ch h??ng
        caculatePrice();
    }

    // Intent v??? Main Activity
    private void intentResul(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intentResul();
    }
}