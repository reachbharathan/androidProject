package com.android.example.udemybasics2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebInApp extends AppCompatActivity {

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        WebView webView = findViewById(R.id.web);

        webView.getSettings().getJavaScriptEnabled();

        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("https://www.youtube.com/watch?v=Zzhql6foHic");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
