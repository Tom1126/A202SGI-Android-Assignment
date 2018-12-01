package com.example.chqns022.androidassignment;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.chqns022.androidassignment.ui.marker.MarkerFragment;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    private static ArrayList<Favourite> curFavList = new ArrayList<>();
    private static final String TEXT_ONCLICK_TAG = "TEXTONCLICKTAG";

    public static ArrayList<Favourite> getCurFavList() {
        return curFavList;
    }

    public static void setCurFavList(ArrayList<Favourite> curFavList) {
        NewAppWidget.curFavList.clear();
        NewAppWidget.curFavList.addAll(curFavList);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        boolean gotLocation = false;

        Log.d("list size", getCurFavList().size() + "");
        if(getCurFavList().size() != 0){
            Log.d("pass 0 size", "yes");
            if(getCurFavList().get(0) != null){
                gotLocation = true;
            }
        }

        String text = curFavList.size() == 0 ? "Title unavailable" : "" + curFavList.get(0).getMarker().getTitle();
        String locationString =  gotLocation ? "[ " + curFavList.get(0).getMarker().getLocation().getLatitude() + ", " + curFavList.get(0).getMarker().getLocation().getLongitude() +"]" : "Location unavailable";

        views.setTextViewText(R.id.appwidget_text, text);
        views.setTextViewText(R.id.appwidget_location, locationString);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        Intent intent = new Intent(context, MarkerActivity.class);
        MarkerFragment.setCurMarker(getCurFavList().get(0).getMarker());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,0);

        views.setOnClickPendingIntent(R.id.widget, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetIds, views);
        //views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
        //views.setOnClickPendingIntent(R.id.appwidget_location, pendingIntent);

        //getPendingSelfIntent(context, TEXT_ONCLICK_TAG);

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if(TEXT_ONCLICK_TAG.equals(intent.getAction())){
            Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show();
            Intent newIntent = new Intent(context, MarkerFragment.class);
            context.startActivity(newIntent);
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action){
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

}

