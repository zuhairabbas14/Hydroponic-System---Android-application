package com.zuhair.zuhair.realscholarshipapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements RewardedVideoAdListener {

    Button searchByCountry;
    Button bookmarks;
    int copyCount = 0;
    static RewardedVideoAd mAd;
    static InterstitialAd mInterstitialAd;
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        notificationManager = NotificationManagerCompat.from(this);
        sendOnChannel1();

        /** ADS CONFIGURATION STARTS HERE **/

        MobileAds.initialize(this, "ca-app-pub-1527451245058524~7178584460");
        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);
        getAd();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1527451245058524/1406736237");

        /** ADS CONFIGURATION ENDS HERE **/

        searchByCountry = (Button) findViewById(R.id.searchByCountry);
        bookmarks = (Button) findViewById(R.id.bookmarksButton);

        SharedPreferences mPrefs = getApplicationContext().getSharedPreferences("com.zuhair.zuhair.realscholarshipapp", Context.MODE_PRIVATE);

        Type type = new TypeToken<ArrayList<ScholarshipData>>(){}.getType();
        String json = mPrefs.getString("bookmarkObject", "");
        Gson gson = new Gson();
        List<ScholarshipData> objectlist = gson.fromJson(json, type);

        if(objectlist != null){
            if(copyCount == 0){
                for(int x = 0; x < objectlist.size(); x++){
                    MailAdapter.list.add(objectlist.get(x));
                    if(x == objectlist.size()-1){
                        copyCount +=1;
                    }
                }
            }
        }
    }

    public void searchByCountryClicked(View view){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void bookmarksClicked(View view){

        Intent intent = new Intent(this, BookmarksActivity.class);
        startActivity(intent);
    }

    public void shareButton(View view){

        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.image_click));
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "My Scholarship App");
        intent.putExtra(Intent.EXTRA_TEXT, "Hey! looking for scholarships? My Scholarship app helps you find the perfect scholarship for you and its completely free!\nGet it on Google Play: \n\n" + "http://play.google.com/store/apps/details?id=" + getPackageName());
        startActivity(Intent.createChooser(intent, "My Scholarship App"));
    }

    public void chatClicked(View view) {

        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.image_click));
        SharedPreferences mPrefs = this.getSharedPreferences("com.zuhair.zuhair.realscholarshipapp", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        if(mPrefs.contains("chatName")){
            Intent intent = new Intent(this, ChatList.class);
            startActivity(intent);

        } else {
            Intent intent = new Intent(this, ChatActivity.class);
            startActivity(intent);

        }
    }

    public void rateClicked(View view) {

        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.image_click));

        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + getPackageName())));
        }
        catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    public void sendOnChannel1() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 1);

        if(calendar.before(Calendar.getInstance())){ // if it's in the past, increment
            calendar.add(Calendar.DATE, 1);
        }

        Intent intent1 = new Intent(Main2Activity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Main2Activity.this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) Main2Activity.this.getSystemService(Main2Activity.this.ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    /** VIDEO ADS METHOD STARTS HERE **/

    public static void loadAd(){

        if (mAd.isLoaded()) {
            mAd.show();
        }
    }

    public static void getAd(){
        mAd.loadAd("ca-app-pub-1527451245058524/9980841773",
                new AdRequest.Builder().build());
    }

    @Override
    public void onRewardedVideoAdLoaded() {
    //    Toast.makeText(this, "The ad has loaded!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }
}
