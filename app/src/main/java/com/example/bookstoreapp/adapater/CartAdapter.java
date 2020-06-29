package com.example.bookstoreapp.adapater;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bookstoreapp.CartActivity;
import com.example.bookstoreapp.R;
import com.example.bookstoreapp.pojo.Books;
import com.example.bookstoreapp.pojo.Cart;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {


    private List<Books> booksList;
    private clickItem clickItem;

    public CartAdapter(List<Books> books, clickItem click){
        this.booksList=books;
        this.clickItem = click;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.cart_list,parent,false);
        return new CartAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartAdapter.ViewHolder holder, final int position) {
        holder.book_name.setText(booksList.get(position).getProductName());
        holder.author.setText(booksList.get(position).getAuthor());
        holder.price.setText(booksList.get(position).getPrice());

        //****for counting price
        String quan = booksList.get(position).getQuantity();
        String price = booksList.get(position).getPrice();
        int item_price = Integer.valueOf(price);
        int quantity = Integer.valueOf(quan);
        int totalPrice = item_price * quantity;
        holder.totalPrice.setText(Integer.toString(totalPrice));



        Glide.with(holder.book_image.getContext()).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
                .load(booksList.get(position).getUrl()).into(holder.book_image);
        holder.quantity.setText(booksList.get(position).getQuantity());
        holder.addOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickItem.onClickAdd(booksList.get(position), holder.getAdapterPosition());
            }
        });
        holder.removeOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickItem.onClickRemove(booksList.get(position),holder.getAdapterPosition());
            }
        });
        holder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickItem.onClickDelete(booksList.get(position),holder.getAdapterPosition());
            }
        });
    }
//****Count total price
//    public int getTotalPrice(){
//        int total_price=0;
//        for(int i=1;i<=booksList.size();i++){
//            total_price = total_price+totalPrice;
//        }
//        return total_price;
//    }



    @Override
    public int getItemCount() {
        return booksList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView book_name;
        ImageView book_image;
        TextView author;
        TextView price;
        TextView quantity;
       TextView totalPrice;
        Button removeOne;
        Button addOne;
        ImageButton removeItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.totalPrice = itemView.findViewById(R.id.total_price);
            this.book_image = itemView.findViewById(R.id.book_image_cart);
            this.book_name = itemView.findViewById(R.id.book_name_cart);
            this.author = itemView.findViewById(R.id.author_cart);
            this.price = itemView.findViewById(R.id.price_cart);
            this.quantity = itemView.findViewById(R.id.quantity);
            this.addOne = itemView.findViewById(R.id.add_item);
            this.removeOne = itemView.findViewById(R.id.remove_item);
            this.removeItem = itemView.findViewById(R.id.remove_all);
        }


    }

    public interface clickItem{
        void onClickAdd(Books book, int position);
        void onClickRemove(Books book,int position);
        void onClickDelete(Books book, int position);
    }

}
