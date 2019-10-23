package com.zuhair.zuhair.realscholarshipapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookmarksActivity extends AppCompatActivity {

    static RecyclerView mRecyclerView;
    static List<ScholarshipData> mScholarshipData;
    static MailAdapter mMailAdapter;
    static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        mContext = getApplicationContext();

        if(!Main2Activity.mAd.isLoaded()){
            Main2Activity.getAd();
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(BookmarksActivity.this,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(BookmarksActivity.this,
                DividerItemDecoration.VERTICAL));

        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        getListFromSharedPreference(getApplicationContext());


    }

    public void chatFabClicked(View view) {
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

    public static void getListFromSharedPreference(Context context) {
        mScholarshipData = new ArrayList<>();
        ScholarshipData mEmail;
        SharedPreferences mPrefs = context.getSharedPreferences("com.zuhair.zuhair.realscholarshipapp", Context.MODE_PRIVATE);

        Type type = new TypeToken<ArrayList<ScholarshipData>>(){}.getType();
        String json = mPrefs.getString("bookmarkObject", "");
        Gson gson = new Gson();
        List<ScholarshipData> objectlist = gson.fromJson(json, type);

        if(objectlist != null){
            for (ScholarshipData object: objectlist) {

                mEmail = new ScholarshipData(object.getmScholarshipName(), object.getmUniName(), object.getmCountry(), object.getmDeadline(), object.geturlscholarship());
                mScholarshipData.add(mEmail);
            }
        }


        MailAdapter mMailAdapter = new MailAdapter(context, mScholarshipData);
        mRecyclerView.setAdapter(mMailAdapter);
    }

    public void clearlistClicked(View view){

        SharedPreferences mPrefs = getApplicationContext().getSharedPreferences("com.zuhair.zuhair.realscholarshipapp", Context.MODE_PRIVATE);
        while(mPrefs.contains("bookmarkObject")){
            mPrefs.edit().remove("bookmarkObject").apply();
        }

        mScholarshipData = new ArrayList<>();

        MailAdapter.list.clear();
        mMailAdapter = new MailAdapter(BookmarksActivity.this, mScholarshipData);
        mRecyclerView.setAdapter(mMailAdapter);
    }

    public static void onStarPressedAgain(List<ScholarshipData> objectlist){

        mScholarshipData = new ArrayList<>();
        ScholarshipData mEmail;

        MailAdapter.list.clear();

        if(objectlist != null){
            for (ScholarshipData object: objectlist) {

                mEmail = new ScholarshipData(object.getmScholarshipName(), object.getmUniName(), object.getmCountry(), object.getmDeadline(), object.geturlscholarship());
                mScholarshipData.add(mEmail);
                MailAdapter.list.add(mEmail);
            }
        }
        MailAdapter mMailAdapter = new MailAdapter(mContext, mScholarshipData);

        if(mRecyclerView != null){
            mRecyclerView.setAdapter(mMailAdapter);
        }
    }

}
