package com.example.mark.disasterguard;

import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class TraumaTreatmentActivity extends AppCompatActivity {

    /* 自建的資料庫類別 */
    private MyDB db=null;

    /* 資料表欄位 */
    //private final static String	_ID	= "_id";
    //private final static String	NAME = "name";
    //private final static String	PRICE = "price";

    Button btnAppend,btnEdit,btnDelete,btnClear;
    EditText edtName,edtPrice;
    ListView listview01;
    Cursor cursor;
    long myid;  // 儲存 _id 的值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trauma_treatment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 取 得元件
        edtName=(EditText)findViewById(R.id.edtName);
        //edtPrice=(EditText)findViewById(R.id.edtPrice);
        listview01=(ListView)findViewById(R.id.ListView01);
        btnAppend=(Button)findViewById(R.id.btnAppend);
        btnEdit=(Button)findViewById(R.id.btnEdit);
        btnDelete=(Button)findViewById(R.id.btnDelete);
        btnClear=(Button)findViewById(R.id.btnClear);
        // 設定偵聽
        btnAppend.setOnClickListener(myListener);
        btnEdit.setOnClickListener(myListener);
        btnDelete.setOnClickListener(myListener);
        btnClear.setOnClickListener(myListener);
        listview01.setOnItemClickListener(listview01Listener);

        db =new MyDB(this);       // 建立 MyDB 物件
        db.open();

        cursor=db.getAll();// 載入全部資料
        UpdateAdapter(cursor);  // 載入資料表至 ListView 中
    }

    private ListView.OnItemClickListener listview01Listener=
            new ListView.OnItemClickListener(){
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    ShowData(id);
                    cursor.moveToPosition(position);
                }
            };

    private void ShowData(long id){ //顯示單筆資料
        Cursor c=db.get(id);
        myid=id;  // 取得  _id 欄位
        edtName.setText(c.getString(1)); // name 欄位
        //edtPrice.setText("" + c.getInt(2)); // price 欄位
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        db.close(); // 關閉資料庫
    }

    private Button.OnClickListener myListener=new Button.OnClickListener(){
        public void onClick(View v){
            try{
                switch (v.getId()){
                    case R.id.btnAppend:{ // 新增
                        //int price=Integer.parseInt( edtPrice.getText().toString());
                        String name=edtName.getText().toString();
                        if ( db.append(name)>0){
                            cursor=db.getAll();// 載入全部資料
                            UpdateAdapter(cursor);  // 載入資料表至 ListView 中
                            ClearEdit();
                        }
                        break;
                    }
                    case R.id.btnEdit: {  //修改
                        // int price=Integer.parseInt( edtPrice.getText().toString());
                        String name=edtName.getText().toString();
                        if (db.update(myid,name)){
                            cursor=db.getAll();// 載入全部資料
                            UpdateAdapter(cursor);  // 載入資料表至 ListView 中
                        }
                        break;
                    }
                    case R.id.btnDelete: { //刪除
                        if (cursor != null && cursor.getCount() >= 0){
                            AlertDialog.Builder builder=new AlertDialog.Builder(TraumaTreatmentActivity.this);
                            builder.setTitle("確定刪除");
                            builder.setMessage("確定要刪除" + edtName.getText() + "這筆資料?");
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int i) {
                                }
                            });
                            builder.setPositiveButton("確定",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int i) {
                                    if (db.delete(myid)){
                                        cursor=db.getAll();// 載入全部資料
                                        UpdateAdapter(cursor); // 載入資料表至 ListView 中
                                        ClearEdit();
                                    }
                                }
                            });
                            builder.show();
                        }
                        break;
                    }
                    case R.id.btnClear: { //清除
                        ClearEdit();
                        break;
                    }
                }
            }catch (Exception err){
                Toast.makeText(getApplicationContext(), "資料不正確!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void ClearEdit(){
        edtName.setText("");

    }

    public void UpdateAdapter(Cursor cursor){
        if (cursor != null && cursor.getCount() >= 0){
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1, // 包含兩個資料項
                    cursor, // 資料庫的 Cursors 物件
                    new String[] { "name"}, // name、price 欄位
                    new int[] { android.R.id.text1},
                    0);
            listview01.setAdapter(adapter); // 將adapter增加到listview01中
        }
    }
}
