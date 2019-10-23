package com.zuhair.zuhair.realscholarshipapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;


import java.io.IOException;

public class DetailActivity extends AppCompatActivity {

    TextView mScholarshipName;
    WebView webView;
    ProgressBar progressBar;
    private AdView mBannerAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /** ADS START HERE **/

        mBannerAd = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mBannerAd.loadAd(adRequest);

        mBannerAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.

                mBannerAd.setVisibility(View.GONE);
            }
        });

        /** ADS END HERE **/

        mScholarshipName = findViewById(R.id.tvScholarshipName);
        progressBar = findViewById(R.id.progressBarCircular);
        progressBar.setVisibility(View.GONE);


        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        new MyTask().execute();


        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
//            mIcon.setText(mBundle.getString("icon"));
//            ((GradientDrawable) mIcon.getBackground()).setColor(mBundle.getInt("colorIcon"));
            mScholarshipName.setText(mBundle.getString("sender"));
        }

    }

    private class MyTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            progressBar.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            progressBar.setProgress(100);

            String ourUrl = "";
            Bundle mBundle = getIntent().getExtras();
            if (mBundle != null) {
                ourUrl = mBundle.getString("link");
            }

            org.jsoup.nodes.Document doc = null;
            try {
                doc = Jsoup.connect(ourUrl).get();

            } catch (IOException e) {

                onCancelled();

            }
            if(doc != null ){
                Elements ele = doc.select("div.entry.clearfix");
                String html = ele.toString();
                return html;

            } else {
                return null;
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            cancel(true);

            progressBar.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            Toast.makeText(DetailActivity.this, "Please, Check you internet Connection and try again!", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(String result) {

            progressBar.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);

            //if you had a ui element, you could display the title
            String mime = "text/html";
            String encoding = "utf-8";
            if(result != null){
                webView.loadData(result, mime, encoding);
            }
        }
    }
}