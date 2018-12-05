package com.example.chqns022.androidassignment.ui.marker;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chqns022.androidassignment.Favourite;
import com.example.chqns022.androidassignment.FavouriteListFragment;
import com.example.chqns022.androidassignment.FirebaseDB;
import com.example.chqns022.androidassignment.MapsFragment;
import com.example.chqns022.androidassignment.Marker;
import com.example.chqns022.androidassignment.MarkerActivity;
import com.example.chqns022.androidassignment.R;

import java.util.ArrayList;

public class MarkerFragment extends Fragment {

    private static int previousPage = 0, curPos = 0;
    private static Favourite curFavourite = new Favourite();
    private static ArrayList<String> activitiesList = new ArrayList<>();
    private static ArrayList<String> helpList = new ArrayList<>();
    private static Marker curMarker = new Marker();
    private static boolean isFromMarker = false, isFromFavourites = false;
    private static ArrayList<Boolean> isMarkerFavourite = new ArrayList<>();
    private FirebaseDB db = new FirebaseDB();

    private TextView title, latlngTitle, latlngText, activitiesTitle, helptitle;
    private RecyclerView helpListRecyclerView, activitiesListRecyclerView;

    private RatingBar mRatingBar;
    private Button favButton;

    public static void setIsMarkerFavourite(ArrayList<Boolean> list){
        isMarkerFavourite.clear();
        isMarkerFavourite.addAll(list);
    }

    public static ArrayList<Boolean> getIsMarkerFavourite(){
        return isMarkerFavourite;
    }

    public static boolean isIsFromMarker() {
        return isFromMarker;
    }

    public static void setIsFromMarker(boolean isFromMarker) {
        MarkerFragment.isFromMarker = isFromMarker;
    }

    public static boolean isIsFromFavourites() {
        return isFromFavourites;
    }

    public static void setIsFromFavourites(boolean isFromFavourites) {
        MarkerFragment.isFromFavourites = isFromFavourites;
    }

    public static int getCurPos() {
        return curPos;
    }

    public static void setCurPos(int curPos) {
        MarkerFragment.curPos = curPos;
    }

    public static Marker getCurMarker(){
        return curMarker;
    }

    public static void setCurMarker(Marker marker){
        curMarker.setLocation(marker.getLocation());
        curMarker.setLocation2(marker.getLocation2());
        curMarker.setActivities(marker.getActivities());
        curMarker.setTitle(marker.getTitle());
        curMarker.setRating(marker.getRating());
        curMarker.setHelpNeeded(marker.isHelpNeeded());
        curMarker.setHelp(marker.getHelp());
        curMarker.setId(marker.getId());
    }

    public static void setCurFavourite(Favourite newFavourite){
        curFavourite.setMarker(newFavourite.getMarker());
    }

    public static void setPreviousPage(int page){
        previousPage = page;
    }

    public static int getPreviousPage(){
        return previousPage;
    }

    @Override
    public void onStart() {
        super.onStart();

        db.checkIfMarkerisFavourite(curMarker, FirebaseDB.getCurrentUser().getEmail(), isMarkerFavourite, getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.marker_fragment, container, false);

        mRatingBar = view.findViewById(R.id.rating);
        title = view.findViewById(R.id.marker_fragment_title);
        latlngTitle = view.findViewById(R.id.latlngTitle);
        latlngText = view.findViewById(R.id.latlngtext);
        activitiesTitle = view.findViewById(R.id.activities_title);
        helptitle = view.findViewById(R.id.help_title);
        favButton = view.findViewById(R.id.add_to_fav_btn);

        Log.d("curPos", "" + curPos);

        if(MarkerFragment.isIsFromFavourites()){
            Log.d("isFromFavourites", "yes");
            MarkerFragment.setCurMarker(FavouriteListFragment.getFavourites().get(curPos).getMarker());
        }

        else if(MarkerFragment.isIsFromMarker()){
            Log.d("isFromMarker", "yes");
            MarkerFragment.setCurMarker(MapsFragment.getMarkerList().get(curPos));
        }

        Log.d("isNotFromBoth", "yes");

        Log.d("Marker MarkerFragment", "" + curMarker);
        Log.d("Marker MarkerFragment", "" + getCurPos());
        Log.d("Marker MarkerFragment", "" + getPreviousPage());
        Log.d("Marker MarkerFragment", "" + curMarker.getTitle());
        Log.d("Marker MarkerFragment", "" + curMarker.getId());
        Log.d("Marker MarkerFragment", "" + curMarker.getLocation());


        title.setText(curMarker.getTitle() == null ?  "Title unavailable" : curMarker.getTitle());
        latlngText.setText(curMarker.getLocation() == null ? "Location unavailable" :  " [ " + curMarker.getLocation().getLatitude() + ", " + curMarker.getLocation().getLongitude() + "]");

        mRatingBar.setMax(5);
        mRatingBar.setNumStars(5);
        mRatingBar.setRating(curMarker == null ? 0 :  curMarker.getRating());

        if(curMarker != null){
            if(!curMarker.isHelpNeeded()) helptitle.setText("Help currently not needed");
            if(curMarker.getActivities().size() == 0) activitiesTitle.setText("Activities currently unavailable");
            if(curMarker.getLocation() == null) favButton.setEnabled(false);
        }

        else{
            favButton.setEnabled(true);
        }

        helpListRecyclerView = view.findViewById(R.id.help_list_section);

        activitiesListRecyclerView = view.findViewById(R.id.activities_list_section);

        updateUI();

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("curMarker", "" + curMarker.getId());
                boolean isFav;
                if(getIsMarkerFavourite().size() == 0){
                    isFav = false;
                }
                else{
                    isFav = getIsMarkerFavourite().get(0);
                }
                
                Log.d("isFav", "" + isFav);
                String toast_msg = isFav ? "Marker already added as favourite" : "Adding to favourite...";
                Toast.makeText(getContext(),
                        toast_msg,
                        Toast.LENGTH_SHORT).show();

                if(!isFav) {

                    db.addFavouriteToDB(MarkerFragment.getCurMarker(), FirebaseDB.getCurrentUser().getEmail(), getActivity());
                }

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void updateUI(){

        helpListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        helpListRecyclerView.setAdapter(new HelpAdapter(curMarker.getHelp()));

        activitiesListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        activitiesListRecyclerView.setAdapter(new ActivitiesAdapter());

    }

    private class HelpHolder extends RecyclerView.ViewHolder{
        private TextView helpText;

        private HelpHolder(View view){
            super(view);
            helpText = (TextView) view;
        }
    }

    private class HelpAdapter extends RecyclerView.Adapter<HelpHolder>{

        private ArrayList<String> adapterHelpList = new ArrayList<>();

        private HelpAdapter(ArrayList<String> helps){
            adapterHelpList = helps;
        }

        @NonNull
        @Override
        public HelpHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
            return new HelpHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HelpHolder helpHolder, int i) {
            String help = curMarker.getHelp().get(i);
            helpHolder.helpText.setText(help);
        }

        @Override
        public int getItemCount() {
            return curMarker.getHelp().size();
        }
    }

    private class ActivitiesHolder extends RecyclerView.ViewHolder{

        private TextView activityText;

        public ActivitiesHolder(@NonNull View itemView) {
            super(itemView);
            activityText = (TextView) itemView;
        }
    }

    private class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesHolder>{

        @NonNull
        @Override
        public ActivitiesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
            return new ActivitiesHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ActivitiesHolder activitiesHolder, int i) {
            String activity = curMarker.getActivities().get(i);
            activitiesHolder.activityText.setText(activity);
        }

        @Override
        public int getItemCount() {
            return curMarker.getActivities().size();
        }
    }
}
