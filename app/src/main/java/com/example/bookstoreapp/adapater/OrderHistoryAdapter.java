package com.example.bookstoreapp.adapater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstoreapp.R;
import com.example.bookstoreapp.pojo.OrderHistory;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>{

    private List<OrderHistory> orderHistories;
    private clickOrder mClickOrder;

    public OrderHistoryAdapter(List<OrderHistory> orders, clickOrder order){
        this.orderHistories = orders;
        this.mClickOrder = order;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.order_history_list,parent,false);
        return new OrderHistoryAdapter.ViewHolder(listItem);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.itemView.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickOrder.onClick(orderHistories.get(position));
            }
        });
        holder.order_history.setText(orderHistories.get(position).getOrderId());
        holder.timestamp.setText(orderHistories.get(position).getTimestamp());
    }

    @Override
    public int getItemCount() {
        return orderHistories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView order_history;
        TextView timestamp;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.order_history = itemView.findViewById(R.id.order_history);
            this.timestamp = itemView.findViewById(R.id.order_timestamp);
        }
    }

    public interface clickOrder{
        void onClick(OrderHistory orderHistory);
    }
}
