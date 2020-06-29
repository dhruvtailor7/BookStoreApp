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
import com.example.bookstoreapp.pojo.Books;
import com.example.bookstoreapp.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {


    public SearchAdapter(List<Books> list, clickProduct click){
        this.booksList=list;
        this.mclick=click;
    }
    private List<Books> booksList;
    private clickProduct mclick;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.book_list,parent,false);





        return new ViewHolder(listItem);
    }





    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.itemView.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mclick.onClick(booksList.get(position));
            }
        });
        holder.book_name.setText(booksList.get(position).getProductName());
        holder.author.setText(booksList.get(position).getAuthor());
        holder.price.setText(booksList.get(position).getPrice());
        Glide.with(holder.book_image.getContext()).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
                .load(booksList.get(position).getUrl()).into(holder.book_image);
    }


    @Override
    public int getItemCount() {
        return booksList.size();
    }





    class ViewHolder extends RecyclerView.ViewHolder{
        TextView book_name;
        ImageView book_image;
        TextView author;
        TextView price;

        ViewHolder(View itemView){
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
