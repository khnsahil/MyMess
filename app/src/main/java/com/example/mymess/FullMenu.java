package com.example.mymess;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymess.Login.LoginActivity;
import com.example.mymess.models.MenuItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FullMenu extends AppCompatActivity {
    private static final String TAG = "FullMenu";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userId;
    HashMap<String,Double> objectMap;
    ArrayList<MenuItem> list=new ArrayList<>();

    RecyclerView recyclerView;
    MyMenuRvAdapter myMenuRvAdapter;
    Context context=FullMenu.this;
    TextView name;
    String res_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_menu);


        setupFirebaseAuth();

        name=findViewById(R.id.tv_mess_name);
        Intent intent = getIntent();
        name.setText(intent.getStringExtra("name"));
        res_id=intent.getStringExtra("res_id");

       // String res_id="-M-xlv43EDQCoV1m73bo";
        Query query = myRef.child("menu").child(res_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    MenuItem menuItem=new MenuItem();
                    for (DataSnapshot snapshot1:snapshot.getChildren())
                    {
                        if (snapshot1.getKey().equals("price")) menuItem.setPrice(Double.parseDouble(snapshot1.getValue().toString()));
                        else  menuItem.setName(snapshot1.getValue().toString());
                        //Log.d(TAG, "onDataChange: inner most "+snapshot1.toString());
                    }

                    Log.d(TAG, "onDataChange: my menu item: "+menuItem.toString());
                    list.add(menuItem);
//
//                    Log.d(TAG, "onDataChange: objmap "+snapshot.getValue().toString());
//                    //MenuItem menuItem=(MenuItem) snapshot.getValue(MenuItem.class);
//                    //objectMap = (HashMap<String, Integer>) snapshot.getValue();
//
//                    if (objectMap!=null) init();
//                    Log.d(TAG, "onDataChange: menu item: "+objectMap.toString());
                }

                // if (!list.isEmpty()) init();
                init();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void init()
    {
        Log.d(TAG, "init: called");
//        for (Map.Entry mapel:objectMap.entrySet())
//        {
//            String key=(String) mapel.getKey();
//            Double val=Double.parseDouble(mapel.getValue().toString());
//
//            list.add(new MenuItem(key,val));
//
//        }

        recyclerView=findViewById(R.id.rv_menu);
        myMenuRvAdapter=new MyMenuRvAdapter(list,context);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myMenuRvAdapter);
        myMenuRvAdapter.notifyDataSetChanged();
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
            Intent intent=new Intent(FullMenu.this, LoginActivity.class);
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
