package com.example.bookstoreapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstoreapp.adapater.CurrentOrderAdapter;
import com.example.bookstoreapp.pojo.OrderDeatils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CheckoutActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static final String myPreference = "mypref";
    private List<OrderDeatils> orderDeatils;
    ImageButton done;
    TextView order_id,total_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        done = (ImageButton) findViewById(R.id.doneButton);

        sharedPreferences = getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        String account = sharedPreferences.getString("user_id", null);

        updateUI(account);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(CheckoutActivity.this, "Thanks for ordering!!", Toast.LENGTH_LONG).show();
                sendToMain();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        }

    private void updateUI(String account) {

        //ProgressBars
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Please Wait...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
         progressBar.show();

        if (account != null) {
            Retrofit retrofit= RetrofitController.getRetrofit();
            ApiInterface api = retrofit.create(ApiInterface.class);
            Call<List<OrderDeatils>> call = api.getCurrentOrder(account);
            call.enqueue(new Callback<List<OrderDeatils>>() {
                @Override
                public void onResponse(Call<List<OrderDeatils>> call, Response<List<OrderDeatils>> response) {
                    progressBar.dismiss();
                    orderDeatils = response.body();
                    if (null!=orderDeatils){

                        totalCost(orderDeatils);
                        RecyclerView recyclerView = findViewById(R.id.orderRecyclerView);
                        recyclerView.scrollToPosition(1);
                        CurrentOrderAdapter currentOrderAdapter = new CurrentOrderAdapter(orderDeatils);
                        recyclerView.setLayoutManager(new LinearLayoutManager(CheckoutActivity.this,LinearLayoutManager.VERTICAL,false));
                        recyclerView.setAdapter(currentOrderAdapter);
                    }
                    else {
                        Toast.makeText(getBaseContext(),"Cart was Empty",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<OrderDeatils>> call, Throwable t) {
                    progressBar.dismiss();
                    Log.e("checkout error", "onFailure: "+t.toString() );
                    Toast.makeText(CheckoutActivity.this,"Quantity not available. Sorry For Inconvenience. :(",Toast.LENGTH_LONG).show();

                }
            });

        } else {
            sendToLogin();
            Toast.makeText(this, "Please Login First", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendToLogin() {

        Intent login_intent = new Intent(CheckoutActivity.this, LoginActivity.class);
        startActivity(login_intent);
    }

    private void sendToMain(){
        Intent intent = new Intent(CheckoutActivity.this,MainActivity.class);
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
}
