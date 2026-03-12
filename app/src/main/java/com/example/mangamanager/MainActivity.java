package com.example.mangamanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText edtten, edttacgia, edttap, edtgia, edttaiban;
    Button btninsert, btnsearch, btnshow, btnupdate;
    ListView lv;
    ArrayList<EditText> checklist = new ArrayList<>();
    public ArrayList<String> mylist;
    public ArrayAdapter myadapter;
    String Madangchon = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, v.getPaddingBottom());
            return insets;
        });
        // Anh xa EDT
        edtten = findViewById(R.id.edtName);
        edttacgia = findViewById(R.id.edtTG);
        edttap = findViewById(R.id.edtChap);
        edttaiban = findViewById(R.id.edtTB);
        edtgia = findViewById(R.id.edtGia);
        // Anh xa BTN
        btninsert = findViewById(R.id.btninsert);
        btnsearch = findViewById(R.id.btnsearch);
        btnupdate = findViewById(R.id.btnupdate);
        btnshow = findViewById(R.id.btnshow);

        Database db = new Database(this);

        // kiem tra loi bo trong
        checklist.add(edtten);
        checklist.add(edttacgia);
        checklist.add(edttap);
        //checklist.add(edttaiban);
        checklist.add(edtgia);

        lv = findViewById(R.id.lv);
        mylist = new ArrayList<>();
        myadapter = new ArrayAdapter<>(MainActivity.this, R.layout.textlist,mylist);
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
                    String Tentruyen = edtten.getText().toString().trim();
                    String Tentacgia = edttacgia.getText().toString().trim();
                    int sotap = Integer.parseInt(edttap.getText().toString());
                    //int Taiban = Integer.parseInt(edttaiban.getText().toString()).trim();
                    int Gia = Integer.parseInt(edtgia.getText().toString());

                    String Matruyen = CreateID(Tentruyen, sotap, Gia);

                    long result = db.InsertData(Matruyen, Tentruyen, Tentacgia, sotap , Gia); // ,Taiban

                    Toast.makeText(MainActivity.this, "Thêm truyện thành công!", Toast.LENGTH_SHORT).show();
                    if (lv.getVisibility() == View.VISIBLE) {
                        btnshow.performClick();
                        btnshow.performClick();
                    }
                    for (EditText et : checklist) {
                        et.setText("");
                    }
                    edtten.requestFocus();

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
                if (lv.getVisibility() == View.GONE) {
                    lv.setVisibility(View.VISIBLE);
                    btnshow.setText("Ẩn danh sách");
                    mylist.clear();
                    mylist.addAll(db.ShowAllManga());
                    if (mylist.isEmpty()) {
                        btnshow.setText("Danh sách");
                        Toast.makeText(MainActivity.this, "Chưa có dữ liệu để hiển thị", Toast.LENGTH_SHORT).show();
                    }
                    myadapter.notifyDataSetChanged();
                }
                else {
                    lv.setVisibility(View.GONE);
                    btnshow.setText("Danh sách");
                }
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //thiet lap muc tieu can xoa
                String slItem = mylist.get(position);
                final String Id = slItem.split("\n")[0];
                final String deleteID = Id.replace("Mã truyện: ", "").trim();

                // thong bao se xoa
                AlertDialog.Builder notice = new AlertDialog.Builder(MainActivity.this);
                notice.setTitle("Xác nhận xóa!");
                notice.setMessage("Bạn có chắc sẽ xóa truyện " + deleteID + " không?");

                // xu ly nut xoa
                notice.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(db.DeleteData(deleteID))
                        {
                            Toast.makeText(MainActivity.this, "Đã xóa thành công!", Toast.LENGTH_SHORT).show();
                            btnshow.performClick();
                            btnshow.performClick();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Lỗi. Xóa không thành công!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                notice.setNegativeButton("Hủy", null);
                notice.show();
                return true;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectManga = mylist.get(position);
                String[] lines = selectManga.split("\n");
                Madangchon = lines[0].replace("Mã truyện: ", "").trim();
                String selectten = lines[1].replace("Tên truyện: ", "").trim();
                String selecttacgia = lines[2].replace("Tên tác giả: ", "").trim();
                String selecttap = lines[3].replace("Tập số: ", "").trim();
                String selectgia = lines[4].replace("Giá: ", "")
                        .replace(" VNĐ.", "")
                        .replace(",", "").trim();
                edtten.setText(selectten);
                edttacgia.setText(selecttacgia);
                edttap.setText(selecttap);
                edtgia.setText(selectgia);
            }
        });

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Madangchon.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Vui lòng chọn truyện cần cập nhật.", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    String newten = edtten.getText().toString();
                    String newtacgia = edttacgia.getText().toString();
                    int newtap = Integer.parseInt(edttap.getText().toString());
                    int newgia = Integer.parseInt(edtgia.getText().toString());
                    String newma = CreateID(newten, newtap, newgia);
                    if (db.UpdateData(Madangchon, newma, newten,newtacgia,newtap,newgia))
                    {
                        Toast.makeText(MainActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        Madangchon = "";
                        if (lv.getVisibility() == View.VISIBLE) {
                            btnshow.performClick();
                            btnshow.performClick();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Lỗi dữ liệu. Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
    }
    // tạo id truyên
    private String CreateID(String ten, int tap, int gia){
        String kytudau = "";
        if (!ten.isEmpty())
        {
            kytudau = ten.substring(0,1).toUpperCase();
        }
        String kytugiua = String.format("%02d", tap);
        String kytucuoi = (gia/1000)+"K";
        return  "#" + kytudau + kytugiua + kytucuoi;
    }
}