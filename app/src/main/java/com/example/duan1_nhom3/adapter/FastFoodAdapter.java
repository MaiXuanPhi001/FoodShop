package com.example.duan1_nhom3.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_nhom3.Dao.FoodDAO;
import com.example.duan1_nhom3.Model.Food;
import com.example.duan1_nhom3.R;
import com.example.duan1_nhom3.fragment.FragmentCombo;
import com.example.duan1_nhom3.fragment.FragmentFastFood;
import com.example.duan1_nhom3.fragment.FragmentWater;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class FastFoodAdapter extends RecyclerView.Adapter<FastFoodAdapter.FastFoodViewHolder>{

    Context context;
    ArrayList<Food> listFood;
    FoodDAO foodDAO;
    DecimalFormat formater;
    Fragment fragment;
    ImageView imageView;
    Bitmap bitmap;

    public FastFoodAdapter(Context context, ArrayList<Food> listFood, Fragment fragment){
        this.context = context;
        this.listFood = listFood;
        this.fragment = fragment;
        foodDAO = new FoodDAO(context);
    }

    public void setBitmap (Bitmap bitmap){
        this.bitmap = bitmap;
        imageView.setImageBitmap(bitmap);
    }

    @NonNull
    @Override
    public FastFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_food, parent, false);
        FastFoodViewHolder viewHolder = new FastFoodViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FastFoodViewHolder holder, int position) {
        formater = new DecimalFormat("###,###,### VND");
        Food food = listFood.get(position);
        holder.tvNameFastFood.setText(food.getFood_name());
        holder.tvPriceFastFood.setText(formater.format(food.getFood_price()));

        Bitmap bitmap = BitmapFactory.decodeByteArray(food.getFood_image(), 0, food.getFood_image().length);
        holder.imgFastFood.setImageBitmap(bitmap);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick){
                    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    food.setFood_status(0);
                                    foodDAO.updateStatusFood(food);
//                                    foodDAO.deleteFood(food.getFood_id() +"");
                                    listFood.clear();
                                    switch (fragment.getClass().toString()){
                                        case "class com.example.duan1_nhom3.fragment.FragmentFastFood":
                                            listFood.addAll(foodDAO.getAllFoodType("0", "1"));
                                            break;
                                        case "class com.example.duan1_nhom3.fragment.FragmentWater":
                                            listFood.addAll(foodDAO.getAllFoodType("1", "1"));
                                            break;
                                        case "class com.example.duan1_nhom3.fragment.FragmentCombo":
                                            listFood.addAll(foodDAO.getAllFoodType("2", "1"));
                                            break;
                                    }

                                    notifyDataSetChanged();
                                    break;
                                case  DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Do you want delete this item?")
                            .setPositiveButton("Yes", dialogListener)
                            .setNegativeButton("No", dialogListener).show();
                }else {
                    openDialogUpdate(food);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listFood.size();
    }

    public class FastFoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        TextView tvNameFastFood, tvPriceFastFood;
        ImageView imgFastFood;
        private ItemClickListener itemClickListener;

        public FastFoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameFastFood = itemView.findViewById(R.id.tv_name_food);
            tvPriceFastFood = itemView.findViewById(R.id.tv_price_food);
            imgFastFood = itemView.findViewById(R.id.iv_food);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return true;
        }
    }

    public byte[] imageToByte(ImageView iv){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) iv.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void openDialogUpdate(Food food1){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view1 = inflater.inflate(R.layout.dialog_update_item, null);
        builder.setView(view1);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);

        //anh xa
        imageView = view1.findViewById(R.id.iv_photo);
        EditText edtNameItem = view1.findViewById(R.id.edtNameItem);
        EditText edtPriceItem = view1.findViewById(R.id.edtPriceItem);
        AppCompatButton btnChoosePhoto = view1.findViewById(R.id.btnChoosePhoto);
        AppCompatButton btnAddItem = view1.findViewById(R.id.btnAddItem);
        AppCompatButton btnCancelItem = view1.findViewById(R.id.btnCancelItem);


        //gan gia tri cho view
        edtNameItem.setText(food1.getFood_name());
        edtPriceItem.setText(food1.getFood_price()+"");

        Bitmap bitmap1 = BitmapFactory.decodeByteArray(food1.getFood_image(), 0, food1.getFood_image().length);
        imageView.setImageBitmap(bitmap1);

        //event
        btnChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (fragment.getClass().toString()){
                    case "class com.example.duan1_nhom3.fragment.FragmentFastFood":
                        ((FragmentFastFood)fragment).intentImage();
                        break;
                    case "class com.example.duan1_nhom3.fragment.FragmentWater":
                        ((FragmentWater)fragment).intentImage();
                        break;
                    case "class com.example.duan1_nhom3.fragment.FragmentCombo":
                        ((FragmentCombo)fragment).intentImage();
                        break;
                }
            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                food1.setFood_name(edtNameItem.getText().toString());
                food1.setFood_price(Double.parseDouble(edtPriceItem.getText().toString()));
                food1.setFood_image(imageToByte(imageView));

                foodDAO.updateFood(food1);
                dialog.dismiss();
                notifyDataSetChanged();
            }
        });

        btnCancelItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}
