package com.example.bookstoreapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bookstoreapp.pojo.Books;
import com.example.bookstoreapp.pojo.Cart;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductActivity extends AppCompatActivity  implements View.OnTouchListener {

    private List<Books> booksList;
    private Button add_to_cart_btn;
    private Button view_merchant;
    private Books books;
    private Cart cart;
    Retrofit retrofit= RetrofitController.getRetrofit();
    ApiInterface api = retrofit.create(ApiInterface.class);
    private ImageView bookImage;
    private List<Books> cartBookList;
    int flag;

    private Animator currentAnimator;
    private int shortAnimationDuration;
    SharedPreferences sharedPreferences;
    public static final String myPreference = "mypref";
    //for zooming he image
    private static final String TAG = "Touch";
    @SuppressWarnings("unused")
    private static final float MIN_ZOOM = 1f,MAX_ZOOM = 1f;


    // These matrices will be used to scale points of the image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // The 3 states (events) which the user is trying to perform
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // these PointF objects are used to record the point(s) the user is touching
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);


        //toolbar
        toolbar = findViewById(R.id.product_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("Product");


        bookImage = (ImageView) findViewById(R.id.book_img_prod);
        bookImage.setOnTouchListener(this);
        add_to_cart_btn = findViewById(R.id.add_to_cart);
        view_merchant = findViewById(R.id.view_merchant);


        books = new Books();
        cart = new Cart();
        Intent intent = getIntent();

        final String book_id = intent.getStringExtra("id");
        final String merchantId = intent.getStringExtra("merchantId");
        final String merchantPrice = intent.getStringExtra("price");

        final Call<Books> call =api.getProductById(book_id);
        call.enqueue(new Callback<Books>() {
            @Override
            public void onResponse(Call<Books> call, Response<Books> response) {
                books=response.body();
                if (books!=null) {
                    String bookName = books.getProductName();
                    String img = books.getUrl();
                    String author = books.getAuthor();
                    String publisher = books.getAttributes().get("publisher");
                    String isbn = books.getIsbn();
                    String genre = books.getGenre();
                    String rating = books.getRating();
                    String description = books.getDescription();
                    String year = books.getAttributes().get("year");
                    String binding = books.getAttributes().get("binding");
                    String pages = books.getAttributes().get("noofpages");
                    String mid = books.getMerchantId();
                    String price = books.getPrice();

                    if(merchantId!= null && merchantPrice!= null) {
                        price = merchantPrice;
                        mid = merchantId;
                    }


                    sharedPreferences = getSharedPreferences(myPreference, Context.MODE_PRIVATE);
                    String user_id = sharedPreferences.getString("user_id", null);


                    cart.setProductId(book_id);
                    cart.setMerchantId(mid);
                    cart.setQuantity("1");
                    if (user_id!=null) {
                        cart.setCartId(user_id);
                    }
                    else {
                        String id = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("guest_id",id);
                        editor.commit();
                        cart.setCartId(id);
                    }

                  //  Toast.makeText(getBaseContext(),mid,Toast.LENGTH_SHORT).show();

                    ImageView image = findViewById(R.id.book_img_prod);
                    TextView nameText = findViewById(R.id.book_name);
                    TextView authorText = findViewById(R.id.author);
                    TextView priceText = findViewById(R.id.price);
                    TextView publisherText = findViewById(R.id.publisher);
                    TextView bookDescText = findViewById(R.id.product_desc);
                    TextView ratingText = findViewById(R.id.rating);
                    TextView isbnText = findViewById(R.id.book_isbn);
                    TextView pagesText = findViewById(R.id.book_pages);
                    TextView genreText = findViewById(R.id.book_genre);
                    TextView yearText = findViewById(R.id.book_year);
                    TextView bindingText = findViewById(R.id.book_binding);
                    Glide.with(getBaseContext()).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
                            .load(img)
                            .into(image);

                    nameText.setText(bookName);
                    authorText.setText(author);
                    priceText.setText(price);
                    publisherText.setText(publisher);
                    bookDescText.setText(description);
                    ratingText.setText(rating);
                    isbnText.setText(isbn);
                    pagesText.setText(pages);
                    genreText.setText(genre);
                    yearText.setText(year);
                    bindingText.setText(binding);


                }
                else {
                    Toast.makeText(getBaseContext(),"Failed",Toast.LENGTH_SHORT).show();
                }

//                Toast.makeText(ProductActivity.this, "Success!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Books> call, Throwable t) {
                Toast.makeText(ProductActivity.this,"Failed",Toast.LENGTH_SHORT).show();
            }
        });

        add_to_cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToCart();
            }
        });


        view_merchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductActivity.this,MerchantDetailsActivity.class);
                intent.putExtra("id",book_id);
                startActivity(intent);
            }
        });

    }

    public void addItemToCart(){
        flag = 0;
        String userId = cart.getCartId();
        final String prodId = cart.getProductId();
        Call<List<Books>> listCall = api.getCart(userId);
        listCall.enqueue(new Callback<List<Books>>() {
            @Override
            public void onResponse(Call<List<Books>> call, Response<List<Books>> response) {
                cartBookList = response.body();
                if (null!=cartBookList) {
                    for (Books list : cartBookList) {
                        if (list.getProductId().equals(prodId)) {
                            flag = 1;
                            break;
                        }
                    }
                }
                if (flag==0) {
                    Call<ResponseBody> addToCart = api.addToCart(cart);
                    addToCart.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            String res = null;
                            try {
                                res = response.body().string();
                                Toast.makeText(getBaseContext(), res, Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(getBaseContext(),"Already Added",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Books>> call, Throwable t) {

            }
        });
    }


    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;

        dumpEvent(event);
        // Handle touch events here...

        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:   // first finger down only
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG"); // write to LogCat
                mode = DRAG;
                break;

            case MotionEvent.ACTION_UP: // first finger lifted

            case MotionEvent.ACTION_POINTER_UP: // second finger lifted

                mode = NONE;
                Log.d(TAG, "mode=NONE");
                break;

            case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down

                oldDist = spacing(event);
                Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 5f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOM");
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (mode == DRAG)
                {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
                }
                else if (mode == ZOOM)
                {
                    // pinch zooming
                    float newDist = spacing(event);
                    Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 5f)
                    {
                        matrix.set(savedMatrix);
                        scale = newDist / oldDist; // setting the scaling of the
                        // matrix...if scale > 1 means
                        // zoom in...if scale < 1 means
                        // zoom out
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix); // display the transformation on screen

        return true; // indicate event was handled
    }



    private float spacing(MotionEvent event)
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event)
    {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



    private void dumpEvent(MotionEvent event)
    {
        String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE","POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP)
        {
            sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }

        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++)
        {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }

        sb.append("]");
        Log.d("Touch Events ---------", sb.toString());
    }
}


