package com.example.bookstoreapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.bookstoreapp.adapater.LoginHistoryAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginHistoryActivity extends AppCompatActivity {

    private ArrayList<String> loginHistory;
    private androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_history);

        //toolbar
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.login_history_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
            Retrofit retrofit= RetrofitController.getRetrofit();
            ApiInterface api = retrofit.create(ApiInterface.class);
            Call<ArrayList<String>> call = api.getLoginHistory(id);
            call.enqueue(new Callback<ArrayList<String>>() {
                @Override
                public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                    loginHistory = response.body();
                    RecyclerView recyclerView = findViewById(R.id.login_history_recycler);
                    recyclerView.scrollToPosition(1);
                    LoginHistoryAdapter loginHistoryAdapter = new LoginHistoryAdapter(loginHistory);
                    recyclerView.setLayoutManager(new LinearLayoutManager(LoginHistoryActivity.this,LinearLayoutManager.VERTICAL,false));
                    recyclerView.setAdapter(loginHistoryAdapter);

                }

                @Override
                public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                    Toast.makeText(LoginHistoryActivity.this,"Failed",Toast.LENGTH_LONG).show();
                }
            });

    }
}
