package com.example.bookstoreapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bookstoreapp.adapater.MerchantAdapter;
import com.example.bookstoreapp.adapater.OrderHistoryAdapter;
import com.example.bookstoreapp.pojo.MerchantDetails;
import com.example.bookstoreapp.pojo.OrderHistory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MerchantDetailsActivity extends AppCompatActivity implements MerchantAdapter.clickMerchant{

    private List<MerchantDetails> merchantDetails;
    public String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_details);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        Retrofit retrofit= RetrofitController.getRetrofit();
        ApiInterface api = retrofit.create(ApiInterface.class);
        Call<List<MerchantDetails>> call = api.getMerchant(id);
        call.enqueue(new Callback<List<MerchantDetails>>() {
            @Override
            public void onResponse(Call<List<MerchantDetails>> call, Response<List<MerchantDetails>> response) {
                merchantDetails=response.body();
                RecyclerView recyclerView = findViewById(R.id.merchant_recycler);
                recyclerView.scrollToPosition(1);
                MerchantAdapter merchantAdapter= new MerchantAdapter(merchantDetails,MerchantDetailsActivity.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(MerchantDetailsActivity.this,LinearLayoutManager.VERTICAL,false));
                recyclerView.setAdapter(merchantAdapter);

            }

            @Override
            public void onFailure(Call<List<MerchantDetails>> call, Throwable t) {
                Toast.makeText(MerchantDetailsActivity.this,"Failed",Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onClick(MerchantDetails merchantDetails) {
        Intent intent = new Intent(MerchantDetailsActivity.this,ProductActivity.class);
        intent.putExtra("merchantId",merchantDetails.getMerchantId());
        intent.putExtra("id",id);
        intent.putExtra("price",merchantDetails.getPrice());
        startActivity(intent);
        finish();
    }
}
