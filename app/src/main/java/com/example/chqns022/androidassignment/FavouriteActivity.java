package com.example.chqns022.androidassignment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class FavouriteActivity extends SingleFragmentActivity {

    //private static ArrayList<Marker> favouritesList = new ArrayList<>();

    @Override
    protected Fragment createFragment(){
        return new FavouriteFragment();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FavouriteActivity.this, HomeMenu.class);
        startActivity(intent);
        finish();
    }

    public static void makeToast(String msg){

    }
}
