package com.ersubhadip.hackathon.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ersubhadip.hackathon.Activity.TabedActivity;
import com.ersubhadip.hackathon.Classes.VideoAdapter;
import com.ersubhadip.hackathon.Classes.booksRvAdapter;
import com.ersubhadip.hackathon.Classes.ebooksAdapter;
import com.ersubhadip.hackathon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class videoFragment extends Fragment {

    private List<String> videoList = new ArrayList<>();
    private List<String> VideoUrlList = new ArrayList<>();
    private RecyclerView VideoRv;
    private VideoAdapter videoAdapter;
    private ImageView courseBanner;
    FirebaseFirestore store;
    String t;
    private ProgressBar bar;






    public videoFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        //initialisation
        VideoRv=view.findViewById(R.id.VideoTabRecyclerView);
        courseBanner=view.findViewById(R.id.courseImage);
        bar=view.findViewById(R.id.loading);
        t = TabedActivity.t;
        //end

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        videoList.clear(); //to avoid duplicate items

        //todo:link setting
        store=FirebaseFirestore.getInstance();

        store.collection("courses").document(t).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                bar.setVisibility(View.VISIBLE);
                DocumentSnapshot snapshot=task.getResult();
                if(task.isSuccessful()){



                    videoList=(ArrayList<String>)snapshot.get("videoTitle");
                    String url=(String)snapshot.get("imageUrl");
                    Glide.with(getContext()).load(url).into(courseBanner);

                    if(videoList.get(0).equals("")){

                        videoList.clear();

                    }


                    //setting LinearLayoutManager
                    LinearLayoutManager manager=new LinearLayoutManager(getContext());
                    manager.setOrientation(RecyclerView.VERTICAL);
                    VideoRv.setLayoutManager(manager);

                    //end

                    //setting the adapter
                    videoAdapter=new VideoAdapter(VideoUrlList,videoList);
                    VideoRv.setAdapter(videoAdapter);
                    videoAdapter.notifyDataSetChanged();
                    //end
                    bar.setVisibility(View.GONE);

                }else{

                    Toast.makeText(getContext(), ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    bar.setVisibility(View.GONE);
                }



            }
        });
    }
}