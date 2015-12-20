package kr.ac.kookmin.embedded.lifelogger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SelectAct extends AppCompatActivity {

    Button bt1;
    Button bt2;
    Button bt3;
    Button bt4;
    Button bt5;

    String act1 = "공부";
    String act2 = "운동";
    String act3 = "여가";
    String act4 = "유흥,오락";
    String act5 = "기타";


    Context mContext;
    Intent recordintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectact);

        bt1 = (Button) findViewById(R.id.bt1);
        bt2 = (Button) findViewById(R.id.bt2);
        bt3 = (Button) findViewById(R.id.bt3);
        bt4 = (Button) findViewById(R.id.bt4);
        bt5 = (Button) findViewById(R.id.bt5);

        recordintent = new Intent(this,RecordShow.class);

        mContext = this.getApplication().getApplicationContext();


        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "공부 한 지역을 조회합니다", Toast.LENGTH_LONG).show();
                recordintent.putExtra("act", act1);
                startActivity(recordintent);
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "운동 한 지역을 조회합니다", Toast.LENGTH_LONG).show();
                recordintent.putExtra("act", act2);
                startActivity(recordintent);
            }
        });

        bt3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "여가 한 지역을 조회합니다", Toast.LENGTH_LONG).show();
                recordintent.putExtra("act",act3);
                startActivity(recordintent);
            }
        });

        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "유흥,오락을 한 지역을 조회합니다", Toast.LENGTH_LONG).show();
                recordintent.putExtra("act", act4);
                startActivity(recordintent);
            }
        });

        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "기타 활동 지역을 조회합니다", Toast.LENGTH_LONG).show();
                recordintent.putExtra("act", act5);
                startActivity(recordintent);
            }
        });


    }
}
