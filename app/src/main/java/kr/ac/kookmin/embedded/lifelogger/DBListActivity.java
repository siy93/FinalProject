package kr.ac.kookmin.embedded.lifelogger;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class DBListActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener{


    //Object about Database
    SQLiteDatabase db;
    String dbName = "log.db";
    String tableName = "logListTable";
    int dbMode = Context.MODE_PRIVATE;


    ArrayAdapter<String> baseAdapter;
    ArrayList<String> arrList = null;
    ArrayList<String> arr_id_list = null;
    ArrayList<String> arr_lon_list = null;
    ArrayList<String> arr_lat_list = null;

    Button mBtRead;
    Button mBtReset;



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dblist);

        // // Database 생성 및 열기
        db = openOrCreateDatabase(dbName,dbMode,null);

        mBtRead = (Button) findViewById(R.id.bt_read);
        mBtReset = (Button)findViewById(R.id.bt_reset);

        mBtRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrList.clear();
                selectAll();
                baseAdapter.notifyDataSetChanged();
            }
        });


        mBtReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                deleteAll();
            }
        });


        // Create listview
        arrList = new ArrayList<String>();
        arr_id_list = new ArrayList<String>();
        arr_lon_list= new ArrayList<String>();
        arr_lat_list= new ArrayList<String>();
        baseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrList);
        ListView mList = (ListView) findViewById(R.id.list_view);
        mList.setAdapter(baseAdapter);

        mList.setOnItemLongClickListener(this);

    }

    //모든 데이터 삭제
    public void deleteAll(){
        db.delete(tableName,null,null);

    }
    // 모든 Data 읽기
    public void selectAll() {
        String sql = "select * from " + tableName + ";";
        Cursor results = db.rawQuery(sql, null);
        results.moveToFirst();

        while (!results.isAfterLast()) {
            String id = results.getString(0);
            String name = results.getString(1);
            String lon = results.getString(2);
            String lat = results.getString(3);
            Log.d("lab_sqlite", "index= " + id + " name=" + name);

            arr_id_list.add(id);
            arrList.add(name);
            arr_lon_list.add(lon);
            arr_lat_list.add(lat);
            results.moveToNext();
        }
        results.close();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        final Integer selectedPos = i;
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
        alertDlg.setTitle(R.string.alert_title_question);

        alertDlg.setPositiveButton( R.string.button_yes, new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String position = arr_id_list.get(selectedPos);
                final String sql = "delete from "+ tableName +" where id = "+ position;
                dialog.dismiss();

                db.execSQL(sql);

                arrList.clear();
                selectAll();
                baseAdapter.notifyDataSetChanged();
            }
        });

        alertDlg.setNegativeButton( R.string.button_no, new DialogInterface.OnClickListener(){

            @Override
            public void onClick( DialogInterface dialog, int which ) {
                String position = arr_id_list.get(selectedPos);
                dialog.dismiss();

                Intent intent = new Intent(DBListActivity.this, DataView.class);
                intent.putExtra("p_id", position);

                startActivity(intent);
            }
        });

        alertDlg.setMessage(R.string.alert_msg_delete);
        alertDlg.show();
        return false;

    }


}
