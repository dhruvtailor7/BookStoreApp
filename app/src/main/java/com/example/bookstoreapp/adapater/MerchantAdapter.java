package com.example.bookstoreapp.adapater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstoreapp.R;
import com.example.bookstoreapp.pojo.MerchantDetails;

import java.util.List;

public class MerchantAdapter extends RecyclerView.Adapter<MerchantAdapter.ViewHolder>{

    private List<MerchantDetails> merchantDetails;
    private clickMerchant mClickMerchant;

    public MerchantAdapter(List<MerchantDetails> merchants, MerchantAdapter.clickMerchant merchant){
        this.merchantDetails = merchants;
        this.mClickMerchant = merchant;
    }

    @NonNull
    @Override
    public MerchantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.merchant_list,parent,false);
        return new MerchantAdapter.ViewHolder(listItem);

    }

    @Override
    public void onBindViewHolder(@NonNull MerchantAdapter.ViewHolder holder, final int position) {
        holder.itemView.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickMerchant.onClick(merchantDetails.get(position));
            }
        });
        holder.merchantName.setText(merchantDetails.get(position).getMerchantName());
        holder.merchantRating.setText(merchantDetails.get(position).getMerchantRating());
        holder.price.setText(merchantDetails.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return merchantDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView merchantName;
        TextView merchantRating;
        TextView price;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.merchantName = itemView.findViewById(R.id.merchant_name);
            this.merchantRating = itemView.findViewById(R.id.merchant_rating);
            this.price = itemView.findViewById(R.id.merchant_price);
        }
    }

    public interface clickMerchant{
        void onClick(MerchantDetails merchantDetails);
    }
}
