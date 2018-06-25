package com.example.gagan.maymay_test1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;



/**
 * Created by GAGAN on 24-05-2018.
 */

public class memepost_adapter extends RecyclerView.Adapter< memepost_adapter.ViewHolder> {

    public List<memepost> meme_list;
    public Context context;
    private FirebaseAuth  auth;
    private FirebaseFirestore firestore;
    private int p;
    String id;


    public memepost_adapter(List<memepost> meme_list){

        this.meme_list=meme_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.meme_card,parent,false);

        context=parent.getContext();
        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();

        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        viewHolder.setIsRecyclable(false);

        final String memepostid = meme_list.get(i).memepostid;


        final String currentuserid = auth.getCurrentUser().getUid();

        final String userid = meme_list.get(i).getUser();

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);




        firestore.collection("users").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String name = task.getResult().getString("name");
                    String userimage = task.getResult().getString("image");


                    if(currentuserid.equals(userid)){

                        viewHolder.showmenu();
                        viewHolder.menubutton.setContentDescription(memepostid);

//                               delete feature

                        viewHolder.menubutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                id=(String) viewHolder.menubutton.getContentDescription();
                                viewHolder.memecard.setTag(id);
                               final int i= viewHolder.getAdapterPosition();
//                                Toast.makeText(context, id, Toast.LENGTH_SHORT).show();

//                                inflates the menu
                                Context newcontext = new ContextThemeWrapper(context,R.style.PopupMenu);
                                PopupMenu popup = new PopupMenu(newcontext, v);
                                popup.getMenuInflater().inflate(R.menu.meme_menu,
                                        popup.getMenu());

                                popup.show();

                                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {

                                        switch (item.getItemId()) {
                                            case R.id.delete:
//                                                Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
                                                viewHolder.delete(id,i);
                                                Toast.makeText(context,"deleting", Toast.LENGTH_SHORT).show();
                                        }

                                        return true;
                                    }
                                });


                            }

                        });
                    }
                    else{
                        viewHolder.hidemenu();
                    }

                    viewHolder.setnameandimage(name, userimage);

                } else {
                    Exception e =new Exception();
                    Toast.makeText(context, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        try {
//        long miliseconds = meme_list.get(i).getTimestamp().getTime();

            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(meme_list.get(i).getTimestamp().getTime());
            String date = DateFormat.format("dd-MM-yyyy hh:mm A", cal).toString();


//        String dateString = DateFormat.format("MM/dd/yyyy", new Date(miliseconds)).toString();
        viewHolder.setdatentime(date);
        } catch (Exception e) {

//            Toast.makeText(context, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        String imageurl = meme_list.get(i).getImage_url();
        viewHolder.setmemeimage(imageurl);


        String descdata = meme_list.get(i).getDescription();
        viewHolder.setdesctext(descdata);



            // likes count
            firestore.collection("posts").document(memepostid).collection("likes");

            firestore.collection("posts").document(memepostid).collection("likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                    if(auth.getCurrentUser()!=null) {

                        if (!queryDocumentSnapshots.isEmpty()) {

                            int count = queryDocumentSnapshots.size();

                            viewHolder.updatelikescount(count);

                        } else {
                            viewHolder.updatelikescount(0);
                        }
                    }

                }
            });

            //get likes
            firestore.collection("posts").document(memepostid).collection("likes").document(currentuserid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if(auth.getCurrentUser()!=null) {
                        if (documentSnapshot.exists()) {
                            viewHolder.memelikebtn.setImageDrawable(context.getDrawable(R.mipmap.likebtnwhite));
                        } else {
                            viewHolder.memelikebtn.setImageDrawable(context.getDrawable(R.mipmap.likebtnblack));
                        }
                    }
                }
            });


            // likes feature
            viewHolder.memelikebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    firestore.collection("posts").document(memepostid).collection("likes").document(currentuserid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            firestore.collection("posts").document(memepostid).collection("likes").document(currentuserid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(auth.getCurrentUser()!=null) {
                                        if (!task.getResult().exists()) {
                                            Map<String, Object> likemap = new HashMap<>();
                                            likemap.put("timestamp", FieldValue.serverTimestamp());


                                            firestore.collection("posts").document(memepostid).collection("likes").document(currentuserid).set(likemap);
                                            p = 1;

                                        } else {
                                            firestore.collection("posts").document(memepostid).collection("likes").document(currentuserid).delete();
                                            p = 0;
                                        }
                                    }

                                }


                            });
                        }


                    });

                    if (p==0){
                        viewHolder.memelikebtn.setImageDrawable(context.getDrawable(R.mipmap.likebtnwhite));
                        p=1;
//                        Toast.makeText(context, "please check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        viewHolder.memelikebtn.setImageDrawable(context.getDrawable(R.mipmap.likebtnblack));
                        p=0;
//                        Toast.makeText(context, "please check your internet connection", Toast.LENGTH_SHORT).show();
                    }



                }
            });


    }



    @Override
    public int getItemCount() {
        return meme_list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mview;
        private TextView descview;
        private ImageView memeimageview;
        private TextView memedate;
        private TextView memername;
        private CircleImageView memerimage;
        private ImageView memelikebtn;
        private TextView memelikecount;
        private ImageButton menubutton;
        private CardView memecard;
        private ProgressBar pb1;
        private ProgressBar pb2;



        public ViewHolder(View itemView) {
            super(itemView);
            mview=itemView;
            memelikebtn=mview.findViewById(R.id.likebtn);
            menubutton=mview.findViewById(R.id.menubutton);
            memecard =mview.findViewById(R.id.memecard);
            pb1=mview.findViewById(R.id.pb1);
            pb2=mview.findViewById(R.id.pb2);
        }

        public void setdesctext(String text){
            descview=mview.findViewById(R.id.memedescription);
            descview.setText(text);

        }

        public  void setdatentime(String date){
            memedate=mview.findViewById(R.id.memedatentime);
            memedate.setText(date);
        }
        public  void  setnameandimage(String name,String userimage){
            memername =mview.findViewById(R.id.memeusername);
            memerimage=mview.findViewById(R.id.memeuserimage);

            memername.setText(name);
            try {
                Glide.with(context).load(userimage).into(memerimage);
            }
            catch (Exception e){

            }
            pb1.setVisibility(View.INVISIBLE);
        }
        public void setmemeimage(String downloaduri){

            memeimageview=mview.findViewById(R.id.memeimag);

            Glide.with(context).load(downloaduri).into(memeimageview);
            pb2.setVisibility(View.INVISIBLE);

        }


        public void updatelikescount(int count){

            memelikecount = mview.findViewById(R.id.likeno);
            memelikecount.setVisibility(View.VISIBLE);
            memelikecount.setText(count + " Likes");
        }

        public void showmenu(){

            menubutton.setVisibility(View.VISIBLE);
        }
        public void hidemenu(){

            menubutton.setVisibility(View.GONE);
        }
        public void delete(final String id, final int i){

            memecard =mview.findViewWithTag(id);
//            Toast.makeText(context,(String) memecard.getTag(), Toast.LENGTH_SHORT).show();

            firestore=FirebaseFirestore.getInstance();
            firestore.collection("posts").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    firestore.collection("posts").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context,"deleted successfully", Toast.LENGTH_SHORT).show();
                            Toast.makeText(context,"swipe down  to refresh", Toast.LENGTH_SHORT).show();
                            notifyItemRemoved(i);
                        }
                    });
                }
            });

        }

    }

}
