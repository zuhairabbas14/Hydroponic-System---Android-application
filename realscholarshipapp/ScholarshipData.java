package com.zuhair.zuhair.realscholarshipapp;

public class ScholarshipData {

    private String mScholarshipName;
    private String mUniName;
    private String mCountry;
    private String mDeadline;
    private String urlscholarship;

    public ScholarshipData(String mScholarshipName, String mUniName, String mCountry, String mDeadline, String urlscholarship) {
        this.mScholarshipName = mScholarshipName;
        this.mUniName = mUniName;
        this.mCountry = mCountry;
        this.mDeadline = mDeadline;
        this.urlscholarship = urlscholarship;
    }

    public String getmScholarshipName() {
        return mScholarshipName;
    }

    public String getmUniName() {
        return mUniName;
    }

    public String getmCountry() {
        return mCountry;
    }

    public String getmDeadline() {
        return mDeadline;
    }

    public String geturlscholarship() {
        return urlscholarship;
    }

}

