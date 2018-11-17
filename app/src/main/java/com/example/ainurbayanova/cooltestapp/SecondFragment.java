package com.example.ainurbayanova.cooltestapp;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class SecondFragment extends Fragment implements ClickLister, ItemOnMeClickListener, ItemOnThisClickListener, View.OnClickListener {
    public SecondFragment() {

    }

    RecyclerView recyclersView;
    CircleImageView firstImageView;
    RecyclerView recyclerViewForChallenge;
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> tutorialImages = new ArrayList<>();
    Animation animation;
    RecycleAlertViewAdapter alertAdapter;
    DatabaseReference databaseReference;
    String user = null;
    AlertDialog alertDialog;
    ProgressBar alertProgress;
    ArrayList<String> enemyName = new ArrayList<>();
    ArrayList<String> enemyImage = new ArrayList<>();
    ArrayList<String> usernames = new ArrayList<>();
    ArrayList<String> enemyName2 = new ArrayList<>();
    ArrayList<String> enemyImage2 = new ArrayList<>();
    ArrayList<String> questions = new ArrayList<>();
    ArrayList<String> hi = new ArrayList<>();
    ArrayList<String> me = new ArrayList<>();
    ArrayList<String> theme3 = new ArrayList<>();
    RecyclerView recyclersView3;
    RecyclerViewForFinished recyclerAdapter3;
    ArrayList<String> theme2 = new ArrayList<>();
    ArrayList<String> usernames2 = new ArrayList<>();
    ArrayList<String> allNames = new ArrayList<>();
    ArrayList<String> theme = new ArrayList<>();
    LinearLayout cardToFight;
    RecyclerViewForFight recyclerAdapter;
    RecyclerViewForChallenge recyclerAdapter2;
    ArrayList<String> hei = new ArrayList<>();
    FirebaseUser users = null;
    CircleImageView animationImageView;
    View view;
    ArrayList<Integer> counting = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    String image;
    ProgressBar preloader;
    int answered = 0;
    int first = 0;
    int second = 0;
    ArrayList<CollectEnemyInformation> inform = new ArrayList<>();
    String enemy = "";
    ArrayList<String> enemySrc = new ArrayList<>();
    ArrayList<String> enemyText = new ArrayList<>();
    ArrayList<String> enemyScoring = new ArrayList<>();
    ArrayList<String> myScoring = new ArrayList<>();
    ArrayList<String> myTitles = new ArrayList<>();
    ArrayList<String> titling = new ArrayList<>();
    TextView clear1;
    TextView clear2;
    TextView fourthCheck;
    TextView clear3;
    TextView clear4;
    ArrayList<String> keyForFinished = new ArrayList<>();
    ArrayList<String> keyForWaiting = new ArrayList<>();
    ArrayList<String> keyForChallenge = new ArrayList<>();
    ArrayList<String> checking = new ArrayList<>();
    TextView firstCheck;
    TextView secondCheck;
    TextView thirdCheck;
    ArrayList<String> keys = new ArrayList<>();
    ArrayList<String> myNames = new ArrayList<>();
    ArrayList<String> myImages = new ArrayList<>();
    ArrayList<String> myData = new ArrayList<>();
    ArrayList<String> keyForMyself = new ArrayList<>();
    ArrayList<String> counters = new ArrayList<>();
    char[] str = new char[]{'h','w','h'};
    int size = 0;
    boolean colorIt = false;
    int howMuch = 0;
    RecyclerViewForMyself recyclerViewForFinisheds;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.secondfragment, container, false);
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale);
        View dialogView = inflater.inflate(R.layout.alert_tutorials, null);
        alertProgress = dialogView.findViewById(R.id.alertProgress);
        swipeRefreshLayout = view.findViewById(R.id.swipeForSecondFragment);
        preloader = view.findViewById(R.id.preloader);

        clear1 = view.findViewById(R.id.clear);
        clear2 = view.findViewById(R.id.clear2);
        clear3 = view.findViewById(R.id.clear3);
        clear4 = view.findViewById(R.id.clear4);

        firstCheck = view.findViewById(R.id.firstCheck);
        secondCheck = view.findViewById(R.id.secondCheck);
        thirdCheck = view.findViewById(R.id.thirdCheck);
        fourthCheck = view.findViewById(R.id.fourthCheck);
//        swipeRefreshLayout = view.findViewById(R.id.swipeForSecondFragment);
//
        swipeRefreshLayout.setColorScheme(R.color.blue,

                R.color.cards,

                R.color.bronze,

                R.color.green2);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                upload();
                uploadChallenges();
                uploadFinished();
                uploadForMyFinished();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        clear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String key:keys){
                    databaseReference.child("Receives").child(key).removeValue();
                }
                enemyName2.clear();
                theme2.clear();
                keys.clear();
                usernames.clear();
                enemyImage.clear();
                recyclerAdapter.notifyDataSetChanged();
            }
        });
        clear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String key:keyForWaiting){
                    databaseReference.child("Challenges").child(key).removeValue();
                }
                enemyName.clear();
                theme.clear();
                counting.clear();
                usernames2.clear();
                enemyImage2.clear();
                recyclerAdapter2.notifyDataSetChanged();
            }
        });
        clear3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String key:keyForFinished){
                    databaseReference.child("Finished").child(key).removeValue();
                }
                enemySrc.clear();
                enemyText.clear();
                enemyScoring.clear();
                myScoring.clear();
                titling.clear();
                checking.clear();
                keyForFinished.clear();
                recyclerAdapter3.notifyDataSetChanged();
            }
        });
        clear4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String key:keyForMyself){
                    databaseReference.child("MyFinished").child(key).removeValue();
                }
                myImages.clear();
                myNames.clear();
                myTitles.clear();
                keyForMyself.clear();
                counters.clear();
                recyclerViewForFinisheds.notifyDataSetChanged();
            }
        });
        recyclersView3 = view.findViewById(R.id.recycleForEnded);

        alertProgress.setVisibility(View.VISIBLE);

        users = FirebaseAuth.getInstance().getCurrentUser();

        recyclerViewForChallenge = view.findViewById(R.id.recyclerChallenge);

        if (users != null) {
            user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            image = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
        }

        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale);

        cardToFight = view.findViewById(R.id.cardToFight);

        databaseReference = (DatabaseReference) FirebaseDatabase.getInstance().getReference();

        recyclersView = view.findViewById(R.id.recyclerViewForChallenge);

        databaseReference.child("Themes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                titles.clear();
                tutorialImages.clear();
                DataSnapshot data = null;
                if (user != null) {
                    data = dataSnapshot.child(user);
                }
                if (data.exists()) {
                    for (DataSnapshot datas : dataSnapshot.child(user).getChildren()) {
                        String values = datas.child("tutorial").getValue().toString();
                        String images = datas.child("image").getValue().toString();
                        titles.add(values);
                        tutorialImages.add(images);
                    }
                }
                alertProgress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        cardToFight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

                LayoutInflater inflater = getActivity().getLayoutInflater();

                View dialogView = inflater.inflate(R.layout.alert_tutorials, null);

                dialogBuilder.setView(dialogView);

                RecyclerView recyclerView = dialogView.findViewById(R.id.recycleView);

                alertAdapter = new RecycleAlertViewAdapter(getActivity(), titles, tutorialImages);

                alertAdapter.setClickListener(SecondFragment.this);

                recyclerView.setHasFixedSize(true);

                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

                recyclerView.setLayoutManager(horizontalLayoutManager);

                recyclerView.setAdapter(alertAdapter);

                alertAdapter.notifyDataSetChanged();

                alertDialog = dialogBuilder.create();

                alertProgress = dialogView.findViewById(R.id.alertProgress);

                alertProgress.setVisibility(View.GONE);

                alertDialog.show();

            }
        });


        uploadChallenges();

        upload();

        uploadFinished();

        uploadForMyFinished();

        return view;
    }

    public void uploadForMyFinished(){
        databaseReference.child("MyFinished").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(myData.size() != 0){
                    size = myData.size();
                    myData.clear();
                }
                myImages.clear();
                myNames.clear();
                myTitles.clear();
                keyForMyself.clear();
                counters.clear();
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    for (DataSnapshot dating:data.getChildren()){
                        if(user.equals(dating.getKey())){
                            keyForMyself.add(data.getKey());
                            for(DataSnapshot into:dating.getChildren()){
                                myTitles.add(into.getKey());
                                for(DataSnapshot last:into.getChildren()){
                                    if(last.getKey().equals("date")){
                                        myData.add(last.child("date").getValue().toString());
                                        counters.add(last.child("main_score").getValue().toString());
                                        myImages.add(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() + "");
                                        myNames.add(FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "");
                                    }
                                }
                            }
                        }
                    }
                }
                Collections.reverse(myData);
                Collections.reverse(myImages);
                Collections.reverse(myNames);
                Collections.reverse(myTitles);
                Collections.reverse(keyForMyself);
                Collections.reverse(counters);
                if(size != 0){
                    if(size != myData.size()){
                        howMuch = myData.size() - size;
                        colorIt = true;
                    }
                }
                if(myData.size() == 0){
                    fourthCheck.setVisibility(View.VISIBLE);
                }
                else{
                    fourthCheck.setVisibility(View.GONE);
                }
                initMyFinishedRecycler();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void uploadFinished() {
        databaseReference.child("Finished").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                enemySrc.clear();
                enemyText.clear();
                enemyScoring.clear();
                myScoring.clear();
                titling.clear();
                checking.clear();
                keyForFinished.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    keyForFinished.add(data.getKey());
                    inform.clear();
                    hi.clear();
                    me.clear();
                    questions.clear();
                    for (DataSnapshot into : data.getChildren()) {
                        if (into.getKey().equals(user)) {
                            for (DataSnapshot enemy : into.getChildren()) {
                                for (DataSnapshot theme : enemy.getChildren()) {
                                    for (DataSnapshot matter : theme.getChildren()) {
                                        if (matter.getKey().equals("Detect")) {
                                            String enemyImage = matter.child("enemyImage").getValue().toString();
                                            String enemyName = matter.child("enemyName").getValue().toString();
                                            String enemyScore = matter.child("enemyScore").getValue().toString();
                                            String myScore = matter.child("myScore").getValue().toString();
                                            String title = matter.child("title").getValue().toString();
                                            String check = "";
                                            if (Integer.parseInt(myScore) > Integer.parseInt(enemyScore)) {
                                                check = "Winned";
                                            } else if (Integer.parseInt(myScore) < Integer.parseInt(enemyScore)) {
                                                check = "Losed";
                                            } else {
                                                check = "Draw";
                                            }
                                            inform.add(new CollectEnemyInformation(enemyImage, enemyName, enemyScore, myScore, title));
                                            enemySrc.add(enemyImage);
                                            enemyText.add(enemyName);
                                            enemyScoring.add(enemyScore);
                                            myScoring.add(myScore);
                                            titling.add(title);
                                            checking.add(check);
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
                if (inform.size() == 0 && enemySrc.size() == 0) {
                    thirdCheck.setVisibility(View.VISIBLE);
                } else {
                    thirdCheck.setVisibility(View.GONE);
                }
                Collections.reverse(enemySrc);
                Collections.reverse(enemyText);
                Collections.reverse(enemyScoring);
                Collections.reverse(myScoring);
                Collections.reverse(titling);
                Collections.reverse(checking);
                Collections.reverse(keyForFinished);
                initThirdRecycler();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void uploadChallenges() {
        databaseReference.child("Receives").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                enemyName2.clear();
                theme2.clear();
                keys.clear();
                usernames.clear();
                enemyImage.clear();
                int count = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    keys.add(data.getKey());
                    for (DataSnapshot datas : data.getChildren()) {
                        if (datas.getKey().equals(user)) {
                            for (DataSnapshot dating : datas.getChildren()) {
                                enemyName2.add(dating.getKey());
                                for (DataSnapshot into : dating.getChildren()) {
                                    theme2.add(into.getKey());
                                }
                            }
                        }
                    }
                }
                Collections.reverse(keys);
                myApp(new FirebaseCallback() {
                    @Override
                    public void callBack(ArrayList<String> user, ArrayList<String> image) {
                        initSecondRecycler();
                    }
                });
                preloader.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void upload() {
        databaseReference.child("Challenges").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                enemyName.clear();
                theme.clear();
                counting.clear();
                keyForWaiting.clear();
                int count = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    answered = 0;
                    for (DataSnapshot datas : data.getChildren()) {
                        if (datas.getKey().equals(user)) {
                            keyForWaiting.add(data.getKey());
                            hei.clear();
                            count = 0;
                            for (DataSnapshot into : datas.getChildren()) {
                                enemyName.add(into.getKey());
                                for (DataSnapshot into_child : into.getChildren()) {
                                    theme.add(into_child.getKey());
                                    for (DataSnapshot last : into_child.getChildren()) {
                                        if (!last.getKey().equals("main")) {
                                            if (last.child("wrongAnswers").getValue().toString().equals("")) {
                                                hei.add(last.child("wrongAnswers").getValue().toString());
                                            }
                                        }
                                    }
                                }
                            }
                            counting.add(hei.size());
                        }
                    }
                }
                myApp2(new FirebaseCallback() {
                    @Override
                    public void callBack(ArrayList<String> user, ArrayList<String> image) {
                        initRecycler();
                    }
                });

                preloader.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void myApp2(final FirebaseCallback firebaseCallback){
        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usernames.clear();
                enemyImage.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    for (String enemy : enemyName) {
                        if (data.child("Username").getValue().toString().equals(enemy)) {
                            enemyImage.add(data.child("Image").getValue().toString());
                            usernames.add(data.child("Username").getValue().toString());
                        }
                    }
                }
                if (theme.size() == 0) {
                    secondCheck.setVisibility(View.VISIBLE);
                } else {
                    secondCheck.setVisibility(View.GONE);
                }
                firebaseCallback.callBack(enemyImage,usernames);
                Collections.reverse(enemyImage);
                Collections.reverse(usernames);
                Collections.reverse(theme);
                Collections.reverse(counting);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void myApp(final FirebaseCallback firebaseCallback){
        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                usernames2.clear();
                enemyImage2.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    for (String enemy : enemyName2) {
                        if (data.child("Username").getValue().toString().equals(enemy)) {
                            enemyImage2.add(data.child("Image").getValue().toString());
                            usernames2.add(data.child("Username").getValue().toString());
                        }
                    }
                }
                Collections.reverse(enemyImage2);
                Collections.reverse(usernames2);
                Collections.reverse(theme2);
                firebaseCallback.callBack(enemyImage2,usernames2);
                if (theme2.size() == 0) {
                    firstCheck.setVisibility(View.VISIBLE);
                } else {
//                            87017242327
                    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    firstCheck.setVisibility(View.GONE);
//
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private interface FirebaseCallback{
        void callBack(ArrayList<String> user,ArrayList<String> image);
    }

    public void initRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclersView.setLayoutManager(linearLayoutManager);

        recyclerAdapter = new RecyclerViewForFight(getActivity(), usernames, theme, enemyImage, counting);

        recyclersView.setAdapter(recyclerAdapter);

        recyclerAdapter.setOnThisItemClickListener(SecondFragment.this);

        recyclerAdapter.notifyDataSetChanged();
    }

    public void initThirdRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclersView3.setLayoutManager(linearLayoutManager);

        recyclerAdapter3 = new RecyclerViewForFinished(getActivity(), enemySrc, enemyText, enemyScoring, myScoring, titling, checking);

        recyclersView3.setAdapter(recyclerAdapter3);

        recyclerAdapter3.setOnItemClickListener(this);

        recyclerAdapter3.notifyDataSetChanged();
    }

    public void initSecondRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerViewForChallenge.setLayoutManager(linearLayoutManager);

        recyclerAdapter2 = new RecyclerViewForChallenge(getActivity(), usernames2, theme2, enemyImage2);

        recyclerViewForChallenge.setAdapter(recyclerAdapter2);

        recyclerAdapter2.setOnItemClickListener(SecondFragment.this);

        recyclerAdapter2.notifyDataSetChanged();
    }
    public void initMyFinishedRecycler(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerViewForFinisheds = new RecyclerViewForMyself(getActivity(),myNames,myImages,myData,myTitles);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewForMyFinished);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerViewForFinisheds);
        recyclerViewForFinisheds.notifyDataSetChanged();
        recyclerViewForFinisheds.setClickListener(this);
    }

    @Override
    public void onClick(View view, int position) {
        String title = titles.get(position);

        Intent intent = new Intent(getActivity(), CallPersons.class);

        intent.putExtra("title", title);

        alertDialog.dismiss();

        startActivity(intent);
    }


    @Override
    public void onMeClick(View view, int position) {

        Intent intent = new Intent(getActivity(), ReceiveChallenge.class);

        intent.putExtra("main_image", image);
        intent.putExtra("main_user_text", user);
        intent.putExtra("second_image", enemyImage2.get(position));
        intent.putExtra("second_user_text", usernames2.get(position));
        intent.putExtra("theme", theme2.get(position));
        intent.putExtra("key", keys.get(position));

        Pair[] pairs = new Pair[0];

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);

        startActivity(intent, options.toBundle());
    }

    @Override
    public void onClickForFinished(View view, final int position) {
        databaseReference.child("Finished").child(keyForFinished.get(position)).child(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                questions.clear();
                hi.clear();
                me.clear();
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    for (DataSnapshot dating:data.getChildren()){
                        for (DataSnapshot numbers:dating.getChildren()){
                            if(!numbers.getKey().equals("Detect")){
                                hi.add(numbers.child("correctAnswers").getValue().toString());
                                questions.add(numbers.child("questions").getValue().toString());
                                me.add("n" + numbers.child("wrongAnswers").getValue().toString());
                            }
                        }
                    }
                }
                Intent intent = new Intent(getActivity(),DetailPageOfResults.class);
                intent.putExtra("enemy_image",enemySrc.get(position));
                intent.putExtra("enemy_name",enemyText.get(position));
                intent.putExtra("enemy_score",enemyScoring.get(position) + "");
                intent.putExtra("my_score",myScoring.get(position));
                intent.putExtra("title",titling.get(position));
                intent.putExtra("check",checking.get(position));
                intent.putStringArrayListExtra("questions",questions);
                intent.putStringArrayListExtra("rightAnswers",hi);
                intent.putStringArrayListExtra("wrongAnswers",me);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    @Override
    public void onClickForMySelf(View view, final int position) {

        databaseReference.child("MyFinished").child(keyForMyself.get(position)).child(user).child(myTitles.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> hi = new ArrayList<>();
                ArrayList<String> questions = new ArrayList<>();
                ArrayList<String> me = new ArrayList<>();
                ArrayList<String> code = new ArrayList<>();
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    if(!data.getKey().equals("date")){
                        hi.add(data.child("correctAnswers").getValue().toString());
                        questions.add(data.child("questions").getValue().toString());
                        me.add("n" + data.child("wrongAnswers").getValue().toString());
                        code.add(data.child("code").getValue().toString());
                    }
                }
                Intent intent = new Intent(getActivity(),DetailPageOfResults.class);
                intent.putExtra("enemy_image","madmad");
                intent.putExtra("enemy_name","kenzhe");
                intent.putExtra("enemy_score","0");
                intent.putExtra("my_scoring",counters.get(position));
                intent.putExtra("title",myTitles.get(position));
                intent.putExtra("check","Failed");
                intent.putStringArrayListExtra("questions",questions);
                intent.putStringArrayListExtra("rightAnswers",hi);
                intent.putStringArrayListExtra("wrongAnswers",me);
                intent.putStringArrayListExtra("code",code);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    @Override
    public void onThisItem(View v, int position) {
        Intent intent = new Intent(getActivity(), ReceiveChallenge.class);
        intent.putExtra("main_image2", image);
        intent.putExtra("main_user_text2", user);
        intent.putExtra("second_image2", enemyImage.get(position));
        intent.putExtra("second_user_text2", usernames.get(position));
        intent.putExtra("theme", theme.get(position));
        intent.putExtra("counts", counting.get(position) + "");
        Pair[] pairs = new Pair[0];
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear3:
                enemySrc.clear();
                enemyScoring.clear();
                enemyText.clear();
                myScoring.clear();
                titling.clear();
                checking.clear();
        }
    }

    public class RecyclerViewForMyself extends RecyclerView.Adapter<RecyclerViewForMyself.ViewHolder> {
        private static final String TAG = "RecycleAlertViewAdapter";
        ArrayList<String> images = new ArrayList<>();
        ItemOnMeClickListener itemClickListener;
        ArrayList<String> usernames = new ArrayList<>();
        ArrayList<String> data = new ArrayList<>();
        private Context mContext;
        ArrayList<String> title = new ArrayList<>();

        public RecyclerViewForMyself(Context context, ArrayList<String> t, ArrayList<String> i,ArrayList<String> data,ArrayList<String> title) {
            usernames = t;
            mContext = context;
            images = i;
            this.data = data;
            this.title = title;
        }

        @NonNull
        @Override
        public RecyclerViewForMyself.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tutorials, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final @NonNull RecyclerViewForMyself.ViewHolder viewHolder, int i) {
            viewHolder.textView.setText(usernames.get(i));
            Glide
                    .with(mContext)
                    .load(images.get(i))
                    .into(viewHolder.imageView);
            viewHolder.date.setText(data.get(i));
            if(colorIt){
                for (int x = 0;x < howMuch;x++){
                    if(i == x){
                        Log.i("info",i + "");
                        viewHolder.linearLayout.setBackgroundResource(R.color.bronzovii);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                viewHolder.linearLayout.setBackgroundResource(R.color.white);
                            }
                        },2000);
                    }
                }
            }
        }

        @Override
        public int getItemCount() {
            return usernames.size();
        }

        public void setClickListener(ItemOnMeClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView textView;
            CircleImageView imageView;
            RelativeLayout relativeLayout;
            LinearLayout linearLayout;
            TextView date;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageOfTutorial);
                textView = itemView.findViewById(R.id.titleOfTutorial);
                relativeLayout = itemView.findViewById(R.id.showForDate);
                date = itemView.findViewById(R.id.date);
                linearLayout = itemView.findViewById(R.id.colorThis);
                relativeLayout.setVisibility(View.VISIBLE);
                itemView.setOnClickListener(this);
                textView.setTextSize(14);
                textView.setPadding(0,0,0,0);
            }

            @Override
            public void onClick(View view) {
                itemClickListener.onClickForMySelf(view, getPosition());
            }
        }
    }

    public class RecycleAlertViewAdapter extends RecyclerView.Adapter<RecycleAlertViewAdapter.ViewHolder> {
        private static final String TAG = "RecycleAlertViewAdapter";
        ArrayList<String> titles = new ArrayList<>();
        ClickLister itemClickListener;
        ArrayList<String> images = new ArrayList<>();
        private Context mContext;

        public RecycleAlertViewAdapter(Context context, ArrayList<String> t, ArrayList<String> i) {
            titles = t;
            mContext = context;
            images = i;
        }

        @NonNull
        @Override
        public RecycleAlertViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tutorials, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecycleAlertViewAdapter.ViewHolder viewHolder, int i) {
            viewHolder.textView.setText(titles.get(i));
            Glide
                    .with(mContext)
                    .load(images.get(i))
                    .into(viewHolder.imageView);
        }

        @Override
        public int getItemCount() {
            return titles.size();
        }

        public void setClickListener(ClickLister itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView textView;
            CircleImageView imageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageOfTutorial);
                textView = itemView.findViewById(R.id.titleOfTutorial);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                itemClickListener.onClick(view, getPosition());
            }
        }
    }

    class RecyclerViewForFinished extends RecyclerView.Adapter<RecyclerViewForFinished.ViewHolder> {

        ItemOnMeClickListener itemClickListener;

        ArrayList<CollectEnemyInformation> information = new ArrayList<>();
        ArrayList<String> enemySrc = new ArrayList<>();
        ArrayList<String> enemyText = new ArrayList<>();
        ArrayList<String> enemyScoring = new ArrayList<>();
        ArrayList<String> myScoring = new ArrayList<>();
        ArrayList<String> titling = new ArrayList<>();
        ArrayList<String> checking = new ArrayList<>();


        private Context mContext;

        public RecyclerViewForFinished(Context context, ArrayList<String> enemySrc, ArrayList<String> enemyText, ArrayList<String> enemyScoring, ArrayList<String> myScoring, ArrayList<String> titling, ArrayList<String> checking) {

            mContext = context;
            this.enemySrc = enemySrc;
            this.enemyText = enemyText;
            this.enemyScoring = enemyScoring;
            this.myScoring = myScoring;
            this.titling = titling;
            this.checking = checking;
        }

        @NonNull
        @Override
        public RecyclerViewForFinished.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_adapter, viewGroup, false);

            ViewHolder viewHolder = new ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewForFinished.ViewHolder viewHolder, int i) {

            Glide.with(mContext).load(enemySrc.get(i)).into(viewHolder.imageView);
            viewHolder.textView.setText(enemyText.get(i));
            viewHolder.textView2.setText(titling.get(i));
            viewHolder.winned.setText(checking.get(i));
            viewHolder.enemyCount.setText(enemyScoring.get(i));
            viewHolder.myCount.setText(myScoring.get(i));

            if (viewHolder.winned.getText().toString().equals("Winned")) {
                viewHolder.checkWin.setBackgroundResource(R.color.green2);
            } else if (viewHolder.winned.getText().toString().equals("Losed")) {
                viewHolder.checkWin.setBackgroundResource(R.color.cards);
            } else {
                viewHolder.checkWin.setBackgroundResource(R.color.colorPrimaryDark);
            }
        }

        public void setOnItemClickListener(ItemOnMeClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public int getItemCount() {
            return enemyScoring.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView textView;
            TextView textView2;
            TextView myCount;
            LinearLayout linearLayout;
            LinearLayout changeBackground;
            CircleImageView imageView;
            LinearLayout checkWin;

            TextView winned;
            TextView enemyCount;

            public ViewHolder(@NonNull View itemView)  {
                super(itemView);
                imageView = itemView.findViewById(R.id.circleImage);
                textView = itemView.findViewById(R.id.text1);

                textView2 = itemView.findViewById(R.id.texting2);
                myCount = itemView.findViewById(R.id.myCount);
                enemyCount = itemView.findViewById(R.id.enemyCount);

                linearLayout = itemView.findViewById(R.id.countLinear);

                changeBackground = itemView.findViewById(R.id.changeBackground);
                checkWin = itemView.findViewById(R.id.checkWin);
                checkWin.setVisibility(View.VISIBLE);
                winned = itemView.findViewById(R.id.winned);
                animationImageView = itemView.findViewById(R.id.circleImage);

                itemView.setOnClickListener(this);


            }

            @Override
            public void onClick(View v) {
                itemClickListener.onClickForFinished(v, getPosition());
            }
        }
    }

    class RecyclerViewForChallenge extends RecyclerView.Adapter<RecyclerViewForChallenge.ViewHolder> {

        ItemOnMeClickListener itemClickListener;

        ArrayList<String> images = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> usernames = new ArrayList<>();


        private Context mContext;

        public RecyclerViewForChallenge(Context context, ArrayList<String> titles, ArrayList<String> usernames, ArrayList<String> images) {

            mContext = context;
            this.titles = titles;
            this.usernames = usernames;
            this.images = images;
        }

        @NonNull
        @Override
        public RecyclerViewForChallenge.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_adapter, viewGroup, false);

            ViewHolder viewHolder = new ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewForChallenge.ViewHolder viewHolder, int i) {

            Glide.with(mContext).load(images.get(i)).into(viewHolder.imageView);

            viewHolder.textView.setText(titles.get(i));

            viewHolder.textView2.setText(usernames.get(i));

            viewHolder.changeBackground.setBackgroundResource(R.color.bronze2);

        }

        public void setOnItemClickListener(ItemOnMeClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public int getItemCount() {
            return usernames.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView textView;
            TextView textView2;
            TextView myCount;
            LinearLayout linearLayout;
            LinearLayout changeBackground;
            LinearLayout checkWin;
            CircleImageView imageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.circleImage);
                textView = itemView.findViewById(R.id.text1);

                textView2 = itemView.findViewById(R.id.texting2);
                myCount = itemView.findViewById(R.id.myCount);

                linearLayout = itemView.findViewById(R.id.countLinear);
                linearLayout.setVisibility(View.GONE);
                changeBackground = itemView.findViewById(R.id.changeBackground);
                animationImageView = itemView.findViewById(R.id.circleImage);

                checkWin = itemView.findViewById(R.id.checkWin);
                checkWin.setVisibility(View.GONE);

                itemView.setOnClickListener(this);

            }

            @Override
            public void onClick(View v) {
                itemClickListener.onMeClick(v, getPosition());
            }
        }
    }


    class RecyclerViewForFight extends RecyclerView.Adapter<RecyclerViewForFight.ViewHolder> {

        ItemOnThisClickListener itemClickListener;

        ArrayList<String> images = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> usernames = new ArrayList<>();
        ArrayList<Integer> accounting = new ArrayList<>();
        ArrayList<Hei> all = new ArrayList<>();


        private Context mContext;

        public RecyclerViewForFight(Context context, ArrayList<String> titles, ArrayList<String> usernames, ArrayList<String> images, ArrayList<Integer> accounting) {

            mContext = context;
            this.titles = titles;
            this.usernames = usernames;
            this.images = images;
            this.accounting = accounting;
        }

        @NonNull
        @Override
        public RecyclerViewForFight.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_adapter, viewGroup, false);

            ViewHolder viewHolder = new ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewForFight.ViewHolder viewHolder, int i) {

            Glide.with(mContext).load(images.get(i)).into(viewHolder.imageView);

            viewHolder.textView.setText(titles.get(i));

            viewHolder.textView2.setText(usernames.get(i));

            viewHolder.myCount.setText(accounting.get(i) + "");

        }

        public void setOnThisItemClickListener(ItemOnThisClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public int getItemCount() {
            return theme.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView textView;
            TextView textView2;
            TextView myCount;
            LinearLayout linearLayout;
            CircleImageView imageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.circleImage);
                textView = itemView.findViewById(R.id.text1);
                textView2 = itemView.findViewById(R.id.texting2);
                myCount = itemView.findViewById(R.id.myCount);
                linearLayout = itemView.findViewById(R.id.changeBackground);
                firstImageView = itemView.findViewById(R.id.circleImage);
                itemView.setOnClickListener(this);
                LinearLayout checkWin = itemView.findViewById(R.id.checkWin);
                checkWin.setVisibility(View.GONE);
            }

            @Override
            public void onClick(View v) {
                itemClickListener.onThisItem(v, getPosition());
            }
        }
    }

}
