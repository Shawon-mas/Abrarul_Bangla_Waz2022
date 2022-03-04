package com.abrarulwaz2022.abrarulbanglawaz2022.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.abrarulwaz2022.abrarulbanglawaz2022.R;
import com.abrarulwaz2022.abrarulbanglawaz2022.adater.IslamLifeTitleAdapter;
import com.abrarulwaz2022.abrarulbanglawaz2022.model.ClickItem;
import com.abrarulwaz2022.abrarulbanglawaz2022.model.IslamLifeTitle;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class IslamLife extends AppCompatActivity implements ClickItem {
    RecyclerView recyclerView;
    List<IslamLifeTitle> titleList_ac;
    IslamLifeTitleAdapter adapter;
    AdView adView;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer youTubePlayer;
    private String currentVideoId;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mRef=db.collection("Blog");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_islam_life);
        initViews();
        recyclerviewImplement();
        addShow();
    }
    private void addShow() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        adView = findViewById(R.id.adView_blog);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
    private void recyclerviewImplement() {
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter=new IslamLifeTitleAdapter(IslamLife.this,titleList_ac,this);
        recyclerView.setAdapter(adapter);
        mRef.orderBy("id", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d("Firestore error", error.getMessage());
                    return;
                }
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        titleList_ac.add(documentChange.getDocument().toObject(IslamLifeTitle.class));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initViews() {
        recyclerView=findViewById(R.id.islamLife_recycler);
        youTubePlayerView=findViewById(R.id.youtube_player_view_islamLife);
        titleList_ac=new ArrayList<IslamLifeTitle>();

    }

    @Override
    public void onItemClick(int position) {
        cueVideo(titleList_ac.get(position).getVideoId());

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer initializedYouTubePlayer) {
                youTubePlayer = initializedYouTubePlayer;
                youTubePlayer.cueVideo(currentVideoId, 0);

            }
        });
        //
        Toasty.info(getApplicationContext(),"Please wait...Loading Video",Toasty.LENGTH_LONG,true).show();
    }
    void cueVideo(String videoId) {
        currentVideoId = videoId;

        if(youTubePlayer == null)
            return;

        youTubePlayer.cueVideo(videoId, 0);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }
}