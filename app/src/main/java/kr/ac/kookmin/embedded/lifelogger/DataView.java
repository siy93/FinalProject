package kr.ac.kookmin.embedded.lifelogger;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class DataView extends AppCompatActivity {

    //Object about Database
    SQLiteDatabase db;
    String dbName = "log.db";
    String tableName = "logListTable";
    int dbMode = Context.MODE_PRIVATE;

    //Object about layout
    Button back;

    //variable
    String Title;
    String act;
    String what;
    String lon;
    String lat;
    double mlon;
    double mlat;

    //method about GoogleMap
    static final LatLng SEOUL = new LatLng( 37.56, 126.97);
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dataview);

        TextView textView1 = (TextView)findViewById(R.id.textView6);
        TextView textView2 = (TextView)findViewById(R.id.textView7);
        TextView textView3 = (TextView)findViewById(R.id.textView11);



        db = openOrCreateDatabase(dbName,dbMode,null);

        back = (Button)findViewById(R.id.bt_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        String id = intent.getExtras().getString("p_id");
        Log.d("id",id);

        String sql = "select * from " + tableName + " where id = " + id + ";";
        Cursor result = db.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if (result.moveToFirst()) {
            Title = result.getString(5);
            what = result.getString(4);
            lon = result.getString(2);
            lat = result.getString(3);
            act = result.getString(1);
        }
        result.close();

        textView1.setText(Title);
        textView2.setText(what);
        textView3.setText(act);

        mlon = Double.valueOf(lon);
        mlat = Double.valueOf(lat);


        final LatLng ThisLocation = new LatLng(mlat, mlon);


        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        Marker my = map.addMarker(new MarkerOptions().position(ThisLocation).title("ThisLocation"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(ThisLocation, 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

    }


}
