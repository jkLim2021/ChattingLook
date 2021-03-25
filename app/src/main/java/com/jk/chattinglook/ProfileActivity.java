package com.jk.chattinglook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {

    ArrayList<Profileitem> profileitems = new ArrayList<>();
    ArrayList<Profileitem> profileitems2 = new ArrayList<>();
    RecyclerView recyclerView, recyclermyView;
    ProfileAdapter adapter, adapter2;

    TextView tv;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tv = findViewById(R.id.tv);


//        Intent intentget = getIntent();
//        int position = intentget.getIntExtra("position",0);
//        String message = intentget.getStringExtra("message");
//        String room = intentget.getStringExtra("room");
//        String dbothernikename = intentget.getStringExtra("dbothernikename");
//        boolean back = intentget.getBooleanExtra("back",false);
//        G.room=room;
//        G.message=message;
//        G.dbothernikename=dbothernikename;
//        G.position=position;
//        G.back=back;
////        tv.setText(room+"\n"+message+"\n"+dbothernikename);
//        tv.setText(G.room+"\n"+G.message+"\n"+G.dbothernikename+"\n"+G.position+"\n"+G.back);
//
//
//        profileitems.get(position).nickname= message;


        recyclerView = findViewById(R.id.recycler);
        recyclermyView = findViewById(R.id.recyclermy);
        recyclerView.setHasFixedSize(true);
        recyclermyView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.LayoutManager layoutManagermy = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclermyView.setLayoutManager(layoutManagermy);

        adapter = new ProfileAdapter(this, profileitems);
        adapter2 = new ProfileAdapter(this, profileitems2);
        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
        recyclermyView.setAdapter(adapter2); // 리사이클러뷰에 어댑터 연결


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = firebaseDatabase.getReference();


        rootRef.child("profile").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Profileitem person = snapshot.getValue(Profileitem.class);

                if (!G.nickName.equals(person.nickname)) {
                    profileitems.add(person);
                    adapter.notifyItemInserted(profileitems.size());
                }

                else if (G.nickName.equals(person.nickname)) {
                    profileitems2.add(person);
                    adapter2.notifyItemInserted(profileitems2.size());
                }
//                else if (G.nickName.equals(person.nickname)) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            int position = data.getIntExtra("position", 0);
            message = data.getStringExtra("message");

//            Toast.makeText(this, "" + position + message, Toast.LENGTH_SHORT).show();

            if (message != null) {
                profileitems.get(position).msg = message;
//                saveData();
            }

            adapter.notifyItemChanged(position);

        }

    }


    //닉네임과 프로필이미지를 저장하는 기능 메소드
    void saveData() {


        //아이템 불러오기
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = firebaseDatabase.getReference();

        rootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Profileitem gifticon = new Profileitem(G.profileUrl, G.nickName, message);
                DatabaseReference personRef = rootRef.child("profile");
                personRef.child(gifticon.nickname).setValue(gifticon);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        rootRef.child("profile").Ons(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(MainActivity.this, "업로드 완료", Toast.LENGTH_SHORT).show();
//
//                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//
//                        Profileitem gifticon = new Profileitem(profileUrl, nickname, "메세지없음");
//                        DatabaseReference personRef = rootRef.child("profile");
//                        personRef.child(gifticon.nickname).setValue(gifticon);
//
//                    }
//                });
//            }
//        });
    }

}