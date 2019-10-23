package com.zuhair.zuhair.realscholarshipapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    private AdView mBannerAd;
    ListView listView;
    ArrayAdapter adapter;
    ArrayList<String> rules = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        rules.add("1- Respect each other.");
        rules.add("2- Don't use abusive / bad language.");
        rules.add("3- Spamming is not allowed.");
        rules.add("4- Avoid irrelevant conversation.");
        rules.add("5- We are not responsible for any inappropriate use of the chat service.");

        editText = (EditText) findViewById(R.id.editText2);
        button = (Button) findViewById(R.id.button3);

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

        listView = findViewById(R.id.rules);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, rules);
        listView.setAdapter(adapter);
    }

    public void enterClicked(View view) {

        String name = editText.getText().toString();
        if(name.equals("")) {
            Toast.makeText(this, "Please, enter a valid name!", Toast.LENGTH_SHORT).show();
            return;

        } else {
            SharedPreferences mPrefs = this.getSharedPreferences("com.zuhair.zuhair.realscholarshipapp", MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            prefsEditor.putString("chatName", name);
            prefsEditor.commit();

            Intent intent = new Intent(this, ChatList.class);
            intent.putExtra("name", name);
            startActivity(intent);
            finish();
        }
    }
}
