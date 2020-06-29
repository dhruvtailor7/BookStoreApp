package com.example.bookstoreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstoreapp.pojo.CustId;
import com.example.bookstoreapp.pojo.GoogleFacebookLogin;
import com.example.bookstoreapp.pojo.Login;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText emailEditText;
    TextInputEditText passwordEditText;
    Button loginBtn;
    TextView newUser;
    Button guestTxt;
    SignInButton google_btn;
    TextInputLayout emailInput;
    TextInputLayout passwordInput;
    GoogleSignInClient mGoogleSignInClient;
    String googleIdToken;
    int RC_SIGN_IN = 0;
    SharedPreferences sharedPreferences;
    public static final String myPreference = "mypref";
    private Retrofit retrofit;
    private ApiInterface api;
    private GoogleFacebookLogin googleFacebookLogin;
    private Login login;
    private CustId custId;
    private LoginButton loginButton;
    private CallbackManager callbackManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        googleFacebookLogin = new GoogleFacebookLogin();

        //FacebookLogin

        loginButton = (LoginButton) findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
//        checkLoginStatus();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                String accessToken = loginResult.getAccessToken().getToken();
                googleFacebookLogin.setAccessToken(accessToken);
                googleFacebookLogin.setLoginType("customer");
                Call<CustId> custIdCall = api.getCustIdFB(googleFacebookLogin);
                custIdCall.enqueue(new Callback<CustId>() {
                    @Override
                    public void onResponse(Call<CustId> call, Response<CustId> response) {
                        custId=response.body();

                        if(custId.getResponse().equals("Not Found")){
                            Toast.makeText(LoginActivity.this, "Facebook Error", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            String id = custId.getResponse();
                            editor.putString("user_id",id);
                            editor.commit();
//                            Toast.makeText(LoginActivity.this, "Successful!!", Toast.LENGTH_SHORT).show();
                            sendToMain();
                        }


                    }

                    @Override
                    public void onFailure(Call<CustId> call, Throwable t) {
                        Toast.makeText(getBaseContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        try {
                            Log.d("response",response.toString());
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");
                            String id = object.getString("id");
                            String image_url = "https://graph.facebook.com/"+id+ "/picture?type=normal";
                            Toast.makeText(LoginActivity.this, "Loading Profile", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
//                Request Graph APIS
                Bundle parameters = new Bundle();
                parameters.putString("fields","first_name,last_name,email,id");
                request.setParameters(parameters);
                request.executeAsync();
                sendToMain();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        //ProgressBars
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Please Wait...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        // progressBar.show();
        //Initializing all the views
        newUser = (TextView) findViewById(R.id.register_btn);
        loginBtn = (Button) findViewById(R.id.login_btn_1);
        emailEditText = (TextInputEditText) findViewById(R.id.email_edit_txt);
        passwordEditText = (TextInputEditText) findViewById(R.id.password_edit_txt);
        google_btn = (SignInButton) findViewById(R.id.google_btn);
        guestTxt = (Button) findViewById(R.id.guest_txt);
        sharedPreferences = getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        emailInput = (TextInputLayout) findViewById(R.id.email_input);
        passwordInput = (TextInputLayout) findViewById(R.id.password_input);


        login = new Login();
        custId = new CustId();

        retrofit = RetrofitController.getRetrofit();
        api = retrofit.create(ApiInterface.class);

        final String id = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("guest_id",id);
        editor.commit();


        //Go as guest
        guestTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToMain();
            }
        });

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(LoginActivity.this, "reg btn clkd!", Toast.LENGTH_SHORT).show();
                sendToRegister();

            }
        });


        //-------------------------------********** GOOGLE **********-------------------------------

        //GOOGLE SIGN-IN
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("344354043561-q5c5oqerfi6srq2jhpj4mfvud6ldrntq.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // LOGIN BUTTON IMPLEMENTATION
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                final String password = passwordEditText.getText().toString();



                if(email.length()!=0){
                    emailInput.setErrorEnabled(false);

                    if(password.length()!=0){
                        passwordInput.setErrorEnabled(false);
                        login.setEmail(email);
                        login.setPassword(password);
                        login.setLoginType("customer");
                        login.setDeviceId(id);

                        Call<CustId> call = api.getCustId(login);
                        progressBar.show();
                        call.enqueue(new Callback<CustId>() {
                            @Override
                            public void onResponse(Call<CustId> call, Response<CustId> response) {
                                progressBar.dismiss();
                                custId=response.body();

                                if(custId.getResponse().equals("Not registered")){
                                    Toast.makeText(LoginActivity.this, "Please Register First", Toast.LENGTH_SHORT).show();
                                }else if(custId.getResponse().equals("Wrong Password")){
                             //       Toast.makeText(LoginActivity.this, "wrong Password", Toast.LENGTH_SHORT).show();
                                    passwordInput.setError("Invalid Password");
                                }
                                else {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    String id = custId.getResponse();
                                    editor.putString("user_id",id);
                                    editor.commit();
                                 //   Toast.makeText(LoginActivity.this, "Successful!!", Toast.LENGTH_SHORT).show();
                                    sendToMain();
                                }


                            }

                            @Override
                            public void onFailure(Call<CustId> call, Throwable t) {
                                progressBar.dismiss();
                                Toast.makeText(getBaseContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        passwordEditText.setError("Please enter Password");
                        passwordInput.setError("Please enter Password");
                    }

                }else {
                    Toast.makeText(LoginActivity.this, "Please enter credentials!!", Toast.LENGTH_SHORT).show();
                    //    emailEditText.setError("Please enter email");

                }


      }
        });


        google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.google_btn:
                        googleSignIn();
                        break;

                }
            }
        });


        //Information from google
//        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
//        if (acct != null) {
//            String personName = acct.getDisplayName();
//            String personGivenName = acct.getGivenName();
//            String personFamilyName = acct.getFamilyName();
//            String personEmail = acct.getEmail();
//            String personId = acct.getId();
//            Uri personPhoto = acct.getPhotoUrl();
//            String token = acct.getIdToken();
//        }





    }//END-OnCreate




    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

//for google
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //For Facebook
        callbackManager.onActivityResult(requestCode,resultCode,data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }




    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            googleIdToken = account.getIdToken();

            googleFacebookLogin.setAccessToken(googleIdToken);
            googleFacebookLogin.setLoginType("customer");
            Call<CustId> custIdCall = api.getCustIdGoogle(googleFacebookLogin);
            custIdCall.enqueue(new Callback<CustId>() {
                @Override
                public void onResponse(Call<CustId> call, Response<CustId> response) {
                    custId=response.body();

                    if(custId.getResponse().equals("Not Found")){
                        Toast.makeText(LoginActivity.this, "Google Error", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        String id = custId.getResponse();
                        editor.putString("user_id",id);
                        editor.commit();
//                        Toast.makeText(LoginActivity.this, "Successful!!", Toast.LENGTH_SHORT).show();
                        sendToMain();
                    }


                }

                @Override
                public void onFailure(Call<CustId> call, Throwable t) {
                    Toast.makeText(getBaseContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("error", "signInResult:failed code=" + e.getStatusCode()+" "+e.getMessage());
            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
        }
    }





    private void sendToMain() {
        Intent main_intent = new Intent(LoginActivity.this,MainActivity.class);
        main_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(main_intent);
        finish();
    }


    private void sendToRegister() {
        Intent reg_intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(reg_intent);
        finish();
    }

//***********For Facebook
//        private void checkLoginStatus(){
//          if(AccessToken.getCurrentAccessToken() != null){
//            Toast.makeText(this, AccessToken.getCurrentAccessToken().getUserId(), Toast.LENGTH_SHORT).show();
//          }
//        }

}
