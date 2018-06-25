package com.example.gagan.maymay_test1;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    private ImageButton back;
    private CircleImageView imgupload;
    private Uri imageuri;
    private Button upload;
    private ProgressBar bar;
    private ImageButton delete;
    private FirebaseAuth auth;
    private StorageReference storage;
    private FirebaseFirestore firestore;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth=FirebaseAuth.getInstance();
        storage= FirebaseStorage.getInstance().getReference().child("images");
        firestore=FirebaseFirestore.getInstance();
        delete=findViewById(R.id.delete);
        bar =findViewById(R.id.progress);

        imgupload=findViewById(R.id.editprofileimage);
        imageuri=null;

        upload=findViewById(R.id.submit);
        back=findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        //              for selecting image
        imgupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1).start(Profile.this);

            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imageuri!=null){

                   bar.setVisibility(View.VISIBLE);
                    Toast.makeText(Profile.this,"uploading", Toast.LENGTH_SHORT).show();

                    final String userid = auth.getCurrentUser().getUid();
                    firestore.collection("users").document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                             name = documentSnapshot.getString("name");
                        }
                    });

                    StorageReference userprofile = storage.child(userid+".jpg");

                    userprofile.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if(task.isSuccessful()){

                                String downloaduri =task.getResult().getDownloadUrl().toString();


                                Map<String , Object> usermap = new HashMap<>();
                                usermap.put("image", downloaduri);
                                usermap.put("name",name);

//                                firestore.collection("users").document(userid).collection("image").add(downloaduri);
                                firestore.collection("users").document(userid).set(usermap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        bar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(Profile.this,"uploaded successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                            else {
                                Toast.makeText(Profile.this,"error:"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                bar.setVisibility(View.INVISIBLE);
                            }

                        }
                    });


                }

                else {

                    Toast.makeText(Profile.this,"please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Profile.this,"gd", Toast.LENGTH_SHORT).show();
                bar.setVisibility(View.VISIBLE);
                final String userid = auth.getCurrentUser().getUid();

                final Query query = firestore.collection("posts");

                query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        int i=0;

                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){

                            if(doc.get("user")==userid){
                                i++;

                                Toast.makeText(Profile.this,i, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });


            }
        });


    }

    //               set image on activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageuri = result.getUri();
                imgupload.setImageURI(imageuri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();

            }
        }
    }
}
