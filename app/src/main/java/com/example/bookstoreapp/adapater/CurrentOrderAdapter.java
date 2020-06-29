package com.example.bookstoreapp.adapater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bookstoreapp.R;
import com.example.bookstoreapp.pojo.OrderDeatils;

import java.util.ArrayList;
import java.util.List;

public class CurrentOrderAdapter extends RecyclerView.Adapter<CurrentOrderAdapter.ViewHolder> {
    private List<OrderDeatils> orderDeatils;

    public CurrentOrderAdapter(List<OrderDeatils> list){
        this.orderDeatils=list;
    }



    @NonNull
    @Override
    public CurrentOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.checkout_list,parent,false);
        return new CurrentOrderAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentOrderAdapter.ViewHolder holder, int position) {

                    holder.prodName.setText(orderDeatils.get(position).getProductName());
                    holder.prodQuantity.setText(orderDeatils.get(position).getQuantity());
                    holder.prodPrice.setText(orderDeatils.get(position).getCost());
                    Glide.with(holder.url.getContext()).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
                            .load(orderDeatils.get(position).getUrl()).into(holder.url);


    }

    @Override
    public int getItemCount() {
        return orderDeatils.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView prodName;
        TextView prodQuantity;
        TextView prodPrice;
        ImageView url;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.prodName = itemView.findViewById(R.id.order_product);
            this.prodQuantity =itemView.findViewById(R.id.order_quantity);
            this.prodPrice = itemView.findViewById(R.id.order_price);
            this.url = itemView.findViewById(R.id.order_url);
        }
    }
}
