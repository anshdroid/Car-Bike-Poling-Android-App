package com.example.app.saathiii;

/*
 Created By: Himanshu Rathore
 Date: 30/03/2019
 Mail: rathore.himanshu821@gmail.com
*/

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Objects;

public class Activity_PersonalDetails extends AppCompatActivity {
    private EditText newUserName, newUserAge, newUsermail;
    private Button save;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private ImageView updateprofilePic;
    private static int PICK_IMAGE=123;
    String name, mail, age;
    Uri imagePath;
    String TAG="Saathi";
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    ProgressDialog progressDialog;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK && data.getData()!=null)
        {
            imagePath=data.getData();

            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
                updateprofilePic.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        newUserName=findViewById(R.id.ImageNameEditText);
        newUserAge=findViewById(R.id.MobileEditText);
        newUsermail=findViewById(R.id.mailEditText);
        save=findViewById(R.id.ButtonAdd);
        updateprofilePic=findViewById(R.id.ShowImageView);

        progressDialog = new ProgressDialog(Activity_PersonalDetails.this);
        progressDialog.setMessage("Uploading");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //get firebase auth instance
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Name, email address, and profile photo Url
            String email = user.getEmail();
            newUsermail.setText(email);

        }
        firebaseStorage= FirebaseStorage.getInstance();


        final DatabaseReference databaseReference=firebaseDatabase.getReference("Users").child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile=dataSnapshot.getValue(UserProfile.class);

                newUserName.setText(userProfile.getUserName());
                newUserAge.setText(userProfile.getUserAge());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Activity_PersonalDetails.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });

        final StorageReference storageReference=firebaseStorage.getReference();
        storageReference.child(firebaseAuth.getUid()).child("Images/Profile Picture").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(updateprofilePic);
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imagePath != null) {

                    name = newUserName.getText().toString();
                    age = newUserAge.getText().toString();
                    mail = newUsermail.getText().toString();

                    progressDialog.show();
                    // Creating second StorageReference.
                    final StorageReference storageReference2nd = storageReference.child(firebaseAuth.getUid()).child("Images").child("Profile Picture");

                    // Adding addOnSuccessListener to second StorageReference.
                    storageReference2nd.putFile(imagePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    storageReference2nd.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Log.d(TAG, "onSuccess: uri= " + uri.toString());
                                            String url = uri.toString();


                                            @SuppressWarnings("VisibleForTests")
                                            UserProfile userProfile = new UserProfile(name, mail, age, url);
                                            databaseReference.setValue(userProfile);

                                            // Getting image upload ID.
                                            String ImageUploadId = databaseReference.push().getKey();

                                            // Adding image upload id s child element into databaseReference.
                                            databaseReference.child(ImageUploadId).setValue(userProfile);
                                            progressDialog.dismiss();
                                            startActivity(new Intent(Activity_PersonalDetails.this, MainActivity.class));
                                            finish();

                                        }
                                    });
                                }
                            })
                            // If something goes wrong .
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {

                                    // Showing exception erro message.
                                    Toast.makeText(Activity_PersonalDetails.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                } else {

                    startActivity(new Intent(Activity_PersonalDetails.this, MainActivity.class));
                    finish();
                }
            }


        });

        updateprofilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
