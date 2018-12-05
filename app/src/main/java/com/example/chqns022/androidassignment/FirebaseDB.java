package com.example.chqns022.androidassignment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.chqns022.androidassignment.ui.marker.MarkerFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import com.example.chqns022.androidassignment.MainActivity;
import com.google.firebase.firestore.core.Query;

import javax.annotation.Nullable;


public class FirebaseDB {
    private FirebaseFirestore db;
    private static FirebaseAuth mAuth;
    private static FirebaseUser mUser = null;
    private static User currentUser = new User();

    public FirebaseDB(){
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public static User getCurrentUser(){
        return currentUser;
    }

    public void sendForgotPasswordEmail(final String email, final Context context){
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("FirebaseDB", "Reset password email sent");
                            Toast.makeText(context,
                                    "Reset Password Email has been sent. Check your email",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);

                        }
                    }
                });
    }

    public void registerUser(final User user, final Context context){
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("FirebaseDB","create user success");
                            Intent intent = new Intent(context, MainActivity.class);
                            Toast.makeText(context,
                                    "Register Successful",
                                    Toast.LENGTH_SHORT).show();

                            context.startActivity(intent);
                        }
                        else{
                            Log.d("FirebaseDB", "create user failed");

                            Toast.makeText(context,
                                    "Register Failed: " + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signInUser(final User user, final Context context){
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("FirebaseDB", "signInWithEmail:success");
                            mUser = mAuth.getCurrentUser();
                            currentUser.setEmail(user.getEmail());
                            currentUser.setPassword(user.getPassword());

                            NotificationsControl.createToast(context, "Sign In Successfully");
                            Intent intent = new Intent(context, HomeMenu.class);
                            context.startActivity(intent);
                        }
                        else{
                            Log.d("FirebaseDB", "signInWithEmail: failure" + task.getException());
                            NotificationsControl.createToast(context, "Sign In Failed: " + task.getException());
                        }
                    }
                });
    }



    public void getMarkersFromDB(final ArrayList<Marker> markerList, final Context context){

        markerList.clear();

        db.collection("markers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("FirebaseDB Markers", document.getId() + " => " + document.getData());
                                Marker newMarker = document.toObject(Marker.class);

                                newMarker.setId(document.getId());

                                //newMarker.setTitle(document.getString("title"));
                                //newMarker.setLocation(new LatLng(document.getGeoPoint("location").getLatitude(), document.getGeoPoint("location").getLongitude()));

                                markerList.add(newMarker);
                            }

                        } else {
                            Log.w("FirebaseDB"
                                    , "Error getting documents.", task.getException());
                            NotificationsControl.sendNotifications(context, "Getting markers error", "Oops, something went wrong. Please try again later.");
                        }
                    }
                });


    }

    public void userSignOut(Context context){
        mAuth.signOut();
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public FirebaseUser getSignedInUser(){
        return mUser;
    }

    public void getFavouritesFromDB(final ArrayList<Favourite> favourites, String email, final Context context){
        favourites.clear();

        db.collection("favourites")
            .whereEqualTo("userEmail", "" + email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                Log.d("Favourites DB2", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                final Favourite newFavourite = new Favourite();

                                String email = documentSnapshot.getString("userEmail");
                                boolean isSetAtHomeScreen = documentSnapshot.getBoolean("setAtHomeScreen");

                                newFavourite.setUserEmail(email);
                                newFavourite.setSetAtHomeScreen(isSetAtHomeScreen);
                                newFavourite.setId(documentSnapshot.getId());

                                DocumentReference doc = documentSnapshot.getDocumentReference("marker");

                                doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Log.d("MarkerDoc", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                        Marker marker = documentSnapshot.toObject(Marker.class);
                                        marker.setId(documentSnapshot.getId());
                                        Log.d("marker MarkerDoc", marker.getTitle());
                                        Log.d("marker MarkerDoc", "" + marker.getLocation());
                                        Log.d("marker MarkerDoc", "" + marker.getRating());
                                        Log.d("marker MarkerDoc", "" + marker.isHelpNeeded());
                                        Log.d("marker MarkerDoc", "" + marker.getActivities().get(0));
                                        newFavourite.setMarker(marker);
                                        favourites.add(newFavourite);
                                    }
                                });
                            }
                        }
                        else{
                            Log.d("Favourites Db2 Error", "" + task.getException());
                            NotificationsControl.sendNotifications(context, "Getting favourites error", "Oops, something went wrong. Please try again later.");
                        }
                    }
                });
    }



    public void checkIfMarkerisFavourite(final Marker marker, String email, final ArrayList<Boolean> isFav, final Context context){
        isFav.clear();
        MarkerFragment.setCurFavourite(new Favourite());

        db.collection("favourites")
                .whereEqualTo("userEmail", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc : task.getResult()){
                                //final Favourite fav = doc.toObject(Favourite.class);
                                DocumentReference markerRef = doc.getDocumentReference("marker");
                                markerRef.get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()){
                                                    DocumentSnapshot doc2 = task.getResult();
                                                    //Marker curMarker = doc2.toObject(Marker.class);
                                                    String id = doc2.getId();
                                                    Log.d("markerId", "id: " + id);
                                                    Log.d("markerId", "markerId: " + marker.getId());
                                                    if(marker.getId().equals(id)){
                                                        Log.d("Inside equals id", "yes");
                                                        isFav.add(true);
                                                        //MarkerFragment.setCurMarker(curMarker);
                                                        Log.d("markerId","isFav(0): " + isFav.get(0));
                                                    }
                                                }

                                            }
                                        });


                            }

                            if(isFav.size() != 0){
                                if(!isFav.get(0)) {
                                    isFav.add(false);
                                }
                            }

                        }

                        else{

                            Log.d("Add fav error", task.getException() + "");
                            NotificationsControl.sendNotifications(context, "Adding favourite error", "Oops, something went wrong. Please try again.");

                        }
                    }
                });
    }

    public void addFavouriteToDB(Marker marker, String email, final Context context){
        Map<String, Object> newFavourite = new HashMap<>();
        DocumentReference newMarkerRef = db.collection("markers").document(marker.getId());
        newFavourite.put("marker", newMarkerRef);
        newFavourite.put("setAtHomeScreen", false);
        newFavourite.put("userEmail", email);

        db.collection("favourites")
                .add(newFavourite)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("add fav to db", "Favourite added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("add fav to db", "Error adding favourite: " + e);
                        NotificationsControl.sendNotifications(context, "Add favourites error", "Oops. Something went wrong. Please try again later");
                    }
                });

        //db.collection("")
    }

    public void deleteFavouriteFromDB(String favouriteId, final Context context){
        db.collection("favourites")
                .document(favouriteId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("deleteFromDB","Favourite deleted successfuly");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("deleteFromDB", "Error deleting favourite: " + e);
                        NotificationsControl.sendNotifications(context, "Deleting Favourite Error", "Oops. Something went wrong. Please try again later");
                    }
                });
    }

}
