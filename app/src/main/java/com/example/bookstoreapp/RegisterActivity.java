package com.example.bookstoreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstoreapp.pojo.Customer;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText mEmail;
    TextInputEditText mName;
    TextInputEditText mPassword;
    TextInputEditText mAddress;
    TextInputEditText mPincode;
    TextInputEditText mPhone;
    Button mRegister;
    Customer cust;
    TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mRegister = (Button) findViewById(R.id.register_btn_reg);
        mName = (TextInputEditText) findViewById(R.id.name_edit_txt_reg);
        mEmail = (TextInputEditText) findViewById(R.id.email_edit_txt_reg);
        mPassword = (TextInputEditText) findViewById(R.id.password_edit_txt_reg);
        mAddress = (TextInputEditText) findViewById(R.id.address_edit_txt_reg);
        mPincode = (TextInputEditText) findViewById(R.id.pincode_edit_txt_reg);
        mPhone = (TextInputEditText) findViewById(R.id.phone_edit_txt_reg);
        loginText = (TextView) findViewById(R.id.login_txt);


        //ProgressBars
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Please Wait...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        // progressBar.show();

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToLogin();
            }
        });


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email, name, password, address, phone_number, pincode;
                email = mEmail.getText().toString();
                name = mName.getText().toString();
                password = mPassword.getText().toString();
                address = mAddress.getText().toString();
                phone_number = mPhone.getText().toString();
                pincode = mPincode.getText().toString();

                if(name.length()!=0){

                    if(email.length()!=0){
                        if(password.length()!=0){
                            cust = new Customer();

                            cust.setName(name);
                            cust.setEmail(email);
                            cust.setAddress(address);
                            cust.setPassword(password);
                            cust.setLoginType("customer");
                            cust.setPhoneNumber(phone_number);
                            cust.setPincode(pincode);

                            Retrofit retrofit = RetrofitController.getRetrofit();
                            ApiInterface api = retrofit.create(ApiInterface.class);
                            Call<ResponseBody> call = api.createUser(cust);
                            progressBar.show();
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    progressBar.dismiss();
                                    String res = null;
                                    try {
                                        res = response.body().string();
                                 //       Toast.makeText(RegisterActivity.this, "Register successfull!!", Toast.LENGTH_SHORT).show();
                                        sendToMain();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }


                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    progressBar.dismiss();
                                    Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        }//end of pass
                        else {
                            mPassword.setError("Please enter Password");
                        }
                    }//end of name
                    else{
                        mEmail.setError("Please enter Name");
                    }
                }//end of email
                else{
                    mName.setError("Please enter email");
                }

            }
        });
    }

    private void sendToLogin() {
        Intent login_intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(login_intent);
        finish();
    }

    private void sendToMain() {
        Intent main_intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(main_intent);
        finish();
    }
}
