package com.example.mymess;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymess.Login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MarkInterest extends AppCompatActivity {

    private static final String TAG = "MarkInterest";

    TextView dish, name, date;
    RadioButton rb1, rb2;
    RadioGroup radioGroup;

    String userId="",res_id;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_interest);

        dish = findViewById(R.id.tv_serving_dish);
        name = findViewById(R.id.tv_mess_name);
        date = findViewById(R.id.tv_date);

        radioGroup=findViewById(R.id.rg);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);

        Intent intent = getIntent();
        dish.setText(intent.getStringExtra("dish"));
        name.setText(intent.getStringExtra("name"));
        res_id=intent.getStringExtra("res_id");


        setupFirebaseAuth();

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        final String todayAsString = dateFormat.format(today);

        date.setText(todayAsString);

        findViewById(R.id.bt_mark_interest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int r_id=radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton=(RadioButton) findViewById(r_id);

               // String res_id="-M-xlv43EDQCoV1m73bo";


                    myRef.child("interest").child(res_id).child(todayAsString).child(userId).setValue(radioButton.getTag());
                    myRef.child("user_data").child("interest").child(res_id).child(todayAsString).child(userId).setValue(radioButton.getTag());

                    Toast.makeText(MarkInterest.this, "Your Response is successfully submitted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MarkInterest.this,HomeActivity.class));

                finish();




            }
        });


    }





































    //-------------------------------firebase-----stuff-------------------------------------------------------------------------

    private void setupFirebaseAuth()
    {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase= FirebaseDatabase.getInstance();
        myRef=mFirebaseDatabase.getReference();


        Log.d(TAG, "setupFirebaseAuth: checking firbase authentication");

        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user= firebaseAuth.getCurrentUser();

                checkCurrentUser(user);

                if (user!=null)
                {
                    userId=user.getUid();
                    Log.d(TAG, "onAuthStateChanged: signed in"+user.getUid());
                }
                else
                {
                    Log.d(TAG, "onAuthStateChanged: signed out");
                }


            }
        };




    }

    private void checkCurrentUser(FirebaseUser user)
    {
        if (user==null)
        {
            Intent intent=new Intent(MarkInterest.this, LoginActivity.class);
            startActivity(intent);finish();
            Log.d(TAG, "checkCurrentUser: no user logged in");
        }

        else
            Log.d(TAG, "checkCurrentUser: user id:"+user.getUid()+" logged in");
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        mAuth.addAuthStateListener(mAuthListener);
    }


    private void updateUI(FirebaseUser currentUser) {
    }


}
