package com.example.gagan.maymay_test1;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.sax.StartElementListener;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Homefragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Homefragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Homefragment extends android.support.v4.app.Fragment {

    private RecyclerView recyclerView;
    private List<memepost> meme_list;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private memepost_adapter memepost_adapter;
    private DocumentSnapshot lastvisible;

    private Query firstquery;

    private Boolean isfirstpagefirstload = true;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Homefragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Homefragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Homefragment newInstance(String param1, String param2) {
        Homefragment fragment = new Homefragment();
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
        View view= inflater.inflate(R.layout.fragment_homefragment, container, false);


        firestore=FirebaseFirestore.getInstance();
        recyclerView=view.findViewById(R.id.meme_rv);
        auth=FirebaseAuth.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
//        recyclerView.setAdapter(memepost_adapter);


        meme_list=new ArrayList<>();
        firestore=FirebaseFirestore.getInstance();


        firstquery = firestore.collection("posts").orderBy("timestamp",Query.Direction.DESCENDING);

        firstquery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (auth.getCurrentUser() != null) {

//                    if (isfirstpagefirstload) {
//
//                        try {
//                            lastvisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
//                        } catch (Exception p) {
//                            p.printStackTrace();
//                        }
//
//                    } else {
//
//                        try{
//                            lastvisible = queryDocumentSnapshots.getDocuments().get(1);
//                    } catch (Exception p) {
//                        p.printStackTrace();
//                    }
//
//                    }

                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            String memepostid = doc.getDocument().getId();
                            memepost memepost = doc.getDocument().toObject(com.example.gagan.maymay_test1.memepost.class).withId(memepostid);

//                            if (isfirstpagefirstload) {
                                meme_list.add(memepost);
//                            } else {
//
//                                meme_list.add(0, memepost);
//                            }

                            memepost_adapter.notifyDataSetChanged();

                        }
                    }
                    isfirstpagefirstload = false;

                }
            }

        });

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                Boolean reacherdbottom = !recyclerView.canScrollVertically(1);
//
//                if(reacherdbottom){
//
////                    Toast.makeText(getContext(),"3 card shown", Toast.LENGTH_SHORT).show();
//                    loadmorepost();
//                }
//            }
//        });



        memepost_adapter =new memepost_adapter(meme_list);
        recyclerView.setAdapter(memepost_adapter);
        return view;


    }

    public void loadmorepost(){


        Query nextquery = firestore.collection("posts").orderBy("timestamp",Query.Direction.DESCENDING).startAfter(lastvisible).limit(3);

        nextquery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(auth.getCurrentUser()!=null) {
                    if(!queryDocumentSnapshots.isEmpty()) {
                        lastvisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                        for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                String memepostid =doc.getDocument().getId();
                                memepost memepost = doc.getDocument().toObject(com.example.gagan.maymay_test1.memepost.class).withId(memepostid);
                                meme_list.add(memepost);
                                memepost_adapter.notifyDataSetChanged();

                            }
                        }
                    }
                }
            }
        });

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

