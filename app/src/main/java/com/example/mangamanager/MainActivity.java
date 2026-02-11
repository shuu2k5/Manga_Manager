package com.example.mangamanager;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText edtMT, edtten, edttacgia, edttap, edtgia, edttaiban;
    Button btninsert, btndelete, btnshow, btnupdate;
    ListView lv;
    ArrayList<EditText> checklist = new ArrayList<>();
    public ArrayList<String> mylist;
    public ArrayAdapter myadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Anh xa EDT
        edtMT = findViewById(R.id.edtMT);
        edtten = findViewById(R.id.edtName);
        edttacgia = findViewById(R.id.edtTG);
        edttap = findViewById(R.id.edtChap);
        edttaiban = findViewById(R.id.edtTB);
        edtgia = findViewById(R.id.edtGia);
        // Anh xa BTN
        btninsert = findViewById(R.id.btninsert);
        btndelete = findViewById(R.id.btndelete);
        btnupdate = findViewById(R.id.btnupdate);
        btnshow = findViewById(R.id.btnshow);

        Database db = new Database(this);

        // kiem tra loi bo trong
        checklist.add(edtMT);
        checklist.add(edtten);
        checklist.add(edttacgia);
        checklist.add(edttap);
        checklist.add(edttaiban);
        checklist.add(edtgia);

        lv = findViewById(R.id.lv);
        mylist = new ArrayList<>();
        myadapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,mylist);
        lv.setAdapter(myadapter);

        btninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (EditText et : checklist) {
                    if (et.getText().toString().trim().isEmpty()) {
                        et.setError("Ô này không được bỏ trống.");
                        et.requestFocus();
                        return;
                    }
                }

                try {
                    String Matruyen = edtMT.getText().toString();
                    String Tentruyen = edtten.getText().toString();
                    String Tentacgia = edttacgia.getText().toString();
                    int sotap = Integer.parseInt(edttap.getText().toString());
                    int Taiban = Integer.parseInt(edttaiban.getText().toString());
                    int Gia = Integer.parseInt(edtgia.getText().toString());

                    long result = db.InsertData(Matruyen, Tentruyen, Tentacgia, sotap ,Taiban, Gia);

                    if (result == -1) {
                        edtMT.setError("Mã này đã tồn tại! Vui lòng nhập mã khác.");
                        edtMT.requestFocus();
                    } else {
                        // TRƯỜNG HỢP THÀNH CÔNG
                        Toast.makeText(MainActivity.this, "Thêm truyện thành công!", Toast.LENGTH_SHORT).show();

                        // CHỈ xóa trắng các ô khi thực sự thành công
                        //for (EditText et : checklist) {
                        //   et.setText("");
                        //}
                        //edtMT.requestFocus();
                    }

                } catch (NumberFormatException e) {
                    edttap.setError("Vui lòng nhập số.");
                    edttap.requestFocus();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mylist.clear();
                mylist.addAll(db.ShowAllManga());
                if(mylist.isEmpty()){
                    Toast.makeText(MainActivity.this, "Chưa có dữ liệu để hiển thị", Toast.LENGTH_SHORT).show();
                }
                myadapter.notifyDataSetChanged();
            }
        });
    }
}