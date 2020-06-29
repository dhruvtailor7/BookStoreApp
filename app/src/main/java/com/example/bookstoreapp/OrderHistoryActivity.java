package com.example.bookstoreapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bookstoreapp.adapater.OrderHistoryAdapter;
import com.example.bookstoreapp.pojo.OrderHistory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrderHistoryActivity extends AppCompatActivity implements OrderHistoryAdapter.clickOrder {

    private androidx.appcompat.widget.Toolbar toolbar;
    private List<OrderHistory> orderHistories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.order_history_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Retrofit retrofit= RetrofitController.getRetrofit();
        ApiInterface api = retrofit.create(ApiInterface.class);
        Call<List<OrderHistory>> call = api.getOrderHistory(id);
        call.enqueue(new Callback<List<OrderHistory>>() {
            @Override
            public void onResponse(Call<List<OrderHistory>> call, Response<List<OrderHistory>> response) {
                orderHistories=response.body();
                RecyclerView recyclerView = findViewById(R.id.order_history_recycler);
                recyclerView.scrollToPosition(1);
                OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter(orderHistories,OrderHistoryActivity.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(OrderHistoryActivity.this,LinearLayoutManager.VERTICAL,false));
                recyclerView.setAdapter(orderHistoryAdapter);

            }

            @Override
            public void onFailure(Call<List<OrderHistory>> call, Throwable t) {
                Toast.makeText(OrderHistoryActivity.this,"Failed",Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onClick(OrderHistory orderHistory) {
        Intent intent = new Intent(OrderHistoryActivity.this,OrderDetailsActivity.class);
        intent.putExtra("orderId",orderHistory.getOrderId());
        startActivity(intent);
    }
}
