package com.example.chqns022.androidassignment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.chqns022.androidassignment.ui.marker.MarkerFragment;

public class MarkerActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new MarkerFragment();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MarkerActivity.this, MarkerFragment.getPreviousPage() == 1 ? MapsFragment.class : FavouriteListActivity.class);
        startActivity(intent);
        finish();

    }
}
