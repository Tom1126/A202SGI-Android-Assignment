package com.example.chqns022.androidassignment;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class Marker {
    private String title;
    private LatLng location2 = new LatLng(1, 1);
    private GeoPoint location;
    private boolean helpNeeded;
    private long rating;
    private ArrayList<String> help = new ArrayList<>();
    private ArrayList<String> activities = new ArrayList<>();
    private String id;


    public String getId(){
        return id;
    }

    public void setId(String newId){
        id = newId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isHelpNeeded() {
        return helpNeeded;
    }

    public void setHelpNeeded(boolean helpNeeded) {
        this.helpNeeded = helpNeeded;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public ArrayList<String> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<String> activities) {
        this.activities.clear();
        this.activities.addAll(activities);
    }

    public LatLng getLocation2() {
        return location2;
    }

    public void setLocation2(LatLng location) {
        this.location2 = location;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public ArrayList<String> getHelp() {
        return help;
    }

    public void setHelp(ArrayList<String> help) {
        this.help.clear();
        this.help.addAll(help);
    }
}
