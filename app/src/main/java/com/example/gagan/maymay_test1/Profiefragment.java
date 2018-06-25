package com.example.gagan.maymay_test1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Profiefragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Profiefragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profiefragment extends android.support.v4.app.Fragment {





    private CircleImageView profile;
    private TextView name;
    private TextView email;

    private ImageButton settings;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private String userid;
    private ProgressBar progressBar;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Profiefragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profiefragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Profiefragment newInstance(String param1, String param2) {
        Profiefragment fragment = new Profiefragment();
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profiefragment, container, false);

        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        userid=auth.getCurrentUser().getUid();

        settings=view.findViewById(R.id.editprofile);
        profile=view.findViewById(R.id.profileview);
        name=view.findViewById(R.id.nameview);
        email=view.findViewById(R.id.emailview);
        progressBar=view.findViewById(R.id.progressBarprofile);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(container.getContext(),Profile.class);
                startActivity(intent);
            }
        });

        if(auth.getCurrentUser()!=null) {

            progressBar.setVisibility(View.VISIBLE);

            firestore.collection("users").document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String user_name = documentSnapshot.getString("name");
                    String user_image = documentSnapshot.getString("image");


                    name.setText(user_name);
                    name.setVisibility(View.VISIBLE);
                    email.setText(auth.getCurrentUser().getEmail());

                    try {
                        Glide.with(container.getContext()).load(user_image).into(profile);
                    }catch (Exception e){

                    }
                    progressBar.setVisibility(View.INVISIBLE);


                }
            });
        }





        return view;

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
