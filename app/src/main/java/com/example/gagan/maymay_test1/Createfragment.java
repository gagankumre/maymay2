package com.example.gagan.maymay_test1;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Createfragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Createfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Createfragment extends android.support.v4.app.Fragment {

    private static final int PICK_IMAGE = 1;
    private static final int MAX_LENGTH = 100;
    private Button post;
    private EditText write;
    private ImageButton uploadmeme;
    private Uri imageuri;
    private Button cancel;

    private ProgressBar progressBar;

    private StorageReference storageReference;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private String userid;

//    private Bitmap compressedImageFile;
//    String downloadthumb;






    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Createfragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Createfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Createfragment newInstance(String param1, String param2) {
        Createfragment fragment = new Createfragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_createfragment, container, false);

        firestore= FirebaseFirestore.getInstance();
        storageReference=FirebaseStorage.getInstance().getReference();
        auth=FirebaseAuth.getInstance();

       userid =  auth.getCurrentUser().getUid();


        imageuri=null;

        post=view.findViewById(R.id.postbtn);
        write=view.findViewById(R.id.write);
        uploadmeme=view.findViewById(R.id.memeuploadbtn);
        cancel=view.findViewById(R.id.cancel);

        progressBar=view.findViewById(R.id.uploadprogressBar);


        uploadmeme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write.setCursorVisible(false);
//                Intent intent= new Intent();
//                intent.setType("image/*");
//                intent.setAction(intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,"select picture"), PICK_IMAGE);

                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(getContext(),Createfragment.this);
            }
        });

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write.setCursorVisible(true);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write.setCursorVisible(false);
                write.getText().clear();
                uploadmeme.setImageResource(R.drawable.meme);
                imageuri=null;
            }
        });


        post.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                write.setCursorVisible(false);

                final String description = write.getText().toString();

                if(!TextUtils.isEmpty(description)){
                if(imageuri!=null){
                    progressBar.setVisibility(View.VISIBLE);
                    cancel.setVisibility(View.GONE);

                    final String randomname= UUID.randomUUID().toString();
                    StorageReference filepath = storageReference.child("post_images").child(randomname+".jpg");


                    filepath.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {


                            if(task.isSuccessful()){

                         /*       File newimagefile = new File(imageuri.getPath());
                                try {
                                    compressedImageFile = new Compressor(Createfragment.this)
                                            .setMaxHeight(100)
                                            .setMaxWidth(100)
                                            .setQuality(2)
                                            .compressToBitmap(newimagefile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] thumbdata = baos.toByteArray();

                                UploadTask uploadTask =storageReference.child("post_images/thumbs").child(randomname+".jpg").putBytes(thumbdata);

                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                       downloadthumb = taskSnapshot.getDownloadUrl().toString();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });  */

                                String downloaaduri = task.getResult().getDownloadUrl().toString();
//


                                Map<String,Object> postmap= new HashMap<>();
                                postmap.put("image_url",downloaaduri);
                                postmap.put("description",description);
                                postmap.put("user",userid);
                                postmap.put("timestamp",FieldValue.serverTimestamp());

                                firestore.collection("posts").add(postmap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {

                                        if (task.isSuccessful()){
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(getActivity(), "meme uploaded successfully", Toast.LENGTH_SHORT).show();




                                            write.getText().clear();
                                            uploadmeme.setImageResource(R.drawable.meme);
                                            imageuri=null;
                                            cancel.setVisibility(View.VISIBLE);

                                        }
                                        else {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(getActivity(),"error:"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                            }
                            else {
                                progressBar.setVisibility(View.INVISIBLE);

                                Toast.makeText(getActivity(),"error:"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }
                else {
                    Toast.makeText(getActivity(), "please select an image", Toast.LENGTH_SHORT).show();
                }
            }

            else {
                    Toast.makeText(getActivity(), "write something", Toast.LENGTH_SHORT).show();
                }
            }
        });




        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == PICK_IMAGE && data != null && data.getData() != null) {
//            imageuri = data.getData();
//            uploadmeme.setImageURI(imageuri);
//
//        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageuri = result.getUri();
                uploadmeme.setImageURI(imageuri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();

            }
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
