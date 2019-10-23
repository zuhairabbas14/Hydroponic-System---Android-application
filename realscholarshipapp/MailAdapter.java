package com.zuhair.zuhair.realscholarshipapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class MailAdapter extends RecyclerView.Adapter<MailViewHolder> {

    private List<ScholarshipData> mScholarshipData;
    RecyclerView mRecyclerView;
    private Context mContext;
    static final ArrayList<ScholarshipData> list = new ArrayList<>();

    public MailAdapter(Context mContext, List<ScholarshipData> mScholarshipData) {
        this.mScholarshipData = mScholarshipData;
        this.mContext = mContext;
    }

    @Override
    public MailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_mail_item,
                parent, false);
        return new MailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MailViewHolder holder, final int position) {

        final int url_position = position;

        holder.mScholarshipName.setText(mScholarshipData.get(position).getmScholarshipName());
        holder.mUniName.setText(mScholarshipData.get(position).getmUniName());
        holder.mCountry.setText(mScholarshipData.get(position).getmCountry());
        holder.mDeadline.setText(mScholarshipData.get(position).getmDeadline());

        // CHECKING ID ITEM IS BOOKMARKED SHOW YELLOW

        SharedPreferences mPrefs = mContext.getSharedPreferences("com.zuhair.zuhair.realscholarshipapp", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Type type = new TypeToken<ArrayList<ScholarshipData>>() {
        }.getType();
        String json = mPrefs.getString("bookmarkObject", "");
        Gson gson = new Gson();
        List<ScholarshipData> objectlist = gson.fromJson(json, type);
        boolean isPresent = false;

        if (objectlist != null) {
            if (objectlist.size() > 0) {

                for (int x = 0; x < objectlist.size(); x++) {
                    if (objectlist.get(x).getmScholarshipName().equals(mScholarshipData.get(position).getmScholarshipName())) {
                        isPresent = true;
                    }
                }
            }
        }
        if(isPresent){
            holder.mFavorite.setColorFilter(ContextCompat.getColor(mContext,
                    R.color.colorOrange));
        } else {
            holder.mFavorite.clearColorFilter();
        }


        holder.mFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.mFavorite.getColorFilter() != null) {
                    holder.mFavorite.clearColorFilter();

                    SharedPreferences mPrefs = mContext.getSharedPreferences("com.zuhair.zuhair.realscholarshipapp", MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    Type type = new TypeToken<ArrayList<ScholarshipData>>() {
                    }.getType();
                    String json = mPrefs.getString("bookmarkObject", "");
                    Gson gson = new Gson();
                    List<ScholarshipData> objectlist = gson.fromJson(json, type);
                    boolean isPresent = false;

                    if (objectlist != null) {
                        if (objectlist.size() > 0) {

                            for (int x = 0; x < objectlist.size(); x++) {
                                if (objectlist.get(x).getmScholarshipName().equals(mScholarshipData.get(position).getmScholarshipName())) {
                                    isPresent = true;
                                    objectlist.remove(x);
                                }
                            }
                        }
                    }

                    while (mPrefs.contains("bookmarkObject")){
                        mPrefs.edit().remove("bookmarkObject").apply();
                    }
                    gson = new Gson();
                    json = gson.toJson(objectlist);
                    prefsEditor.putString("bookmarkObject", json);
                    prefsEditor.apply();
                    BookmarksActivity.onStarPressedAgain(objectlist);

                    Toast.makeText(mContext, "Bookmark removed!", Toast.LENGTH_SHORT).show();

                } else {

                    // STORING BOOKMARKS CODE STARTS HERE

                    SharedPreferences mPrefs = mContext.getSharedPreferences("com.zuhair.zuhair.realscholarshipapp", MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    Type type = new TypeToken<ArrayList<ScholarshipData>>() {
                    }.getType();
                    String json = mPrefs.getString("bookmarkObject", "");
                    Gson gson = new Gson();
                    List<ScholarshipData> objectlist = gson.fromJson(json, type);
                    boolean isPresent = false;

                    if (objectlist != null) {
                        if (objectlist.size() > 0) {

                            for (int x = 0; x < objectlist.size(); x++) {
                                if (objectlist.get(x).getmScholarshipName().equals(mScholarshipData.get(position).getmScholarshipName())) {
                                    isPresent = true;
                                }
                            }
                        }
                    }

                        if (!isPresent) {
                            list.add(mScholarshipData.get(position));
                            gson = new Gson();
                            json = gson.toJson(list);
                            prefsEditor.putString("bookmarkObject", json);
                            prefsEditor.apply();
                            holder.mFavorite.setColorFilter(ContextCompat.getColor(mContext,
                                    R.color.colorOrange));
                            Toast.makeText(mContext, "Bookmark saved!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "Already saved!", Toast.LENGTH_SHORT).show();
                        }

                    // ----------------------------------------
                }
            }
        });

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(mContext, DetailActivity.class);
                mIntent.putExtra("sender", holder.mScholarshipName.getText().toString());
                mIntent.putExtra("title", holder.mUniName.getText().toString());
                mIntent.putExtra("details", holder.mCountry.getText().toString());
                mIntent.putExtra("time", holder.mDeadline.getText().toString());
                mIntent.putExtra("link", mScholarshipData.get(url_position).geturlscholarship());
                mContext.startActivity(mIntent);
                if(Main2Activity.mAd.isLoaded()){
                    Main2Activity.mAd.show();
                } else {
                    if(Main2Activity.mInterstitialAd.isLoaded()){
                        Main2Activity.mInterstitialAd.show();
                    }
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mScholarshipData.size();
    }
}

class MailViewHolder extends RecyclerView.ViewHolder {

    TextView mScholarshipName;
    TextView mUniName;
    TextView mCountry;
    TextView mDeadline;
    ImageView mFavorite;
    RelativeLayout mLayout;

    MailViewHolder(View itemView) {
        super(itemView);

        mScholarshipName = itemView.findViewById(R.id.tvScholarshipName);
        mUniName = itemView.findViewById(R.id.tvUniName);
        mCountry = itemView.findViewById(R.id.tvCountry);
        mDeadline = itemView.findViewById(R.id.tvDeadline);
        mFavorite = itemView.findViewById(R.id.ivFavorite);
        mLayout = itemView.findViewById(R.id.layoutList);

    }
}