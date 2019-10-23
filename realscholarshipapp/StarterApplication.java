package com.zuhair.zuhair.realscholarshipapp;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;


public class StarterApplication extends App {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("52ba79e5a15fe2ef84e4afc693a988621bb9f93e")
                .clientKey("3995aa725e12a53380331c778c0f7478508fd25d")
                .server("http://18.224.198.243:80/parse/")
                .build()
        );


        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

    }
}
