package com.example.duan1_nhom3.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_nhom3.R;

import java.io.ByteArrayOutputStream;

public class DbHelper extends SQLiteOpenHelper{

    private static DbHelper dbInstance;
    private Context context;
    private SQLiteStatement statement;
    private Bitmap bitmap;

    public static synchronized DbHelper getDbInstance(Context ctx){
        if (dbInstance == null) dbInstance = new DbHelper(ctx);
        return dbInstance;
    }

    public DbHelper(@Nullable Context context) {
        super(context, "MiniBar", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng FOOD
        db.execSQL("CREATE TABLE IF NOT EXISTS FOOD(" +
                "FOOD_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "FOOD_NAME TEXT," +
                "FOOD_PRICE DOUBLE," +
                "FOOD_TYPE INTEGER," +
                "FOOD_STATUS INTEGER," +
                "FOOD_IMAGE BLOB)");

        // Tạo bảng BOARD
        db.execSQL("CREATE TABLE IF NOT EXISTS BOARD(" +
                "BOARD_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "BOARD_NUMBER INTEGER," +
                "BOARD_STATUS INTEGER)");

        // Tạo bảng BILL
        db.execSQL("CREATE TABLE IF NOT EXISTS BILL(" +
                "BILL_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "BILL_DATE TEXT," +
                "BILL_PRICE DOUBLE,"+
                "BOARD_ID INTEGER REFERENCES BOARD(BOARD_ID))");

        // Tạo bảng FOODBOARD
        db.execSQL("CREATE TABLE IF NOT EXISTS FOODBOARD(" +
                "FOODBOARD_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "FOOD_ID INTEGER REFERENCES FOOD(FOOD_ID)," +
                "BOARD_ID INTEGER REFERENCES BOARD(BOARD_ID)," +
                "FOODBOARD_STATUS INTEGER)");

        // Tạo bảng STAFF
        db.execSQL("CREATE TABLE IF NOT EXISTS STAFF(" +
                "STAFF_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "STAFF_NAME TEXT," +
                "STAFF_SDT TEXT," +
                "STAFF_BIRTH TEXT," +
                "STAFF_MONEY DOUBLE," +
                "STAFF_CALENDAR TEXT," +
                "STAFF_TIME INTEGER," +
                "STAFF_PASSWORD TEXT," +
                "STAFF_ROLE TEXT," +
                "STAFF_IMAGE BLOB)");

        // Inser dữ liệu Bảng Board
        db.execSQL("INSERT INTO BOARD (BOARD_NUMBER, BOARD_STATUS) VALUES (1, 1), (2, 1), (3, 1), (4,1), (5, 1), (6, 0), (7, 0), (8, 0), (9, 0), (10, 0), (11, 0), (12, 0), (13, 0), (14, 0), (15, 0), (16, 0)");

        // Insert dữ liệu bảng FOOD
        insertDataFood("Hamburger",40000, 0,1, R.drawable.bread, db);   // 1
        insertDataFood("Pizza",50000, 0, 1, R.drawable.pizza, db);       // 2
        insertDataFood("Mì cay",45000, 0, 1, R.drawable.micay, db);      // 3
        insertDataFood("Gà rán",45000, 0, 1, R.drawable.garan, db);      // 4
        insertDataFood("Hot dog",30000, 0, 1, R.drawable.hotdog, db);        // 5
        insertDataFood("Khoai tây chiên",30000,0,1, R.drawable.khoaitaychien, db);     // 6
        insertDataFood("Tokbokki",40000, 0, 1, R.drawable.tokbokki, db);     // 7
        insertDataFood("Cafe đen",20000, 1, 1, R.drawable.cafeden, db);      // 8
        insertDataFood("Cafe sữa",25000, 1, 1, R.drawable.cafesua, db);      // 9
        insertDataFood("Capuchino",35000, 1, 1, R.drawable.capuchino, db);       // 10
        insertDataFood("Cocacola",20000, 1, 1, R.drawable.cocacola, db);     // 11
        insertDataFood("Matcha",40000, 1, 1, R.drawable.matcha, db);         // 12
        insertDataFood("Nước cam",35000, 1, 1, R.drawable.nuoccam, db);      // 13
        insertDataFood("Trà đào",35000, 1, 1, R.drawable.tradao, db);        // 14
        insertDataFood("Coca, khoai chiên, gà rán",80000, 2, 1, R.drawable.combococakhoaiga, db);        // 15
        insertDataFood("Hamburger, khoai chiên, nước cam",90000, 2, 1, R.drawable.combokhoaihamburgernuoccam, db);       // 16
        insertDataFood("Khoai chiên, hot dog, coca",65000, 2, 1, R.drawable.combokhoaihotdogcoca, db);       // 17
        insertDataFood("Mi cay, trà đào",70000, 2, 1, R.drawable.combomicaytradao, db);      // 18

        // Inser dữ liệu bảng FoodBoard
        db.execSQL("INSERT INTO FOODBOARD (FOOD_ID, BOARD_ID, FOODBOARD_STATUS) VALUES (4, 1, 1), (13, 1, 1), (1, 2, 1), (11, 2, 1), (11, 2, 1), (18, 3, 1), (18, 3, 1), (18, 3, 1), (12, 4, 1), (13, 4, 1), (13, 4, 1), (13, 4, 1), (9, 4, 1), (16, 5, 1), (16, 5, 1), (18, 5, 1), (2, 5, 1), (8, 6, 2), (8, 6, 2), (9, 7, 2), " +
                "(12, 8, 2), (13, 8, 2), (9, 8, 2), (2, 9, 2), (4, 9, 2), (17, 10, 2), (10, 10, 2), (5, 11, 2), (6, 11, 2), (7, 11, 2), (8, 11, 2), (13, 12, 2), (14, 12, 2), (15, 12, 2), (1, 13, 2), (10, 13, 2), " +
                "(6, 14, 2), (12, 14, 2), (8, 15, 2), (9, 16, 2)");

        // Insert dữ liệu Bill
        db.execSQL("INSERT INTO BILL (BILL_DATE, BILL_PRICE, BOARD_ID) VALUES " +
                "('2020/05/23 08:30', 40000, 6), ('2020/07/15 09:00', 25000, 7), ('2020/08/24 14:00', 100000, 8), ('2020/09/30 12:23', 95000, 9), ('2020/11/01 15:45',100000, 10), ('2020/11/02 20:45', 120000, 11), ('2020/11/02 21:23', 150000, 12), " +
                "('2021/11/13 09:00', 75000, 13), ('2021/11/13 09:30', 70000, 14), ('2021/12/13 10:00', 20000, 15), ('2021/12/13 11:30', 25000, 16)");

        // Inser dữ liệu bảng Staff
        insertDataStaff("Nguyễn Hoàng Xuân", "0965256589", "06/05/1992", 40000, "6h - 22h", 0, "123", "Quản lý", R.drawable.manager, db);
        insertDataStaff("Trần Minh Hoàng", "0965256589", "05/07/1993", 35000, "6h - 22h", 0, "1", "Đầu bếp", R.drawable.chef, db);
        insertDataStaff("Nguyễn Phi Long", "0965256589", "08/08/1995", 35000, "6h - 22h", 0, "1", "Pha chế", R.drawable.phache, db);
        insertDataStaff("Phan Ngọc", "0965256589", "09/05/1999", 30000, "6h - 22h", 0, "1", "Phục vụ", R.drawable.phucvu, db);
    }

    public void insertDataFood(String foodName, double foodPrice, int foodType, int foodStatus, int foodImage, SQLiteDatabase db) {
        statement = db.compileStatement("INSERT INTO FOOD (FOOD_NAME, FOOD_PRICE, FOOD_TYPE, FOOD_STATUS, FOOD_IMAGE) " +
                "VALUES ('"+foodName+"', "+foodPrice+", "+foodType+", "+foodStatus+", ?)");

        //truyền ảnh vào database
        bitmap = BitmapFactory.decodeResource(context.getResources(), foodImage);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byte[] data = stream.toByteArray();

        statement.bindBlob(1, data);
        statement.execute();
    }

    public void insertDataStaff(String staffName, String staffSdt, String staffBirth, double staffMoney, String staffCalendar, int staffTime, String staffPassword, String staffRole, int staffImage, SQLiteDatabase db) {
        statement = db.compileStatement("INSERT INTO STAFF (STAFF_NAME, STAFF_SDT, STAFF_BIRTH, STAFF_MONEY, STAFF_CALENDAR, STAFF_TIME, STAFF_PASSWORD, STAFF_ROLE, STAFF_IMAGE) VALUES " +
                "('"+staffName+"', '"+staffSdt+"', '"+staffBirth+"', "+staffMoney+", '"+staffCalendar+"', "+staffTime+", '"+staffPassword+"', '"+staffRole+"', ?)");

        //Truyền ảnh vào database
        bitmap = BitmapFactory.decodeResource(context.getResources(), staffImage);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byte[] data = stream.toByteArray();

        statement.bindBlob(1, data);
        statement.execute();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion){
            db.execSQL("DROP TABLE IF EXISTS FOOD");
            db.execSQL("DROP TABLE IF EXISTS BOARD");
            db.execSQL("DROP TABLE IF EXISTS BILL");
            db.execSQL("DROP TABLE IF EXISTS STAFF");
            db.execSQL("DROP TABLE IF EXISTS FOODBOARD");
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
}
