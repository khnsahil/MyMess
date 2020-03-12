package com.example.mymess;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymess.models.Restaurant;

import java.util.List;

public class MyRvAdapter extends RecyclerView.Adapter<MyRvAdapter.ViewHolder> {

    private static final String TAG = "MyRvAdapter";

    private List<Restaurant> restaurantList;
    private Context mContext;
    private IAdapterClicked iAdapterClicked;

    public void setiAdapterClicked(IAdapterClicked iAdapterClicked) {
        this.iAdapterClicked = iAdapterClicked;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView Name, Type, Price,Rating,Timing;
        ImageView profile;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.tv_rest_name);
            Type = itemView.findViewById(R.id.tv_res_type);
            Price = itemView.findViewById(R.id.tv_price);
            Rating = itemView.findViewById(R.id.tv_rating);
            Timing = itemView.findViewById(R.id.tv_timings);
            profile = itemView.findViewById(R.id.im_rest);


            Log.d(TAG, "ViewHolder: Constructor Ok");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iAdapterClicked != null) {
                        iAdapterClicked.onPostionTap(getAdapterPosition());
                    }
                }
            });

        }
    }

    public MyRvAdapter(List<Restaurant> restaurants, Context context) {
        this.restaurantList = restaurants;
        this.mContext = context;

        Log.d(TAG, "MyRvAdapter: Constructor ok");
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder: aaa");
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_main, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Restaurant rest = restaurantList.get(i);
        viewHolder.Name.setText(rest.getRes_name());
        viewHolder.Price.setText("Avg.Price: "+rest.getPrice());
        viewHolder.Type.setText(rest.getRes_type());
        viewHolder.Timing.setText("Timing: "+rest.getTiming());
        viewHolder.Rating.setText("Rating: "+rest.getRating());
        Glide.with(mContext).load(rest.getProfile_pic()).into(viewHolder.profile);
        Log.d(TAG, "onBindViewHolder: views set");

    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    //8602 606 409

}