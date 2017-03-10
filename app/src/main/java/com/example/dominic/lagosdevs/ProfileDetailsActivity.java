package com.example.dominic.lagosdevs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProfileDetailsActivity extends AppCompatActivity {
    TextView username;
    ImageView photo;
    TextView profileUrl;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        Intent myIntent=getIntent();
        //get data from the previous activity
        final Dev dev=(Dev) myIntent.getSerializableExtra("dev");

        //set and display Username
        username=(TextView) findViewById(R.id.username);
        username.setText("USERNAME: "+dev.getUsername());

        //set and display GitHub Profile URL
        profileUrl=(TextView) findViewById(R.id.profile_url);
        profileUrl.setText(Html.fromHtml("<b>URL: </b>").toString()+dev.getProfileUrl());
        //make the text clickable
        Linkify.addLinks(profileUrl,Linkify.WEB_URLS);

        //set and Profile Photo
        photo=(ImageView) findViewById(R.id.profile_photo);
        Picasso.with(this)
                .load(dev.getPhotoUrl())
                .error(R.drawable.loading)
                .into(photo);

        //share button
        btn=(Button) findViewById(R.id.share);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Check out this awesome developer @"+dev.getUsername()+", "+dev.getProfileUrl()+" .";
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

    }
}
