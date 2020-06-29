package com.example.bookstoreapp.adapater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bookstoreapp.ApiInterface;
import com.example.bookstoreapp.R;
import com.example.bookstoreapp.RetrofitController;
import com.example.bookstoreapp.pojo.OrderDeatils;

import java.util.List;

import retrofit2.Retrofit;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {
    private List<OrderDeatils> orderDetails;
    private rating mClick;
    public OrderDetailsAdapter(List<OrderDeatils> list, rating click){
        this.orderDetails =list;
        this.mClick = click;
    }
    Retrofit retrofit = RetrofitController.getRetrofit();
    ApiInterface api = retrofit.create(ApiInterface.class);


    @NonNull
    @Override
    public OrderDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.order_details_list,parent,false);
        return new OrderDetailsAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderDetailsAdapter.ViewHolder holder, final int position) {

        holder.prodName.setText(orderDetails.get(position).getProductName());
        holder.prodQuantity.setText(orderDetails.get(position).getQuantity());
        holder.prodPrice.setText(orderDetails.get(position).getCost());
        Glide.with(holder.url.getContext()).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
                .load(orderDetails.get(position).getUrl()).into(holder.url);

        Integer[] items = new Integer[]{1,2,3,4,5};
      //  ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.order_details_list,items);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(holder.merchantSpinner.getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        holder.orderSpinner.setAdapter(adapter);
        holder.merchantSpinner.setAdapter(adapter);
        holder.merchantRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = holder.merchantSpinner.getSelectedItem().toString();
                mClick.mRating(orderDetails.get(position),str);
            }
        });
        holder.orderRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  str = holder.orderSpinner.getSelectedItem().toString();
                mClick.pRating(orderDetails.get(position),str);
            }
        });


    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView prodName;
        TextView prodQuantity;
        TextView prodPrice;
        ImageView url;
        Button merchantRatingButton;
        Button orderRatingButton;
        Spinner merchantSpinner;
        Spinner orderSpinner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.prodName = itemView.findViewById(R.id.order_product);
            this.prodQuantity =itemView.findViewById(R.id.order_quantity);
            this.prodPrice = itemView.findViewById(R.id.order_price);
            this.url = itemView.findViewById(R.id.order_url);
            this.merchantRatingButton = itemView.findViewById(R.id.btn_merchant);
            this.orderRatingButton = itemView.findViewById(R.id.btn_order);
            this.merchantSpinner = itemView.findViewById(R.id.spinner_merchant);
            this.orderSpinner = itemView.findViewById(R.id.spinner_product);
        }
    }

    public interface rating{
        void mRating(OrderDeatils orderDetails, String str);
        void pRating(OrderDeatils orderDeatils, String str);
    }

}
