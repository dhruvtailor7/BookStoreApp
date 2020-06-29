package com.example.bookstoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;


import com.example.bookstoreapp.adapater.SearchAdapter;
import com.example.bookstoreapp.pojo.Books;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchActivity extends AppCompatActivity implements SearchAdapter.clickProduct {

    private List<Books> booksList;
    private Books books;
    private BottomNavigationView mSearchNav;
    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private TextInputEditText search;
    Retrofit retrofit= RetrofitController.getRetrofit();
    ApiInterface api = retrofit.create(ApiInterface.class);



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        search = (TextInputEditText)findViewById(R.id.search_bar);




        //ProgressBars
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Please Wait...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        // progressBar.show();



            //searchbar search btn
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        //searchbar
        search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    performSearch();
                    return true;
                }
                return false;
            }
        });
        //drawable left listner




        search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               final int DRAWABLE_LEFT = 0;
                 final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (search.getRight() - search.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        String text = search.getText().toString();
                        Call<List<Books>> call = api.getSearch(text);
                        progressBar.show();
                        call.enqueue(new Callback<List<Books>>() {
                            @Override
                            public void onResponse(Call<List<Books>> call, Response<List<Books>> response) {
                                progressBar.dismiss();
                                booksList = response.body();
                                recyclerView = findViewById(R.id.recycle);
                                searchAdapter = new SearchAdapter(booksList,SearchActivity.this);
                                int no_of_coloumns = 2;
                                recyclerView.setLayoutManager(new GridLayoutManager(SearchActivity.this,no_of_coloumns));
                                recyclerView.setAdapter(searchAdapter);
                            }

                            @Override
                            public void onFailure(Call<List<Books>> call, Throwable t) {
                                progressBar.dismiss();
                                Toast.makeText(SearchActivity.this,"Failed",Toast.LENGTH_LONG);
                            }
                        });

                        return true;
                    }
                }
                return false;
            }
        });








        //bottom nav
        mSearchNav = (BottomNavigationView) findViewById(R.id.bottom_nav_view_search);

        mSearchNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.bottom_nav_back: sendToMain();
                        return true;
                    case R.id.bottom_nav_cart:sendToCart();
                        return true;
                    case R.id.bottom_nav_home: sendToMain();
                    default:return false;
                }
            }
        });

        Intent intent = getIntent();
        String genre = intent.getStringExtra("genre");
        Call<List<Books>> booksByGenre = api.getBooksByGenre(genre);
        booksByGenre.enqueue(new Callback<List<Books>>() {
            @Override
            public void onResponse(Call<List<Books>> call, Response<List<Books>> response) {
                booksList=response.body();
                recyclerView = findViewById(R.id.recycle);
                searchAdapter = new SearchAdapter(booksList,SearchActivity.this);
                int no_of_coloumns = 2;
                recyclerView.setLayoutManager(new GridLayoutManager(SearchActivity.this,no_of_coloumns));
                recyclerView.setAdapter(searchAdapter);
            }

            @Override
            public void onFailure(Call<List<Books>> call, Throwable t) {
                Toast.makeText(SearchActivity.this,"Failed",Toast.LENGTH_LONG);
            }
        });




    }

    private void performSearch() {
        String text = search.getText().toString();
        Call<List<Books>> call = api.getSearch(text);
        call.enqueue(new Callback<List<Books>>() {
            @Override
            public void onResponse(Call<List<Books>> call, Response<List<Books>> response) {
                booksList = response.body();
                recyclerView = findViewById(R.id.recycle);
                searchAdapter = new SearchAdapter(booksList,SearchActivity.this);
                int no_of_coloumns = 2;
                recyclerView.setLayoutManager(new GridLayoutManager(SearchActivity.this,no_of_coloumns));
                recyclerView.setAdapter(searchAdapter);
            }

            @Override
            public void onFailure(Call<List<Books>> call, Throwable t) {
                Toast.makeText(SearchActivity.this,"Failed",Toast.LENGTH_LONG);
            }
        });

    }


    private void sendToCart() {

        Intent cart_intent  = new Intent(SearchActivity.this,CartActivity.class);
        startActivity(cart_intent);

    }

    private void sendToMain() {
        Intent main_intent  = new Intent(SearchActivity.this,MainActivity.class);
        startActivity(main_intent);
        finish();
    }

    @Override
    public void onClick(Books book) {
        String book_id = book.getProductId();
        Intent intent = new Intent(SearchActivity.this, ProductActivity.class);

        intent.putExtra("id",book_id);

        startActivity(intent);
    }

}
