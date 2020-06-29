package com.example.bookstoreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bookstoreapp.pojo.Customer;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyProfileActivity extends AppCompatActivity {

    private CircleImageView mProfilePic;
    Retrofit retrofit = RetrofitController.getRetrofit();
    ApiInterface api = retrofit.create(ApiInterface.class);
    SharedPreferences sharedPreferences;
    public static final String myPreference = "mypref";
    private Customer customer;

    private TextView mName;
    private TextView mEmail;
    private TextView mPhone;
    private TextView mLoginHistory;
    private TextView mOrderHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        mName = (TextView)findViewById(R.id.profile_name);
        mEmail = (TextView) findViewById(R.id.profile_email);
        mPhone = (TextView) findViewById(R.id.profile_phone);
        mLoginHistory = (TextView) findViewById(R.id.profile_login_history);
        mOrderHistory = (TextView) findViewById(R.id.profile_my_orders);

        sharedPreferences=getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        String account = sharedPreferences.getString("user_id",null);

        customer=new Customer();

        Call<Customer> call = api.getCustomer(account);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                customer=response.body();
                mProfilePic = (CircleImageView) findViewById(R.id.profile_image);

                Glide.with(MyProfileActivity.this).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_launcher_background))
                        .load("data:image/jpeg;base64,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")
                        .into(mProfilePic);

                //setting profile details in respective text fields
                    mName.setText(customer.getName());
                    mPhone.setText(customer.getPhoneNumber());
                    mEmail.setText(customer.getEmail());

            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Toast.makeText(getBaseContext(),"Login Required",Toast.LENGTH_SHORT).show();
            }
        });

//        mProfilePic = (CircleImageView) findViewById(R.id.profile_image);
//
//        Glide.with(this).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_launcher_background))
//                .load("https://image.shutterstock.com/image-photo/beautiful-water-drop-on-dandelion-260nw-789676552.jpg")
//                .into(mProfilePic);


        mLoginHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getSharedPreferences(myPreference, Context.MODE_PRIVATE);
                String account = sharedPreferences.getString("user_id", null);
                if (null!=account) {
                    Intent intent = new Intent(MyProfileActivity.this, LoginHistoryActivity.class);
                    intent.putExtra("id",account);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getBaseContext(),"Login Required",Toast.LENGTH_SHORT).show();
                }

            }
        });

        mOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getSharedPreferences(myPreference, Context.MODE_PRIVATE);
                String account = sharedPreferences.getString("user_id", null);
                if (null!=account) {
                    Intent intent = new Intent(MyProfileActivity.this, OrderHistoryActivity.class);
                    intent.putExtra("id",account);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getBaseContext(),"Login Required",Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
}
