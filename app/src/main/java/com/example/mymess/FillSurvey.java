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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FillSurvey extends AppCompatActivity {

    private static final String TAG = "FillSurvey";
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    RadioGroup radioGroup;
    RadioButton rb1,rb2,rb3,rb4;
    TextView date,mess_name;

    String userId="";

    Map<String, String> objectMap;
    private String res_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_survey);

        mess_name=findViewById(R.id.tv_mess_name);
        radioGroup=findViewById(R.id.rg);
        rb1=findViewById(R.id.rb1);
        rb2=findViewById(R.id.rb2);
        rb3=findViewById(R.id.rb3);
        rb4=findViewById(R.id.rb4);

        date=findViewById(R.id.tv_date);
        setupFirebaseAuth();

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

        final String tomorrowAsString = dateFormat.format(tomorrow);
        String surveyID=myRef.push().getKey();

        final Intent intent=getIntent();
        final String name=intent.getStringExtra("name");
        res_id=intent.getStringExtra("res_id");
        mess_name.setText(intent.getStringExtra("name"));
        //String res_id="-M-xlv43EDQCoV1m73bo";


        try{


            Query query = myRef.child("survey").child(res_id).child(tomorrowAsString).child("map");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    objectMap = (HashMap<String, String>) dataSnapshot.getValue();

                        if (objectMap!=null)
                        {
                            rb1.setText(objectMap.get("op1"));
                            rb2.setText(objectMap.get("op2"));
                            rb3.setText(objectMap.get("op3"));
                            rb4.setText(objectMap.get("op4"));
                        }
                        else {
                            Toast.makeText(FillSurvey.this, "Survey for this date was not Created", Toast.LENGTH_SHORT).show();
                            FillSurvey.super.onBackPressed();
                        }




                    date.setText(tomorrowAsString);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            findViewById(R.id.bt_add_survey).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int r_id=radioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton=(RadioButton) findViewById(r_id);

                    //String res_id="-M-xlv43EDQCoV1m73bo";

                    if (objectMap!=null)
                    {
                        myRef.child("survey_res").child(res_id).child(tomorrowAsString).child(userId).setValue(radioButton.getText());
                        myRef.child("user_data").child("survey_res").child(res_id).child(tomorrowAsString).child(userId).setValue(radioButton.getText());

                        Toast.makeText(FillSurvey.this, "Your Response is successfully submitted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(FillSurvey.this,HomeActivity.class));
                        finish();
                    }




                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Survey for this date was not Created", Toast.LENGTH_SHORT).show();
            FillSurvey.super.onBackPressed();
        }

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
            Intent intent=new Intent(FillSurvey.this, LoginActivity.class);
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
