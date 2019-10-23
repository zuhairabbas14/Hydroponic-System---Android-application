package com.zuhair.zuhair.realscholarshipapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.parse.ParseAnalytics;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String[] countries = new String[]{"Africa", "Australia", "Austria", "Belgium", "Canada", "Carribean", "China", "Costa Rica", "Czech Republic", "Denmark", "Finland", "France", "Germany", "Greece", "Grenada", "Honk Kong", "Hungary", "Ireland", "Italy", "Japan", "Korea", "List of Countries", "Macau", "Malaysia", "Mexico", "Netherlands", "New Zealand", "Norway", "Philippines", "Poland", "Singapore", "Slovakia", "South Africa", "Southeast Asia", "Spain", "Sweden", "Switzerland", "Taiwan", "Thailand", "Tunisia", "Turkey", "UAE", "UK", "USA"};
    private static final String[] degrees = new String[]{"undergraduate", "postgraduate", "phd"};
    private static final String[] majors = new String[]{"Agriculture & Forestry", "Development", "Economics & Finance", "Education", "Engineering", "Environment", "Health", "Human development", "Information technology", "Journalism", "Law & public policy", "MBA", "Medicine", "Natural science", "Science & technology", "Social sciences"};
    RecyclerView mRecyclerView;
    List<ScholarshipData> mScholarshipData;
    AutoCompleteTextView autoCompleteDegree;
    AutoCompleteTextView autoCompleteCountry;
    AutoCompleteTextView autoCompleteMajor;
    ImageView imageViewDegree;
    ImageView imageViewCountry;
    ImageView imageViewMajor;
    ProgressBar progressBar;
    boolean clickable;
    Button find;
    private AdView mBannerAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* ADS START HERE ... **/

        mBannerAd = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mBannerAd.loadAd(adRequest);

        if(!Main2Activity.mAd.isLoaded()){
            Main2Activity.getAd();
        }

        /* ADS END HERE ... **/


        find = (Button) findViewById(R.id.button);
        progressBar = (ProgressBar) findViewById(R.id.progressBarCircular);
        progressBar.setVisibility(View.GONE);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        autoCompleteDegree = (AutoCompleteTextView) findViewById(R.id.actv1);
        autoCompleteCountry = (AutoCompleteTextView) findViewById(R.id.actv2);
        autoCompleteMajor = (AutoCompleteTextView) findViewById(R.id.actv3);

        imageViewDegree = (ImageView) findViewById(R.id.imageDownArrow);
        imageViewCountry = (ImageView) findViewById(R.id.imageDownArrow2);
        imageViewMajor = (ImageView) findViewById(R.id.imageDownArrow3);

        autoCompleteCountry.setThreshold(1);
        autoCompleteDegree.setThreshold(1);
        autoCompleteMajor.setThreshold(1);

        ArrayAdapter<String> adapter_country = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, countries);
        autoCompleteCountry.setAdapter(adapter_country);
        ArrayAdapter<String> adapter_degree = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, degrees);
        autoCompleteDegree.setAdapter(adapter_degree);
        ArrayAdapter<String> adapter_major = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, majors);
        autoCompleteMajor.setAdapter(adapter_major);

        imageViewDegree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteDegree.showDropDown();
            }
        });

        imageViewCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteCountry.showDropDown();
            }
        });

        imageViewMajor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteMajor.showDropDown();
            }
        });

        autoCompleteCountry.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    // on focus off
                    String str = autoCompleteCountry.getText().toString();

                    ListAdapter listAdapter = autoCompleteCountry.getAdapter();
                    for(int i = 0; i < listAdapter.getCount(); i++) {
                        String temp = listAdapter.getItem(i).toString();
                        if(str.compareTo(temp) == 0) {
                            return;
                        }
                    }

                    autoCompleteCountry.setText("");

                }
            }
        });

        autoCompleteDegree.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    // on focus off
                    String str = autoCompleteDegree.getText().toString();

                    ListAdapter listAdapter = autoCompleteDegree.getAdapter();
                    for(int i = 0; i < listAdapter.getCount(); i++) {
                        String temp = listAdapter.getItem(i).toString();
                        if(str.compareTo(temp) == 0) {
                            return;
                        }
                    }

                    autoCompleteDegree.setText("");

                }
            }
        });

        autoCompleteMajor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    // on focus off
                    String str = autoCompleteMajor.getText().toString();

                    ListAdapter listAdapter = autoCompleteMajor.getAdapter();
                    for(int i = 0; i < listAdapter.getCount(); i++) {
                        String temp = listAdapter.getItem(i).toString();
                        if(str.compareTo(temp) == 0) {
                            return;
                        }
                    }

                    autoCompleteMajor.setText("");

                }
            }
        });

        autoCompleteDegree.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {

                autoCompleteMajor.setText("");

            }
        });

        autoCompleteCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {

                autoCompleteMajor.setText("");

            }
        });

        autoCompleteMajor.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {

                autoCompleteDegree.setText("");
                autoCompleteCountry.setText("");
            }
        });


        /**
         *
         * RECYCLE VIEW PART STARTS HERE.....!!!!!!!!!!
         */

        mRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(MainActivity.this,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this,
                DividerItemDecoration.VERTICAL));

        mRecyclerView.setLayoutManager(mLinearLayoutManager);

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

    public void findButtonClicked(View view){

        mBannerAd.setVisibility(View.GONE);

        if(!Main2Activity.mAd.isLoaded() && !Main2Activity.mInterstitialAd.isLoaded()){
            Main2Activity.mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }

        if(clickable){
            clickable = false;
            find.setEnabled(false);
        }

        String countryChosen = autoCompleteCountry.getText().toString();
        String degreeChosen = autoCompleteDegree.getText().toString();
        String majorChosen = autoCompleteMajor.getText().toString();

        if((countryChosen.equals("") && degreeChosen.equals("") && majorChosen.equals(""))){
            Toast.makeText(this, "Please, fill the fields above!", Toast.LENGTH_SHORT).show();
            find.setEnabled(true);
            clickable = true;
            return;
        }

        mRecyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        if(!majorChosen.equals("")){

            autoCompleteCountry.setText("searching by major..");
            autoCompleteDegree.setText("searching by major..");

            mScholarshipData = new ArrayList<>();

            ParseQuery<ParseObject> query = ParseQuery.getQuery("MajorLevel");

            query.whereEqualTo("level", majorChosen);


            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    if (e == null && objects != null) {

                        mScholarshipData.clear();

                        for (ParseObject object : objects) {

                            ScholarshipData mEmail;
                            mEmail = new ScholarshipData(object.getString("title"), object.getString("institute"), object.getString("country"), object.getString("deadline"), object.getString("title_link"));
                            mScholarshipData.add(mEmail);
                            MailAdapter mMailAdapter = new MailAdapter(MainActivity.this, mScholarshipData);
                            mRecyclerView.setAdapter(mMailAdapter);
                        }

                        if(mScholarshipData.size() == 0){
                            mScholarshipData.clear();
                            MailAdapter mMailAdapter = new MailAdapter(MainActivity.this, mScholarshipData);
                            mRecyclerView.setAdapter(mMailAdapter);
                        }

                        Toast.makeText(MainActivity.this, String.valueOf(mScholarshipData.size()) + " scholarships found!", Toast.LENGTH_SHORT).show();



                    } else {

                        Toast.makeText(MainActivity.this, "Please, check your internet and try agan!", Toast.LENGTH_SHORT).show();
                    }

                    progressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    clickable = true;
                    find.setEnabled(true);
                }
            });

            autoCompleteCountry.setText("");
            autoCompleteDegree.setText("");
            ParseAnalytics.trackAppOpenedInBackground(getIntent());

        }

        else {

            autoCompleteMajor.setText("");

            mScholarshipData = new ArrayList<>();

            ParseQuery<ParseObject> query = ParseQuery.getQuery("DegreeLevel");

            if(degreeChosen.equals("")){
                query.whereContains("country", countryChosen);
            } else {
                query.whereContains("country", countryChosen).whereEqualTo("level", degreeChosen);
            }

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    if (e == null && objects != null) {

                        mScholarshipData.clear();

                        for (ParseObject object : objects) {

                            ScholarshipData mEmail;
                            mEmail = new ScholarshipData(object.getString("title"), object.getString("institute"), object.getString("country"), object.getString("deadline"), object.getString("title_link"));
                            mScholarshipData.add(mEmail);
                            MailAdapter mMailAdapter = new MailAdapter(MainActivity.this, mScholarshipData);
                            mRecyclerView.setAdapter(mMailAdapter);
                        }

                        if(mScholarshipData.size() == 0){
                            mScholarshipData.clear();
                            MailAdapter mMailAdapter = new MailAdapter(MainActivity.this, mScholarshipData);
                            mRecyclerView.setAdapter(mMailAdapter);
                        }

                        Toast.makeText(MainActivity.this, String.valueOf(mScholarshipData.size()) + " scholarships found!", Toast.LENGTH_SHORT).show();


                    } else {

                        Toast.makeText(MainActivity.this, "Please, check your internet and try agan!", Toast.LENGTH_SHORT).show();
                    }

                    progressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    find.setEnabled(true);
                    clickable = true;

                }
            });

            ParseAnalytics.trackAppOpenedInBackground(getIntent());
        }
    }
}
