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
import com.example.bookstoreapp.pojo.Books;

import java.util.ArrayList;
import java.util.List;

public class TopPicksAdapter extends RecyclerView.Adapter<TopPicksAdapter.ViewHolder> {

    public  TopPicksAdapter(List<Books> list,clickProduct click){
        this.topBooksList=list;
        this.clickProduct=click;
    }
    private List<Books> topBooksList;
    private clickProduct clickProduct;

    @NonNull
    @Override
    public TopPicksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.book_list,parent,false);
        return new TopPicksAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull TopPicksAdapter.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickProduct.onClick(topBooksList.get(position));
            }
        });
        holder.book_name.setText(topBooksList.get(position).getProductName());
        holder.author.setText(topBooksList.get(position).getAuthor());
        holder.price.setText(topBooksList.get(position).getPrice());
        Glide.with(holder.book_image.getContext()).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
                .load(topBooksList.get(position).getUrl()).into(holder.book_image);

    }

    @Override
    public int getItemCount() {
        return topBooksList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView book_name;
        ImageView book_image;
        TextView author;
        TextView price;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.book_image = itemView.findViewById(R.id.book_image);
            this.book_name = itemView.findViewById(R.id.book_name);
            this.author = itemView.findViewById(R.id.author);
            this.price = itemView.findViewById(R.id.price);
        }
    }

    public interface clickProduct{
        void onClick(Books book);
    }

}
