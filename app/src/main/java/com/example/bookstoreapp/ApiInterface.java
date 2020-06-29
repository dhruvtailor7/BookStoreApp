package com.example.bookstoreapp;

import com.example.bookstoreapp.pojo.Books;
import com.example.bookstoreapp.pojo.Cart;
import com.example.bookstoreapp.pojo.CustId;
import com.example.bookstoreapp.pojo.Customer;
import com.example.bookstoreapp.pojo.GoogleFacebookLogin;
import com.example.bookstoreapp.pojo.Login;
import com.example.bookstoreapp.pojo.MerchantDetails;
import com.example.bookstoreapp.pojo.OrderDeatils;
import com.example.bookstoreapp.pojo.OrderHistory;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

      @POST("/login/signup")
      Call<ResponseBody>  createUser(@Body Customer customer);

      @POST("/login/login")
      Call<CustId> getCustId(@Body Login login);

      @POST("/login/googlelogin")
      Call<CustId> getCustIdGoogle(@Body GoogleFacebookLogin googleFacebookLogin);

      @POST("/login/facebooklogin")
      Call<CustId> getCustIdFB(@Body GoogleFacebookLogin googleFacebookLogin);

      @GET("/login/getLoginHistoryById/{id}")
      Call<ArrayList<String>> getLoginHistory(@Path("id") String id);

      @GET("/customer/getCustomerById/{id}")
      Call<Customer> getCustomer(@Path("id") String id);

      @GET("http://192.168.2.7:8090/search/search")
      Call<List<Books>> getSearch(
              @Query("keyword") String keyword
      );

      @GET("/product/getGenreList")
      Call<ArrayList<String>> getGenre();

      @GET("/product/getTopProducts")
      Call<List<Books>> getTopBooks();

      @GET("/product/getProductByGenre/{genre}")
      Call<List<Books>> getBooksByGenre(@Path("genre") String genre);

      @GET("/product/getProductByProductId/{id}")
      Call<Books> getProductById(@Path("id") String id);

      @GET("/merchant/getMerchantByProductId/{id}")
      Call<List<MerchantDetails>> getMerchant(@Path("id") String id);

      @POST("/cart/addToCart")
      Call<ResponseBody> addToCart(@Body Cart cart);

      @GET("/cart/getFromCart/{id}")
      Call<List<Books>> getCart(@Path("id") String id);

      @GET("/order/checkout/{id}")
      Call<List<OrderDeatils>> getCurrentOrder(@Path("id") String id);

      @GET("/order/getOrderHistoryByCustomerId/{id}")
      Call<List<OrderHistory>> getOrderHistory(@Path("id") String id);

      @GET("/order/getOrderDetailsbyOrderId/{id}")
      Call<List<OrderDeatils>> getOrderDetails(@Path("id") String id);

      @POST("/merchant/addMerchantRating")
      Call<String> giveMerchantrating(@Body MerchantDetails merchantDetails);

      @POST("/product/addProductRating")
      Call<String> giveProductRating(@Body Books books);
}
