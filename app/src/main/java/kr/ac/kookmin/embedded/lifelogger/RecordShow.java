package kr.ac.kookmin.embedded.lifelogger;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by 송인엽 on 2015-12-20.
 */
public class RecordShow  extends AppCompatActivity {

    ArrayAdapter<String> baseAdapter;
    ArrayList<String> actList;


    //Object about Database
    SQLiteDatabase db;
    String dbName = "log.db";
    String tableName = "logListTable";
    int dbMode = Context.MODE_PRIVATE;

    String act;
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
        setContentView(R.layout.activity_recordshow);

        // // Database 열기
        db = openOrCreateDatabase(dbName, dbMode, null);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        Intent intent = getIntent();
        String whatact = intent.getExtras().getString("act");
        selectDataAll(whatact);
    }

    // 모든 Data 읽기
    public void selectDataAll(String what) {
        String sql = "select * from " + tableName + ";";
        Cursor result = db.rawQuery(sql, null);
        result.moveToFirst();

        while (!result.isAfterLast()) {
            String act = result.getString(5);

            if(act.equals(what)) {
                lon = result.getString(2);
                lat = result.getString(3);

                mlon = Double.valueOf(lon);
                mlat = Double.valueOf(lat);

                final LatLng ThisLocation = new LatLng(mlat, mlon);
                Marker my = map.addMarker(new MarkerOptions().position(ThisLocation).title("ThisLocation"));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(ThisLocation, 15));
                map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            }
            result.moveToNext();
        }
        result.close();
    }
}


