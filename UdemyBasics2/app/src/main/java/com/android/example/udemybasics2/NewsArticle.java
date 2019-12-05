package com.android.example.udemybasics2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class NewsArticle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_article);

        WebView webView = findViewById(R.id.web);

        webView.getSettings().getJavaScriptEnabled();

        String link = "";
        Intent extras = getIntent();
        Bundle value = extras.getExtras();

        if (getIntent().hasExtra("link")) {

            link = value.getString("link", String.valueOf(0));

            if (link != "0") {

                webView.loadUrl(link);

            }
        }

    }
}
