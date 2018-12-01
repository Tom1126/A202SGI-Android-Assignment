package com.example.chqns022.androidassignment;

import com.google.firebase.firestore.DocumentReference;

public class Favourite {
    private Marker mMarker = new Marker();
    private String userEmail = "";
    private boolean setAtHomeScreen = false;
    private String id = "";
    private DocumentReference marker;

    public void setMarker(DocumentReference marker) {
        this.marker = marker;
    }

    public String getId(){
        return id;
    }

    public void setId(String newId){
        id = newId;
    }

    public Marker getMarker() {
        return mMarker;
    }

    public void setMarker(Marker marker) {
        //this.mMarker = mMarker;
        this.mMarker.setId(marker.getId());
        this.mMarker.setActivities(marker.getActivities());
        this.mMarker.setHelp(marker.getHelp());
        this.mMarker.setLocation(marker.getLocation());
        this.mMarker.setHelpNeeded(marker.isHelpNeeded());
        this.mMarker.setTitle(marker.getTitle());
        this.mMarker.setRating(marker.getRating());
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public boolean isSetAtHomeScreen() {
        return setAtHomeScreen;
    }

    public void setSetAtHomeScreen(boolean setAtHomeScreen) {
        this.setAtHomeScreen = setAtHomeScreen;
    }
}
