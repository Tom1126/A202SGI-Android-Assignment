package com.example.chqns022.androidassignment;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chqns022.androidassignment.ui.marker.MarkerFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class FavouriteListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private FavouriteAdapter mFavouriteAdapter;
    private static Favourite curSetAtHomeScreenFav = new Favourite();
    private static ArrayList<Favourite> favourites = new ArrayList<>();
    private FirebaseDB db = new FirebaseDB();


    public static void setAllMarkerHomeScreenFalse(){
        for(int i = 0; i < favourites.size(); i++){
            favourites.get(i).setSetAtHomeScreen(false);
        }
    }

    public static Favourite getCurSetAtHomeScreenFav() {
        return curSetAtHomeScreenFav;
    }

    public static void setCurSetAtHomeScreenFav(Favourite curSetAtHomeScreenFav) {
        FavouriteListFragment.curSetAtHomeScreenFav = curSetAtHomeScreenFav;
    }

    public static void setFavourites(ArrayList<Favourite> favouriteArrayList){
        favourites.clear();
        favourites.addAll(favouriteArrayList);
    }

    public static ArrayList<Favourite> getFavourites(){
        return favourites;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<Favourite> list = NewAppWidget.getCurFavList();
        list.clear();

        for(int i = 0; i < favourites.size(); i++){
            if(favourites.get(i).isSetAtHomeScreen()){
                list.add(favourites.get(i));
                NewAppWidget.setCurFavList(list);
                break;
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_favourite_list, container, false);

        mRecyclerView = view.findViewById(R.id.favourite_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI(){


        mFavouriteAdapter = new FavouriteAdapter(favourites);
        mRecyclerView.setAdapter(mFavouriteAdapter);

    }

    private class FavouriteHolder extends RecyclerView.ViewHolder{
        private TextView mTextView;
        private CheckBox mCheckBox;
        private Button mButton;
        private int mPos;
        private Favourite fav;


        private FavouriteHolder(View itemView){
            super(itemView);
            mTextView = itemView.findViewById(R.id.favourite_title);

            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MarkerFragment.setPreviousPage(2);
                    MarkerFragment.setIsFromFavourites(true);
                    MarkerFragment.setIsFromMarker(false);
                    Log.d("mPos", "" + mPos);
                    MarkerFragment.setCurPos(mPos);
                    MarkerFragment.setCurMarker(favourites.get(mPos).getMarker());

                    db.checkIfMarkerisFavourite(MarkerFragment.getCurMarker(), FirebaseDB.getCurrentUser().getEmail(), MarkerFragment.getIsMarkerFavourite(), getActivity());

                    Intent intent = new Intent(v.getContext(), MarkerActivity.class);
                    startActivity(intent);

                }

            });

            mCheckBox = itemView.findViewById(R.id.favourite_check_box);

            mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!mCheckBox.isChecked()){
                        FirebaseDB db = new FirebaseDB();
                        db.deleteFavouriteFromDB(fav.getId(), getActivity());
                    }

                }
            });

            mButton = itemView.findViewById(R.id.add_to_home_screen_btn);
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String toast_msg =  fav.isSetAtHomeScreen() ? "Favourite already set at home screen" : "Setting favourite to home screen";
                    Toast.makeText(v.getContext(),
                            toast_msg,
                            Toast.LENGTH_SHORT).show();

                    if(!(fav.isSetAtHomeScreen())){
                        setAllMarkerHomeScreenFalse();
                        fav.setSetAtHomeScreen(true);

                        ArrayList<Favourite> curFav = new ArrayList<>();
                        curFav.add(fav);

                        //db.updateMarkerHomeScreenStatus(getCurSetAtHomeScreenFav(), true, getActivity());
                        NewAppWidget.setCurFavList(curFav);

                        //Intent intent = new Intent(getActivity(), NewAppWidget.class);
                        //intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

                        int[] ids = AppWidgetManager.getInstance(getActivity()).getAppWidgetIds(new ComponentName(getActivity(), NewAppWidget.class));
                        //intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                        NewAppWidget widget = new NewAppWidget();
                        widget.onUpdate(getActivity(), AppWidgetManager.getInstance(getActivity()), ids);

                    }

                }

            });

        }

        private void setPos(int newPos){
            mPos = newPos;
        }

        private void bindFavourite(Favourite favourite, int pos){
            fav = favourite;
            mTextView.setText(favourite.getMarker().getTitle());
            mCheckBox.setChecked(true);
            mPos = pos;
        }

    }

    private class FavouriteAdapter extends RecyclerView.Adapter<FavouriteHolder>{
        private ArrayList<Favourite> mFavourites;

        public FavouriteAdapter(ArrayList<Favourite> favourites){
            mFavourites = favourites;
            if(mFavourites.size() == 0){
                NotificationsControl.sendNotifications(getActivity(), "Favourite List Empty", "Try adding some favourites");
            }
        }

        @NonNull
        @Override
        public FavouriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_favourite, parent, false);
            return new FavouriteHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FavouriteHolder holder, int pos){
            Favourite favourite = mFavourites.get(pos);
            holder.setPos(pos);
            holder.bindFavourite(favourite, pos);
        }

        @Override
        public int getItemCount(){
            return mFavourites.size();
        }
    }
}
