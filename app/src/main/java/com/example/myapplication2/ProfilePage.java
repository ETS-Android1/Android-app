package com.example.myapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication2.objectmodel.Container;
import com.example.myapplication2.objectmodel.ProfileModel;
import com.example.myapplication2.objectmodel.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;



//TODO Need Logic to hide button when visiting other profile pages (Check DocumentReference and/or ID)
public class ProfilePage extends AppCompatActivity {
    private static final String TAG = "ProfilePage";
    FirebaseFirestore db;

    enum Data {
        NAME,
        EMAIL,
        PILLAR,
        TERM,
        MODULE,
        BIO
    }

    ImageView backArrow;
    Button logOutButton;
    ImageView profilePicture;
    TextView profileName;
    TextView profileEmail;
    TextView pillarValue;
    TextView termValue;
    TextView module1;
    TextView module2;
    TextView module3;
    TextView module4;
    TextView module5;
    TextView bioText;
    Button editButton;

    //Test Variables for Firestore
    String value;
    final Container<UserModel> user = new Container(new UserModel());
    final Container<ProfileModel> profile = new Container(new ProfileModel());

    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.backArrow:
                    startActivity((new Intent(ProfilePage.this, MainPageActivity.class)));
                    break;
                case R.id.logOutButton:
                    startActivity((new Intent(ProfilePage.this, LoginActivity.class)));
                    break;
                case R.id.editButton:
                    startActivity(new Intent(ProfilePage.this, EditProfilePage.class));
                    Log.i(TAG, "UserModel Container: " + user.get().toString());
                    break;
                default:
                    Log.w(TAG, "Button not Found");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        db = FirebaseFirestore.getInstance();

        backArrow = findViewById(R.id.backArrow);
        logOutButton = findViewById(R.id.logOutButton);
        profilePicture = findViewById(R.id.profilePicture);
        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        pillarValue = findViewById(R.id.pillarValue);
        termValue = findViewById(R.id.termValue);
        module1 = findViewById(R.id.Module1);
        module2 = findViewById(R.id.Module2);
        module3 = findViewById(R.id.Module3);
        module4 = findViewById(R.id.Module4);
        module5 = findViewById(R.id.Module5);
        bioText = findViewById(R.id.bioText);
        editButton = findViewById(R.id.editButton);

        backArrow.setOnClickListener(new ClickListener());
        logOutButton.setOnClickListener(new ClickListener());
        editButton.setOnClickListener(new ClickListener());
    }

    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference userId = getDocumentReference("Users", "Test");
        DocumentReference profileId = getDocumentReference("Profiles", "Test");
        getProfileData(profileId, profileName, Data.NAME);
        getUserData(userId, profileEmail, Data.EMAIL);
        //FIXME find a way to store data from Firestore in an Object for referencing
        getProfileData(profileId, pillarValue, Data.PILLAR);
        getProfileData(profileId, termValue, Data.TERM);
        //FIXME find a way to extract modules from Firestore DocumentReference
        getProfileData(profileId, bioText, Data.BIO);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public DocumentReference getDocumentReference(String collectionId, String documentId) {
        return db.collection(collectionId).document(documentId);
    }

    public void getProfileData(DocumentReference profileRef, TextView view, Data data) {
        //FIXME Using onSuccessListener
        profileRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                ProfilePage.this.profile.set(document.toObject(ProfileModel.class));
                Log.i(TAG, "ProfileModel Class: " + document.toObject(ProfileModel.class).toString());
            }
        });

        //FIXME USING onCompleteListener
        profileRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
           @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: "+ document.getData());
                        switch (data) {
                            case NAME:
                                view.setText(document.getString("name"));
                                break;
                            case TERM:
                                view.setText(String.valueOf(document.getLong("term")));
                                break;
                            case PILLAR:
                                view.setText(document.getString("pillar"));
                                break;
                            case BIO:
                                view.setText(document.getString("bio"));
                                break;
                            default:
                                Toast.makeText(ProfilePage.this, "Item Does Not Exist in Document", Toast.LENGTH_LONG).show();
                                Log.d(TAG, "Error in Enum detected" + data);
                                break;
                        }
                    }
                    else {
                        Log.d(TAG, "No such document");
                    }
                }
                else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
           }
        });
    }

    public void getUserData(DocumentReference userRef, TextView view, Data data) {
        //FIXME Using onSuccessListener
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                ProfilePage.this.user.set(document.toObject(UserModel.class));
                Log.i(TAG, "UserModel Class: " + document.toObject(UserModel.class).toString());
            }
        });

        //FIXME USING onCompleteListener
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: "+ document.getData());
                        switch (data) {
                            case EMAIL:
                                view.setText(document.getString("email"));
                                break;
                            default:
                                Toast.makeText(ProfilePage.this, "Item Does Not Exist in Document", Toast.LENGTH_LONG).show();
                                Log.d(TAG, "Error in Enum detected"+ data);
                                break;
                        }
                    }
                    else {
                        Log.d(TAG, "No such document");
                    }
                }
                else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}