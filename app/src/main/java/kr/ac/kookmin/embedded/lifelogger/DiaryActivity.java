package kr.ac.kookmin.embedded.lifelogger;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DiaryActivity extends AppCompatActivity {

    //Object about Database
    SQLiteDatabase db;
    String dbName = "log.db";
    String tableName = "logListTable";
    int dbMode = Context.MODE_PRIVATE;

    //Object about Layout
    Context mContext;
    EditText what;
    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        //Intent Setting
        Intent intent = getIntent();
        final String local = intent.getExtras().getString("local");
        final String addr = intent.getExtras().getString("address");
        final double lon = intent.getExtras().getDouble("lon");
        final double lat = intent.getExtras().getDouble("lat");

        //layout Setting
        mContext = this.getApplication().getApplicationContext();
        what = (EditText)findViewById(R.id.editText);

        TextView textView1 = (TextView)findViewById(R.id.textView);
        TextView textView2 = (TextView)findViewById(R.id.textView4);
        TextView textView3 = (TextView)findViewById(R.id.textView5);
        TextView textView4 = (TextView)findViewById(R.id.textView9);

        textView1.setText(local);
        textView2.setText(String.valueOf(lon));
        textView3.setText(String.valueOf(lat));
        textView4.setText(addr);

        //Object about Data
        db = openOrCreateDatabase(dbName,dbMode,null);
        createTable();

        //Button Setting
        confirm = (Button)findViewById(R.id.bt_confirm);
        confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "저장되었습니다", Toast.LENGTH_LONG).show();
                String diary = what.getText().toString();

                insertData(local,lon,lat,diary);
                finish();
            }
        });

    }

    //Table 생성
    public void createTable() {
        try {
            String sql = "create table " + tableName + "(id integer primary key autoincrement, name TEXT, lon TEXT, lat TEXT, what TEXT)";
            db.execSQL(sql);
        } catch (android.database.sqlite.SQLiteException e) {
        }
    }

    //Data 추가
    public void insertData(String name,Double lon,Double lat,String what) {
        String sql = "INSERT INTO " + tableName + " VALUES (NULL, '" +name +  "', '" + lon + "', '" + lat + "', '" + what + "');";
        db.execSQL(sql);
    }

}
