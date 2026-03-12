package com.example.mangamanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.material.tabs.TabLayout;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;


public class Database extends SQLiteOpenHelper {
    // co dinh
    public static final String Database_Name ="MangaLibrary.db";
    public static final int Data_Version = 9;

    // cac thuoc tinh database
    public static final String Table_Name = "Manga";
    public static final String Ma_truyen = "Ma_Truyen";
    public static final String Ten_truyen = "Ten_Truyen";
    public static final String So_tap = "So_Tap";
    public static final String Tai_ban = "Tai_Ban";
    public static final String Gia_tien = "Gia_Tien";
    public static final String Tac_gia = "Tac_Gia";

    // ham khoi tao
    public Database (Context context) {
        super(context, Database_Name, null, Data_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + Table_Name +
                " ( " + Ma_truyen + " TEXT PRIMARY KEY , "
                + Ten_truyen + " TEXT, "
                + Tac_gia + " TEXT, "
                + So_tap + " INTEGER, "
                //+ Tai_ban + " INTEGER, "
                + Gia_tien + " INTEGER )" ;
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        onCreate(db);
    }
    public long InsertData(String edtMT, String edtTen, String edtTG, int edtChap, int edtGia){ //, int edtTB
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Ma_truyen, edtMT);
        values.put(Ten_truyen, edtTen);
        values.put(Tac_gia,edtTG);
        values.put(So_tap, edtChap);
        //values.put(Tai_ban ,edtTB);
        values.put(Gia_tien, edtGia);
        return db.insert(Table_Name, null, values);
    }

    public ArrayList<String> ShowAllManga(){
        ArrayList<String> mylist = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ Table_Name + " ORDER BY rowid DESC",null);

        DecimalFormatSymbols symbol = new DecimalFormatSymbols(Locale.US);
        DecimalFormat formatter = new DecimalFormat("###,###,###", symbol);

        while (cursor.moveToNext()) {
            int indexMa = cursor.getColumnIndex(Ma_truyen);
            int indexTen = cursor.getColumnIndex(Ten_truyen);
            int indexTacgia = cursor.getColumnIndex(Tac_gia);
            int indexTapso = cursor.getColumnIndex(So_tap);
            //int indexTaiban = cursor.getColumnIndex(Tai_ban);
            int indexGiatien = cursor.getColumnIndex(Gia_tien);
            if (indexMa != -1 && indexTen != -1) {
                String ma = cursor.getString(indexMa);
                String ten = cursor.getString(indexTen);
                String tacgia = cursor.getString(indexTacgia);
                String tapso = cursor.getString(indexTapso);
                //String taiban = cursor.getString(indexTaiban);
                int gia = Integer.parseInt(cursor.getString(indexGiatien));
                String fixgia = formatter.format(gia).replace(",", ",");;
                String showData = "Mã truyện: " + ma +
                        "\nTên truyện: " + ten +
                        "\nTên tác giả: " + tacgia +
                        "\nTập số: " + tapso +
                        //"\nLần tái bản: " + taiban +
                        "\nGiá: " + fixgia + " VNĐ.";
                mylist.add(showData);
            }
        }
        cursor.close();
        return mylist;
    }

    public Boolean DeleteData(String ma){
        SQLiteDatabase db = this.getWritableDatabase();
        long resual = db.delete(Table_Name, Ma_truyen + " = ?", new String[]{ma});
        return resual > 0;
    }
    public Boolean UpdateData(String edtMT , String newedtMT, String edtTen, String edtTG, int edtChap, int edtGia){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values2 = new ContentValues();
        values2.put(Ma_truyen, newedtMT);
        values2.put(Ten_truyen, edtTen);
        values2.put(Tac_gia, edtTG);
        values2.put(So_tap, edtChap);
        values2.put(Gia_tien, edtGia);
        int resual = db.update(Table_Name, values2, Ma_truyen + " = ?", new String[]{edtMT});
        return resual > 0;
    }
}
