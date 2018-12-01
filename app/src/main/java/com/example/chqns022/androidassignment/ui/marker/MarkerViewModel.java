package com.example.chqns022.androidassignment.ui.marker;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.type.LatLng;

public class MarkerViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<String> title = new MutableLiveData<>();
    private MutableLiveData<LatLng> latlng = new MutableLiveData<>();

    public void setMarkerTitle(String title){
        this.title.setValue(title);
    }

    public LiveData<String> getMarkerTitle(){
        return this.title;
    }

    public String getMarkerTitleString(){
        return this.title.getValue();
    }

    public void setLatLng(LatLng latLng){
        this.latlng.setValue(latLng);
    }

    public LiveData<LatLng> getLatLng(){
        return this.latlng;
    }

    public LatLng getLatLngValue(){
        return this.latlng.getValue();
    }
}
