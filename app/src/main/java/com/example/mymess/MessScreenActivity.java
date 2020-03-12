package com.example.mymess;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MessScreenActivity extends AppCompatActivity {
    private static final String TAG = "MessScreenActivity";
    TextView mess_name,mess_type,rating,timing,price,dish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_screen);

        mess_name=findViewById(R.id.tv_mess_name);
        mess_type=findViewById(R.id.tv_mess_type);
        rating=findViewById(R.id.tv_rating);
        timing=findViewById(R.id.tv_timings);
        price=findViewById(R.id.tv_price);
        dish=findViewById(R.id.tv_serving_dish);

        final Intent intent=getIntent();
        final String name=intent.getStringExtra("name");
        mess_name.setText(intent.getStringExtra("name"));
        mess_type.setText(intent.getStringExtra("type"));
        timing.setText(intent.getStringExtra("timing")+" \n Timing");
        price.setText("Rs."+intent.getStringExtra("price")+"\n Avg. Price");
        rating.setText(intent.getStringExtra("rating")+" \n Rating");
       // mess_name.setText(intent.getStringExtra("image"));

        final String ser_dish=dish.getText().toString();

        findViewById(R.id.rel_vote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessScreenActivity.this,FillSurvey.class));
            }
        });

        findViewById(R.id.rel_rate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(MessScreenActivity.this,Rate.class);
                intent1.putExtra("name",name);
                Log.d(TAG, "onCreate: name="+name);
                startActivity(intent1);
            }
        });

        findViewById(R.id.rel_interest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1=new Intent(MessScreenActivity.this,MarkInterest.class);
                intent1.putExtra("name",name);
                intent1.putExtra("dish",ser_dish);
                Log.d(TAG, "onCreate: name="+name);
                startActivity(intent1);
            }
        });

        findViewById(R.id.rel_full_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1=new Intent(MessScreenActivity.this,FullMenu.class);
                intent1.putExtra("name",name);
                intent1.putExtra("dish",ser_dish);
                Log.d(TAG, "onCreate: name="+name);
                startActivity(intent1);
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessScreenActivity.super.onBackPressed();
            }
        });
    }
}
