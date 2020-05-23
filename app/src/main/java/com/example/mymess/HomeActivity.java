package com.example.mymess;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymess.Login.*;
import com.example.mymess.Utils.*;
import com.example.mymess.models.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements IAdapterClicked {

    private static final String TAG = "HomeActivity";
    private Context mContext=HomeActivity.this;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FireBaseMethods mFireBaseMethods;

    private RecyclerView recyclerView;
    private MyRvAdapter rvAdapter;

    private ArrayList<Restaurant> restaurantArrayList=new ArrayList<>();

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView=findViewById(R.id.rv_mainfeed);
        progressBar=findViewById(R.id.pb_main);
        setupFirebaseAuth();

        progressBar.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rvAdapter = new MyRvAdapter(restaurantArrayList, mContext);
        recyclerView.setAdapter(rvAdapter);
        rvAdapter.notifyDataSetChanged();
        rvAdapter.setiAdapterClicked(this);

        // getRestaurants();
    }

    private void getRestaurants() {

        Log.d(TAG, "getRestaurants: Getting Restaurants");
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        Query query=reference
                .child(getString(R.string.dbname_restaurant))
                .orderByKey();

        restaurantArrayList.clear();
        rvAdapter.notifyDataSetChanged();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Restaurant restaurant=snapshot.getValue(Restaurant.class);
                    restaurantArrayList.add(restaurant);
                    rvAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.d(TAG, "onDataChange: restaurants "+restaurant.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                    getRestaurants();
                }
                else
                {
                    Log.d(TAG, "onAuthStateChanged: signed out");
                }


            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void checkCurrentUser(FirebaseUser user)
    {
        if (user==null)
        {
            Intent intent=new Intent(mContext,LoginActivity.class);
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

    @Override
    public void onPostionTap(int position) {

        Intent intent=new Intent(HomeActivity.this,MessScreenActivity.class);
        intent.putExtra("name",restaurantArrayList.get(position).getRes_name());
        intent.putExtra("type",restaurantArrayList.get(position).getRes_type());
        intent.putExtra("timing",restaurantArrayList.get(position).getTiming());
        intent.putExtra("price",restaurantArrayList.get(position).getPrice());
        intent.putExtra("rating",restaurantArrayList.get(position).getRating());
        intent.putExtra("image",restaurantArrayList.get(position).getProfile_pic());
        intent.putExtra("res_id",restaurantArrayList.get(position).getRes_id());
        startActivity(intent);finish();
    }
}
