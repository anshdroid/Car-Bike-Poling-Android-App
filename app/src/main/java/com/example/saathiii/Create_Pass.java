package com.example.app.saathiii;

/*
 Created By: Himanshu Rathore
 Date: 30/03/2019
 Mail: rathore.himanshu821@gmail.com
*/

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class Create_Pass extends AppCompatActivity {

    private EditText userName, userAge;
    private Button regButton;
    private FirebaseAuth firebaseAuth;
    private ImageView userprofilePic;
    String  name, age, email, password, url;
    private FirebaseStorage firebaseStorage;
    private static int PICK_IMAGE=123;
    Uri imagePath;
    private StorageReference storageReference;


    private EditText inputEmail, inputPassword;
    private Button btnSignIn;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK && data.getData()!=null)
        {
            imagePath=data.getData();

            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
                userprofilePic.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pass);
        setupUIViews();


        firebaseAuth= FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();

        storageReference=firebaseStorage.getReference();


        userprofilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
            }
        });




        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (validate()) {


                    //upload data to the database
                    String user_email=inputEmail.getText().toString().trim();
                    String user_Password=inputPassword.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                sendEmailVerification();
                            }
                            else
                            {
                                Toast.makeText(Create_Pass.this, "Registration Failed", Toast.LENGTH_SHORT).show();

                            }


                        }
                    });



                }
            }
        });


       btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Create_Pass.this, Activity_Signin.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private void setupUIViews(){
        userName = (EditText)findViewById(R.id.NameEditTextAdd);
        regButton = (Button) findViewById(R.id.ButtonRegister);
        userAge = (EditText)findViewById(R.id.MobileEditTextAdd);
        userprofilePic =(ImageView)findViewById(R.id.ShowImageViewToAdd);
        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);

    }



    private Boolean validate(){

        Boolean result=false;
        name=userName.getText().toString();
        password=inputPassword.getText().toString();
        email=inputEmail.getText().toString();
        age=userAge.getText().toString();


        if(name.isEmpty() || password.isEmpty() || email.isEmpty()|| age.isEmpty() || imagePath==null) {
            Toast.makeText(this,"Please Enter all the details",Toast.LENGTH_SHORT).show();


        }else{

            result = true;
        }
        return result;
    }

    private void sendEmailVerification(){
        final FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if (firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        sendUserData();
                        Toast.makeText(Create_Pass.this,"Successfully Registered, Please verify Email To proceed further",Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        startActivity(new Intent(Create_Pass.this, Activity_Signin.class));
                        finish();

                    }else{
                        Toast.makeText(Create_Pass.this, "Verfication mail has'nt been sent!",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }


    private void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myref = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid());

        if (imagePath != null) {

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
                                    url = uri.toString();

                                    @SuppressWarnings("VisibleForTests")
                                    UserProfile userProfile = new UserProfile(age, name, email, url);
                                    myref.setValue(userProfile);
                                }
                            });
                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Showing exception erro message.
                            Toast.makeText(Create_Pass.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {

            Toast.makeText(Create_Pass.this, "Please Select Image", Toast.LENGTH_LONG).show();

        }
    }




    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
