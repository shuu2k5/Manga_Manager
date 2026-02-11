package com.example.mangamanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class Database extends SQLiteOpenHelper {
    // co dinh
    public static final String Database_Name ="MangaLibrary.db";
    public static final int Data_Version = 1;

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
                + Tai_ban + " INTEGER, "
                + Gia_tien + " INTEGER )" ;
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        onCreate(db);
    }
    public long InsertData(String edtMT, String edtTen, String edtTG, int edtChap, int edtTB, int edtGia){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Ma_truyen, edtMT);
        values.put(Ten_truyen, edtTen);
        values.put(Tac_gia,edtTG);
        values.put(So_tap, edtChap);
        values.put(Tai_ban ,edtTB);
        values.put(Gia_tien, edtGia);
        return db.insert(Table_Name, null, values);
    }

    public ArrayList<String> ShowAllManga(){
        ArrayList<String> mylist = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ Table_Name,null);
        while (cursor.moveToNext()) {
            int indexMa = cursor.getColumnIndex(Ma_truyen);
            int indexTen = cursor.getColumnIndex(Ten_truyen);
            int indexTacgia = cursor.getColumnIndex(Tac_gia);
            int indexTapso = cursor.getColumnIndex(So_tap);
            int indexTaiban = cursor.getColumnIndex(Tai_ban);
            int indexGiatien = cursor.getColumnIndex(Gia_tien);
            if (indexMa != -1 && indexTen != -1) {
                String ma = cursor.getString(indexMa);
                String ten = cursor.getString(indexTen);
                String tacgia = cursor.getString(indexTacgia);
                String tapso = cursor.getString(indexTapso);
                String taiban = cursor.getString(indexTaiban);
                String gia = cursor.getString(indexGiatien);
                String showData = "Mã truyện: #" + ma +
                        "\nTên truyện: " + ten +
                        "\nTên tác giả: " + tacgia +
                        "\nTập số: " + tapso +
                        "\nLần tái bản: " + taiban +
                        "\nGiá: " + gia + "K VNĐ.";
                mylist.add(showData);
            }
        }
        cursor.close();
        return mylist;
    }
}
