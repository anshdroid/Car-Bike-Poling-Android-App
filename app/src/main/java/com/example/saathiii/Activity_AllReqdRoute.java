package com.example.app.saathiii;

/*
 Created By: Himanshu Rathore
 Date: 30/03/2019
 Mail: rathore.himanshu821@gmail.com
*/

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Activity_AllReqdRoute extends AppCompatActivity {

    DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    List<ReqRouteDetails> list = new ArrayList<>();

    RecyclerView recyclerView;

    RecyclerView.Adapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_req_route);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(Activity_AllReqdRoute.this));

        progressDialog = new ProgressDialog(Activity_AllReqdRoute.this);

        progressDialog.setMessage("Loading Requested Routes");

        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference(Activity_ReqRoute.Database_Path);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    ReqRouteDetails reqrouteDetails = dataSnapshot.getValue(ReqRouteDetails.class);

                    list.add(reqrouteDetails);
                }

                adapter = new RecyclerViewAdapter1(Activity_AllReqdRoute.this, list);

                recyclerView.setAdapter(adapter);

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
