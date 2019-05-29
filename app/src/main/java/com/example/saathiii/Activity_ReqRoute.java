package com.example.app.saathiii;

/*
 Created By: Himanshu Rathore
 Date: 30/03/2019
 Mail: rathore.himanshu821@gmail.com
*/

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Activity_ReqRoute extends AppCompatActivity implements View.OnClickListener{

    Button btnDatePicker, btnTimePicker, ChooseButton;
    EditText txtDate, txtTime, destination, mobile, seats, name, image, source;
    private int mYear, mMonth, mDay, mHour, mMinute;

    // Declaring String variable ( In which we are storing firebase server URL ).
    public static final String Firebase_Server_URL = "https://saathi-61d96.firebaseio.com/";
    // Declaring String variables to store name & phone number get from EditText.
    String  DestinationHolder, MobileHolder, NameHolder,TimeHolder, DateHolder, SeatHolder, ImageURLHolder, SourceHolder;
    Firebase firebase;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private ImageView updateprofilePic;
    private static int PICK_IMAGE=123;
    Uri imagePath;
    private StorageReference storageReference;
    ProgressDialog progressDialog;
    String TAG ="Saathi";

    String[] places = {"Dausa","JK Lakshmipat University",
            "Shanti Nagar", "Diggi Road","Nirman Nagar","Shahpura","Sachivalaya Nagar","Ramnagar","Bhankrota",
            "RIICO Industrial Area","Delhi-Jaipur expressway","Jaipur-Ajmer Express Highway","NH-8","Sanganer",
            "Jhotwara","Baroni","Jyoti Nagar","Triveni Nagar","Sitapura","Raj Bhavan Road","Sagram Colony","Jaisinghpura",
            "Gokulpura","Sardar Patel Marg","Durgapura","Udyog Nagar","Gopalpura","M I Road","Moti Dongri Road","Khatipura",
            "Boraj","Shivdaspura","Heerawala","Jawahar Nagar","Hanuman Nagar","Bhan Nagar","Officers Campus Colony","C-Scheme",
            "Marudhar Nagar","Sodala","Bagru","Virat Nagar","Bichun","Niwai","Achrol","Phulera","Amer","Saiwad","Mansarovar",
            "Asalpur","Mahapura","Chaksu","Vaishali Nagar","Mahaveer Nagar","Jagatpura","Sirsi Road","Ajmer Road","Bapu Nagar",
            "Tilak Nagar","Malviya Nagar","Agra Road","Transport Nagar","Kukas","Renwal Phagi Road","Nari Ka Bas","Kalwar Road",
            "Chitrakoot","Subhash Marg","Lajpat Marg","Govindpura","Anand Lok","Sikar Road","Shyam Nagar","Vidhyadhar Nagar",
            "Raja Park","Tonk Road","Bani Park","Parthviraj Nagar","SC Road","Ashok Nagar","Jalupura","Goner Road","Adarsh Nagar",
            "Gopalpura By Pass","Pahadiya Road","Sahdev Marg","Padampura","Sethi Colony","Gopalbari","Vatika","Lal Kothi",
            "Tagore Nagar","Jhotwara Road","Milap Nagar","Takht E Shahi Road","Vivekanand Marg","New Sanganer Road",
            "Shastri Nagar","Bhawani Singh Road","Civil Lines","Malpura","Pratap Nagar","Sindhi Camp","Chandpole","Amrapali Circle"};

    // Root Database Name for Firebase Database.
    public static final String Database_Path = "Req_Route_Details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_req_route);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Activity_ReqRoute.this);
        builder.setMessage(Html.fromHtml("<font color='#000000'>You are requested to add request for today or for next day</font>"));
        builder.setTitle(Html.fromHtml("<font color='#000000'>Note</font>"));
        builder.setCancelable(false);
        builder.setNegativeButton(Html.fromHtml("<font color='#000000'>Ok</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,
                                int which)
            {
                dialog.cancel();
            }
        });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        alertDialog.show();


        Firebase.setAndroidContext(Activity_ReqRoute.this);
        firebase = new Firebase(Firebase_Server_URL);
        // Assign FirebaseStorage instance to storageReference.
        storageReference = FirebaseStorage.getInstance().getReference();

        // Assign FirebaseDatabase instance with root database name.
        databaseReference2 = FirebaseDatabase.getInstance().getReference(Database_Path);

        btnDatePicker=(Button)findViewById(R.id.btn_req_date);
        btnTimePicker=(Button)findViewById(R.id.btn_req_time);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);
        destination=(EditText)findViewById(R.id.input_destination);
        mobile = (EditText) findViewById(R.id.input_num);
        seats = (EditText) findViewById(R.id.input_seats);
        name = (EditText)findViewById(R.id.input_name);
        image = (EditText)findViewById(R.id.input_imageURL);
        source = (EditText)findViewById(R.id.input_pickup);

        progressDialog = new ProgressDialog(Activity_ReqRoute.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        //Creating the instance of ArrayAdapter containing list
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, places);
        //Getting the instance of AutoCompleteTextView
        AutoCompleteTextView actv = (AutoCompleteTextView) destination;
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(Color.WHITE);

        //Creating the instance of ArrayAdapter containing list
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, places);
        //Getting the instance of AutoCompleteTextView
        AutoCompleteTextView actvv = (AutoCompleteTextView) source;
        actvv.setThreshold(1);//will start working from first character
        actvv.setAdapter(adapter1);//setting the adapter data into the AutoCompleteTextView
        actvv.setTextColor(Color.WHITE);


        firebaseAuth= FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();


        final DatabaseReference databaseReference=firebaseDatabase.getReference("Users").child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile=dataSnapshot.getValue(UserProfile.class);

                name.setText(userProfile.getUserName().toString());
                mobile.setText(userProfile.getUserAge().toString());
                image.setText(userProfile.getImageURL().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Activity_ReqRoute.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });


        findViewById(R.id.btn_req).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validationSuccess()) {

                    ReqRouteDetails reqRootDetails = new ReqRouteDetails();

                    GetDataFromEditText();

                    reqRootDetails.setRName(NameHolder);
                    reqRootDetails.setRDestination(DestinationHolder);
                    reqRootDetails.setRMobile(MobileHolder);
                    reqRootDetails.setRTime(TimeHolder);
                    reqRootDetails.setRDate(DateHolder);
                    reqRootDetails.setRSeat(SeatHolder);
                    reqRootDetails.setRimageURL(ImageURLHolder);
                    reqRootDetails.setRSource(SourceHolder);

                    //String ImageUploadId = databaseReference2.push().getKey();

                    // Adding image upload id s child element into databaseReference.
                    databaseReference2.push().setValue(reqRootDetails);

                    progressDialog.dismiss();

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Activity_ReqRoute.this);
                    alertDialog.setTitle("Done!"); // Sets title for your alertbox
                    alertDialog.setMessage("Your Request is added"); // Message t11o be displayed on alertbox
                    /* When positive (yes/ok) is clicked */
                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            startActivity(new Intent(Activity_ReqRoute.this, MainActivity.class));
                            finish();
                        }
                    });

                    alertDialog.show();
                    AlertDialog dialog = alertDialog.create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                }
            }
        });


    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    private Boolean validationSuccess() {

        if (source.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please enter Pick Up point", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (destination.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please enter Drop Point", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtDate.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please enter Date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtTime.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please enter Time", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (seats.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please enter No. of Seats required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void GetDataFromEditText() {

        // Setting progressDialog Title.
        progressDialog.setTitle("Request is Uploading...");

        // Showing progressDialog.
        progressDialog.show();

        NameHolder = name.getText().toString().trim();
        DestinationHolder = destination.getText().toString().trim();
        MobileHolder = mobile.getText().toString().trim();
        TimeHolder = txtTime.getText().toString().trim();
        DateHolder = txtDate.getText().toString().trim();
        SeatHolder = seats.getText().toString().trim();
        ImageURLHolder = image.getText().toString().trim();
        SourceHolder = source.getText().toString().trim();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
