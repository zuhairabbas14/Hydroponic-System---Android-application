package com.zuhair.zuhair.realscholarshipapp;

import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        ArrayList<String> tips = new ArrayList<>();
        tips.add("Tip: Apply as early as possible.");
        tips.add("Tip: Spend a good amount of time on your motivation letter.");
        tips.add("Tip: Get someone else to read your application.");
        tips.add("Tip: Manage your time well.");
        tips.add("Tip: Don't take any chances, Apply everywhere!");
        tips.add("Tip: Start the scholarship search process sooner than later.");
        tips.add("Tip: Apply for scholarships in different categories.");
        tips.add("Tip: Include letters of recommendation.");
        tips.add("Tip: Don’t count out scholarships with lower award amounts.");
        tips.add("Tip: Don’t become discouraged if you don’t win right away.");
        tips.add("Tip: Don’t avoid scholarships that require essays.");
        tips.add("Tip: Apply early and apply often!");
        tips.add("Tip: Stay organized, keeping deadlines in mind!");
        tips.add("Tip: Make a scholarship application schedule and stick to it.");
        tips.add("Tip: Beware of Scholarship and Financial Aid Scams.");
        tips.add("Tip: Carefully read the scholarship criteria and tailor each application accordingly.");
        tips.add("Tip: Understand the scholarship criteria and can clearly see why you are a good candidate.");
        tips.add("Tip: Proofread and repeat your application draft.");
        tips.add("Tip: Be unique, be you.");
        tips.add("Tip: Make sure you know everything about the award before you apply for it.");
        tips.add("Tip: Also Apply for smaller scholarships.");
        tips.add("Tip: Submit scholarship applications early.");
        tips.add("Tip: Follow your passion.");
        tips.add("Tip: The more applications you submit, the greater your chances are of winning scholarships.");
        tips.add("Tip: Look into Scholarships in Any Field of Study and Any Country.");
        tips.add("Tip: Take note of Annual Scholarship Programs.");
        tips.add("Tip: Be neat, because neatness matters.");
        tips.add("Tip: Seek Out Similar Applications.");
        tips.add("Tip: Read the requirements carefully.");
        tips.add("Tip: Plan Letters of Recommendation in Advance.");
        tips.add("Tip: Keep a record of all the documents.");
        tips.add("Tip: Be confident.");
        tips.add("Tip: Be Prepared to Work Hard for Study Abroad Scholarships.");
        tips.add("Tip: Make a List of Everything You Qualify for.");
        tips.add("Tip: Understand the Organization's Goals and Values");
        tips.add("Tip: Understand How They Evaluate Candidates.");
        tips.add("Tip: Also Consider Studying Critical Languages and Going to Less Commonly-Visited Countries.");
        tips.add("Tip: Double Check Eligibility and Application Requirements.");

        Random random = new Random();
        int low = 0;
        int high = tips.size();
        int r = random.nextInt(high-low) + low;

        Toast.makeText(context, tips.get(r), Toast.LENGTH_LONG).show();
    }
}