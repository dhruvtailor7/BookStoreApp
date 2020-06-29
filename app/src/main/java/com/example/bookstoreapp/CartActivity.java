package com.example.bookstoreapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.bookstoreapp.adapater.CartAdapter;
import com.example.bookstoreapp.pojo.Books;
import com.example.bookstoreapp.pojo.Cart;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CartActivity extends AppCompatActivity implements CartAdapter.clickItem {


//    GoogleSignInClient mGoogleSignInClient;
//    GoogleApiClient mGoogleApiClient;
  //  private BottomNavigationView mCartNav;
    SharedPreferences sharedPreferences;
    public static final String myPreference = "mypref";
    Retrofit retrofit = RetrofitController.getRetrofit();
    ApiInterface api = retrofit.create(ApiInterface.class);
    private List<Books> cartList;
    private Cart cart;
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private androidx.appcompat.widget.Toolbar toolbar;
    private ImageButton checkoutBtn;
    private Boolean emptyFlag=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        //toolbar
        toolbar =  findViewById(R.id.cartToolBar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //ProgressBars
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Please Wait...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        // progressBar.show();

         checkoutBtn =  findViewById(R.id.checkout_btn);
        cart = new Cart();

        //bottom nav



        sharedPreferences=getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        String account = sharedPreferences.getString("user_id",null);
        if(account==null){
            account = sharedPreferences.getString("guest_id",null);
        }

        Call<List<Books>> call = api.getCart(account);
        progressBar.show();
        call.enqueue(new Callback<List<Books>>() {
            @Override
            public void onResponse(Call<List<Books>> call, Response<List<Books>> response) {
                progressBar.dismiss();
                cartList =response.body();
                if (cartList.size()!=0)
                    emptyFlag=false;
                recyclerView = findViewById(R.id.cart_recycler);
                cartAdapter = new CartAdapter(cartList,CartActivity.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                recyclerView.setAdapter(cartAdapter);
            }

            @Override
            public void onFailure(Call<List<Books>> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(getBaseContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        //ActionBar
        //Objects.requireNonNull(getSupportActionBar()).setTitle("Cart");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emptyFlag){
                    Toast.makeText(CartActivity.this,"Cart is empty",Toast.LENGTH_SHORT).show();
                }
                else {
                    sentToCheckOut();
                }
            }
        });

    }

    private void sendToMain() {
        Intent main_intent  = new Intent(CartActivity.this,MainActivity.class);
        startActivity(main_intent);
        finish();
    }

    private void sentToCheckOut() {

        Intent checkout_intent = new Intent(CartActivity.this, CheckoutActivity.class);
        startActivity(checkout_intent);
        finish();

    }

    protected void onStart() {
        super.onStart();
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        sharedPreferences=getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        String account = sharedPreferences.getString("user_id",null);
        updateUI(account);
    }

    private void updateUI(String account) {
        if(account!= null){
//            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);


        }else{

        }
    }

    @Override
    public void onClickAdd(final Books book,final int position) {
        int quantity = Integer.parseInt(book.getQuantity());
        quantity+=1;
        final String q = String.valueOf(quantity);
        cart.setMerchantId(book.getMerchantId());
        cart.setProductId(book.getProductId());
        cart.setQuantity(q);
        sharedPreferences=getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        String account = sharedPreferences.getString("user_id",null);
        if(account==null){
            account = sharedPreferences.getString("guest_id",null);
        }
        cart.setCartId(account);

        Call<ResponseBody> call = api.addToCart(cart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    if(null != response.body() && response.body().string().toLowerCase().equals("success")){
                        Books books = cartList.get(position);
                        books.setQuantity(q);
                        cartList.set(position, book);
                        cartAdapter.notifyDataSetChanged();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Failed :(", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClickDelete(final Books book, final int position) {
        int quantity=0;
        final String q = String.valueOf(quantity);
        cart.setMerchantId(book.getMerchantId());
        cart.setProductId(book.getProductId());
        cart.setQuantity(q);
        sharedPreferences=getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        String account = sharedPreferences.getString("user_id",null);
        if(account==null){
            account = sharedPreferences.getString("guest_id",null);
        }
        cart.setCartId(account);

        Call<ResponseBody> call = api.addToCart(cart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    if(null != response.body() && response.body().string().toLowerCase().equals("success")){
                        Books books = cartList.get(position);
                        books.setQuantity(q);
                        cartList.set(position, book);
                        cartList.remove(position);
                        cartAdapter.notifyItemRemoved(position);
                        emptyFlag=true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Failed :(", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onClickRemove(final Books book, final int position) {
        int quantity = Integer.parseInt(book.getQuantity());
        quantity-=1;
        final String q = String.valueOf(quantity);
        cart.setMerchantId(book.getMerchantId());
        cart.setProductId(book.getProductId());
        cart.setQuantity(q);
        sharedPreferences=getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        String account = sharedPreferences.getString("user_id",null);
        if(account==null){
            account = sharedPreferences.getString("guest_id",null);
        }
        cart.setCartId(account);

        Call<ResponseBody> call = api.addToCart(cart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    if(null != response.body() && response.body().string().toLowerCase().equals("success")){
                        Books books = cartList.get(position);
                        books.setQuantity(q);
                        cartList.set(position, book);
                        if (q.equals("0")){
                            cartList.remove(position);
                            cartAdapter.notifyItemRemoved(position);
                            emptyFlag=true;
                        }
                        else {
                            cartAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Failed :(", Toast.LENGTH_SHORT).show();
            }
        });

    }
}