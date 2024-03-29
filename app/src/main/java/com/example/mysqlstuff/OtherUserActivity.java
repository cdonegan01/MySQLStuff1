package com.example.mysqlstuff;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mysqlstuff.adapter.ReviewAdapter1;
import com.example.mysqlstuff.adapter.ReviewAdapter2;
import com.example.mysqlstuff.model.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class OtherUserActivity  extends AppCompatActivity {

    private String URL_JSON = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/reviewList2.php";
    private JsonArrayRequest ArrayRequest ;
    private RequestQueue requestQueue ;
    private List<Review> lstReviews;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);

        getSupportActionBar().hide();

        int idNo = getIntent().getExtras().getInt("otherUserId");
        String name  = getIntent().getExtras().getString("otherUsername");
        String bio = getIntent().getExtras().getString("otherBio");
        String reviewTitle = "Reviews by "+name;
        String image_url = getIntent().getExtras().getString("otherProfilePic");

        TextView otherUserName = findViewById(R.id.otherUserName);
        TextView otherUserBio = findViewById(R.id.otherUserBio);
        TextView otherReviewsTitle = findViewById(R.id.otherReviewsTitle);
        ImageView otherUserProfile = findViewById(R.id.otherUserProfile);

        otherUserName.setText(name);
        otherUserBio.setText(bio);
        otherReviewsTitle.setText(reviewTitle);
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading).error(R.drawable.loading);
        Glide.with(this).load(image_url).apply(requestOptions).into(otherUserProfile);

        lstReviews = new ArrayList<>();
        recyclerView = findViewById(R.id.otherUserReviews);
        jsoncall();
    }

    private void jsoncall() {
        ArrayRequest = new JsonArrayRequest(URL_JSON, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;
                for (int i = 0 ; i<response.length();i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Review review = new Review();
                        review.setReviewId(jsonObject.getInt("Review_id"));
                        review.setGameName(jsonObject.getString("title"));
                        review.setGamePictureUrl(jsonObject.getString("cover_art"));
                        review.setLikes(jsonObject.getInt("Likes"));
                        review.setRating(jsonObject.getInt("Rating"));
                        review.setReview(jsonObject.getString("Review"));
                        review.setGameId(jsonObject.getInt("id"));
                        review.setAuthorId(jsonObject.getInt("Author"));
                        lstReviews.add(review);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setRvadapter(lstReviews);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(OtherUserActivity.this);
        requestQueue.add(ArrayRequest);
    }

    public void setRvadapter (List<Review> lstReviews) {
        ReviewAdapter2 myAdapter = new ReviewAdapter2(this,lstReviews) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

    }
}
