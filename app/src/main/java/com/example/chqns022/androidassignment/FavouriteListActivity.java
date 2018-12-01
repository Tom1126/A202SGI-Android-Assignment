package com.example.chqns022.androidassignment;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class FavouriteListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new FavouriteListFragment();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FavouriteListActivity.this, HomeMenu.class);
        startActivity(intent);
        finish();
    }
}
