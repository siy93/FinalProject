package kr.ac.kookmin.embedded.lifelogger;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.*;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class GoogleMapActivity extends AppCompatActivity {
    //Object about Googlemap
    private GoogleMap map;
    static final LatLng SEOUL = new LatLng( 37.56, 126.97);


    //Object about layout
    EditText mEtinform;
    Button mBtsave;
    Context mContext;

    //other
    Intent diary;
    double lon;
    double lat;
    private ProgressBar mRegistrationProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_googlemap);
        mContext = this.getApplication().getApplicationContext();
        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);

        diary= new Intent(this,DiaryActivity.class);

        //map관련
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        map = mapFragment.getMap();

        //현재 위치로 가는 버튼 표시
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 10));//초기 위치...수정필
        // map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                mRegistrationProgressBar.setVisibility(ProgressBar.GONE);

                String msg = "현재 위치 \n위도: "+location.getLongitude()+" -- 경도: "+location.getLatitude();
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                drawMarker(location);
                lon = location.getLongitude();
                lat = location.getLatitude();
            }
        };

        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(getApplicationContext(), locationResult);



        mEtinform = (EditText) findViewById(R.id.et_inform);
        mBtsave = (Button) findViewById(R.id.bt_save);


        mBtsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(mContext, "데이터를 추가합니다", Toast.LENGTH_LONG).show();
                String name = mEtinform.getText().toString();
                String addr = getLocation(lat,lon);


                mEtinform.clearFocus();

                InputMethodManager imm =
                        (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);


                diary.putExtra("local", name);
                diary.putExtra("lon", lon);
                diary.putExtra("lat", lat);
                diary.putExtra("address",addr);
                startActivity(diary);
            }
        });


    }

    //map 관련
    private void drawMarker(Location location) {

        //기존 마커 지우기
        map.clear();
        LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

        //currentPosition 위치로 카메라 중심을 옮기고 화면 줌을 조정한다. 줌범위는 2~21, 숫자클수록 확대
        map.moveCamera(CameraUpdateFactory.newLatLngZoom( currentPosition, 17));
        map.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);

        //마커 추가
        map.addMarker(new MarkerOptions()
                .position(currentPosition)
                .snippet(getLocation(location.getLatitude(),location.getLongitude()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("현재위치"));
    }

    public String getLocation(double lat,double lng){
        String str = null;
        Geocoder geocoder = new Geocoder(this, Locale.KOREA);

        List<Address> address;
        try {
            if (geocoder != null) {
                address = geocoder.getFromLocation(lat, lng, 1);
                if (address != null && address.size() > 0) {
                    str = address.get(0).getAddressLine(0).toString();
                }
            }
        } catch (IOException e) {
            Log.e("MainActivity", "주소를 찾지 못하였습니다.");
            e.printStackTrace();
        }
        return str;
    }
}
