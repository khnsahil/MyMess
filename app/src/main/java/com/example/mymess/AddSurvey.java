package com.example.mymess;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymess.Login.LoginActivity;
import com.example.mymess.models.Survey;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AddSurvey extends AppCompatActivity {

    private static final String TAG = "AddSurvey";

    EditText op1,op2,op3,op4;
    Button add_survey;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_survey);

        op1=findViewById(R.id.et_op1);
        op2=findViewById(R.id.et_op2);
        op3=findViewById(R.id.et_op3);
        op4=findViewById(R.id.et_op4);

        add_survey=findViewById(R.id.bt_add_survey);

        setupFirebaseAuth();

        add_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!op1.getText().equals(null) && !op2.getText().equals(null) && !op3.getText().equals(null) && !op4.getText().equals(null))
                {
                    HashMap<String,String> map=new HashMap<>();

                    map.put("op1",op1.getText().toString());
                    map.put("op2",op2.getText().toString());
                    map.put("op3",op3.getText().toString());
                    map.put("op4",op4.getText().toString());

                    Calendar calendar = Calendar.getInstance();
                    Date today = calendar.getTime();

                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    Date tomorrow = calendar.getTime();

                    DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

                    String tomorrowAsString = dateFormat.format(tomorrow);

                    Survey survey=new Survey(tomorrowAsString,map);

                    String surveyID=myRef.push().getKey();

                    String res_id="-M-xlv43EDQCoV1m73bo";

                    myRef.child("survey").child(res_id).child(tomorrowAsString).setValue(survey);

                    Toast.makeText(AddSurvey.this, "Survey Successfully Added", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddSurvey.this,HomeActivity.class));


                }
                else Toast.makeText(AddSurvey.this, "Please Fill All The Options", Toast.LENGTH_SHORT).show();
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
            Intent intent=new Intent(AddSurvey.this, LoginActivity.class);
            startActivity(intent);
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
