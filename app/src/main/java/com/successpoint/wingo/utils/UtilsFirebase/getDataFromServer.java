package com.successpoint.wingo.utils.UtilsFirebase;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import androidx.annotation.NonNull;

public class getDataFromServer {

    private Context context;
    private String url;

    public getDataFromServer(Context context, String url) {
        this.context = context;
        this.url = url;
    }

    public void Data(final DataFromFirebaseOnGet dataReceived) throws JSONException {
        final boolean[] boolean_val={true};
        FirebaseDatabase.getInstance().getReference().child(url).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(boolean_val[0]){
                    boolean_val[0]=false;
                    dataReceived.onSuccess(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dataReceived.onCancel(databaseError);
            }
        });

    }
    public void DataChild(final DataFromFirebaseOnGet dataReceived) throws JSONException {
        final boolean[] boolean_val={true};
        FirebaseDatabase.getInstance().getReference().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(boolean_val[0]){
                    boolean_val[0]=false;
                    dataReceived.onSuccess(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void DataChildLastOne(final DataFromFirebaseOnGet dataReceived) throws JSONException {
        final boolean[] boolean_val={false};
        FirebaseDatabase.getInstance().getReference().child(url).limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("dataSnapshot",dataSnapshot.toString());
                if(boolean_val[0]) {
                    dataReceived.onSuccess(dataSnapshot);
                    Log.e("dataSnapshotx",dataSnapshot.toString());
                }
                else
                    boolean_val[0]=true;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void DataChildLastOneDeleted(final DataFromFirebaseOnGet dataReceived) throws JSONException {
        FirebaseDatabase.getInstance().getReference().child(url).limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                dataReceived.onSuccess(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void DataRemove(final DataFromFirebaseOnAdded dataReceived) throws JSONException {
        FirebaseDatabase.getInstance().getReference().child(url).removeValue();

    }
}
