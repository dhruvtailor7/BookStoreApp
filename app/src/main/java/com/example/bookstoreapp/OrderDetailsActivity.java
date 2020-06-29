package com.example.bookstoreapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstoreapp.adapater.OrderDetailsAdapter;
import com.example.bookstoreapp.pojo.Books;
import com.example.bookstoreapp.pojo.MerchantDetails;
import com.example.bookstoreapp.pojo.OrderDeatils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrderDetailsActivity extends AppCompatActivity implements OrderDetailsAdapter.rating{

    private List<OrderDeatils> orderDeatils;
    ImageButton done;
    private androidx.appcompat.widget.Toolbar toolbar;
    Retrofit retrofit = RetrofitController.getRetrofit();
    ApiInterface api = retrofit.create(ApiInterface.class);
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.checkoutToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        done = (ImageButton) findViewById(R.id.doneButton);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToMain();
            }
        });
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Please Wait...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);

        Intent intent = getIntent();
        String orderId = intent.getStringExtra("orderId");


        Call<List<OrderDeatils>> call = api.getOrderDetails(orderId);
        progressBar.show();
        call.enqueue(new Callback<List<OrderDeatils>>() {
            @Override
            public void onResponse(Call<List<OrderDeatils>> call, Response<List<OrderDeatils>> response) {
                orderDeatils=response.body();
                totalCost(orderDeatils);
                progressBar.dismiss();
                RecyclerView recyclerView = findViewById(R.id.orderRecyclerView);
                recyclerView.scrollToPosition(1);
                OrderDetailsAdapter orderDetailsAdapter = new OrderDetailsAdapter(orderDeatils,OrderDetailsActivity.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(OrderDetailsActivity.this,LinearLayoutManager.VERTICAL,false));
                recyclerView.setAdapter(orderDetailsAdapter);

            }

            @Override
            public void onFailure(Call<List<OrderDeatils>> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(OrderDetailsActivity.this,"Failed to Fetch",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendToMain(){
        Intent intent = new Intent(OrderDetailsActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void totalCost(List<OrderDeatils> orderDeatils){
        double cost=0;
        for(OrderDeatils orders: orderDeatils){
            double price = Double.parseDouble(orders.getCost());
            cost+=price;

        }
        String tot = String.valueOf(cost);
        TextView total = findViewById(R.id.price);

        total.setText(tot);
    }

    @Override
    public void mRating(OrderDeatils orderDetail, String str) {
        MerchantDetails merchantDetails = new MerchantDetails();
        merchantDetails.setMerchantId(orderDetail.getMerchantId());
        merchantDetails.setMerchantRating(str);
        Call<String> merchant = api.giveMerchantrating(merchantDetails);
        merchant.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                Toast.makeText(getBaseContext(),"Average rating: "+res,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    @Override
    public void pRating(OrderDeatils orderDeatil, String str) {
        Books book = new Books();
        book.setProductId(orderDeatil.getProductId());
        book.setRating(str);
        Call<String> product = api.giveProductRating(book);
        product.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                Toast.makeText(getBaseContext(),"Average rating: "+res,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
