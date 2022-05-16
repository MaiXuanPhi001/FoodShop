package com.example.duan1_nhom3.adapter;

import static com.example.duan1_nhom3.utilities.Constants.BOARD_ID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_nhom3.Dao.FoodBoardDAO;
import com.example.duan1_nhom3.Model.Bill;
import com.example.duan1_nhom3.Model.Food;
import com.example.duan1_nhom3.Model.FoodBoard;
import com.example.duan1_nhom3.R;
import com.example.duan1_nhom3.activity.FoodBoardActivity;
import com.example.duan1_nhom3.activity.InfoHoaDon;

import java.text.DecimalFormat;
import java.util.List;

public class InfoAdapter extends  RecyclerView.Adapter<InfoAdapter.InfoAdapterViewHolder> {

    private Context context;
    private List<FoodBoard> listFoodBoard;
    private DecimalFormat formater;
    private FoodBoardDAO foodBoardDAO;
    private Bitmap bitmap;


    public InfoAdapter(Context context, List<FoodBoard> listFoodBoard) {
        this.context = context;

        this.listFoodBoard = listFoodBoard;
        foodBoardDAO = new FoodBoardDAO(context);
    }


    @NonNull
    @Override
    public InfoAdapter.InfoAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_billinfo, parent, false);
        InfoAdapter.InfoAdapterViewHolder viewHolder = new InfoAdapter.InfoAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InfoAdapter.InfoAdapterViewHolder holder, int position) {
        formater = new DecimalFormat("###,###,### VND");

        holder.tvNameFood.setText(listFoodBoard.get(position).getFoodName());
        holder.tvPrice.setText(formater.format(listFoodBoard.get(position).getFoodPrice()));
        holder.tvNumber.setText("x" + listFoodBoard.get(position).getFoodCount());

        bitmap = BitmapFactory.decodeByteArray(listFoodBoard.get(position).getFoodBoardImg(),0, listFoodBoard.get(position).getFoodBoardImg().length);
        holder.imgFood.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return listFoodBoard.size();
    }

    public class InfoAdapterViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgFood;
        public TextView tvNameFood, tvPrice, tvNumber;


        public InfoAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.iv_food);
            tvNameFood = itemView.findViewById(R.id.tv_name_food);
            tvPrice = itemView.findViewById(R.id.tv_price_food);
            tvNumber = itemView.findViewById(R.id.tv_count);

        }

    }
}
