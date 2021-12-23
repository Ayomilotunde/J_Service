package com.j_service;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressDialog prDialog;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private  String url;
    ProgressBar progressBar;
    private LottieAnimationView lottieAnimationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress_circular);
        progressBar.setVisibility(View.VISIBLE);
        lottieAnimationView = findViewById(R.id.animationView);
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh_items);

        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new MyWebViewClient());

         url = "https://jambash.org/";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.loadUrl(url);

//        webView = findViewById(R.id.webView);
//        webView.setWebViewClient(new MyWebViewClient());
//
//        url = "https://www.ayomilotunde.github.io/";



        if (!DetectInternet.checkInternetConnection(this)) {

            webView.setVisibility(GONE);
            lottieAnimationView.setVisibility(VISIBLE);

            mSwipeRefreshLayout.setOnRefreshListener(() -> {

                if (!DetectInternet.checkInternetConnection(this)) {
                    Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();

                } else {
                    lottieAnimationView.setVisibility(GONE);
                    webView.setVisibility(VISIBLE);
//                webView.clearCache(true);
                    webView.clearHistory();
                    webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                    webView.getSettings().setBuiltInZoomControls(true);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

                    webView.loadUrl(url);

                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        if(mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }, 1000);

                }
                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    if(mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);

            });

            Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
        } else {
            webView.setVisibility(VISIBLE);


            mSwipeRefreshLayout.setOnRefreshListener(() -> {

                mSwipeRefreshLayout.setEnabled(false);

                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    if(mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    } else {
                        mSwipeRefreshLayout.setEnabled(false);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            });


        }

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {

            new AlertDialog.Builder(this).setTitle("Exit App").setMessage("Are you sure you want to Exit?")
                    .setNegativeButton("No", null).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.super.onBackPressed();
                }
            }).create().show();
        }
    }


    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
//            prDialog = new ProgressDialog(MainActivity.this);
//            prDialog.setMessage("Loading ...");
//            prDialog.show();
//            prDialog.setCancelable(false);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (progressBar != null) {
                progressBar.setVisibility(View.INVISIBLE);
//                prDialog.dismiss();
            }
        }
    }
}