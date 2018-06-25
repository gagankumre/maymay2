package com.example.gagan.maymay_test1;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity {


    private EditText namesignup;
    private EditText emailsignup;
    private EditText passwordsignup;
    private Button signup;
    private Button alreadyhvacc;
    private CircleImageView profileupload;
    private ProgressBar progressBarregister;

    private Uri imageuri;

    private StorageReference storage;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        imageuri=null;

        storage =FirebaseStorage.getInstance().getReference().child("images");
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        namesignup=findViewById(R.id.namesignup);
        emailsignup=findViewById(R.id.emailsignup);
        passwordsignup=findViewById(R.id.passwordsignup);
        signup=findViewById(R.id.signup);
        alreadyhvacc=findViewById(R.id.alreadyhaveacc);
        profileupload=findViewById(R.id.profileimagesignup);
        progressBarregister = findViewById(R.id.registerprogressbar);

        namesignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namesignup.setCursorVisible(true);

            }
        });
        emailsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailsignup.setCursorVisible(true);
            }
        });
        passwordsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordsignup.setCursorVisible(true);

            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                namesignup.setCursorVisible(false);
                emailsignup.setCursorVisible(false);
                passwordsignup.setCursorVisible(false);

                if (imageuri!=null){
                    progressBarregister.setVisibility(View.VISIBLE);
                    Toast.makeText(Register.this,"Please wait we are creating your acccount", Toast.LENGTH_SHORT).show();

                    final String name=namesignup.getText().toString();
                    final String email=emailsignup.getText().toString();
                    final String password=passwordsignup.getText().toString();

                    if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){

                        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    final String userid = auth.getCurrentUser().getUid();

                                    StorageReference userprofile = storage.child(userid+".jpg");

                                    userprofile.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> uploadtask) {
                                            if(uploadtask.isComplete()){

                                                String downloaduri =uploadtask.getResult().getDownloadUrl().toString();

                                                Map<String , Object> usermap = new HashMap<>();
                                                usermap.put("name",name);
                                                usermap.put("image", downloaduri);

                                                firestore.collection("users").document(userid).set(usermap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        progressBarregister.setVisibility(View.INVISIBLE);
                                                        Toast.makeText(Register.this,"Your account is created successfully", Toast.LENGTH_SHORT).show();
                                                        sendtomain();

                                                    }
                                                });
                                            }
                                            else{

                                                Toast.makeText(Register.this,"error:"+ uploadtask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                progressBarregister.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                    });

                                }
                                else {
                                    Toast.makeText(Register.this,"error:"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    progressBarregister.setVisibility(View.INVISIBLE);
                                }
                            }
                        });

                    }

                    else{
                        progressBarregister.setVisibility(View.INVISIBLE);
                        Toast.makeText(Register.this,"all the field should be filled", Toast.LENGTH_SHORT).show();
                    }


                }
                else{
                    Toast.makeText(Register.this,"please select an image", Toast.LENGTH_SHORT).show();
                }

            }
        });



        alreadyhvacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



//              for selecting image
        profileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1).start(Register.this);


//                Intent intent= new Intent();
//                intent.setType("image/*");
//                intent.setAction(intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,"select picture"), PICK_IMAGE);
            }
        });



    }


    private void sendtomain() {

        Intent intent= new Intent(Register.this,Home.class);
        startActivity(intent);
        finish();

    }


    //               set image on activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
               imageuri = result.getUri();
               profileupload.setImageURI(imageuri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();


//        if(requestCode==PICK_IMAGE&&data!=null&&data.getData()!=null){
//            imageuri=data.getData();
//            profileupload.setImageURI(imageuri);


                }
            }
        }
    }


