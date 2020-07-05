package tech.berjis.groupgoals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDexApplication;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference dbRef;
    String UID;

    private ViewPager groupsPager;
    GroupsPagerAdapter groupsPagerAdapter;
    DotsIndicator dots_indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();
        UID = mAuth.getCurrentUser().getUid();
        dbRef.keepSynced(true);
        newUserState();

    }

    private void newUserState() {
        dbRef.child("Users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("first_name").exists() ||
                        !dataSnapshot.child("last_name").exists() ||
                        !dataSnapshot.child("user_name").exists() ||
                        !dataSnapshot.child("user_email").exists()) {
                    startActivity(new Intent(FeedActivity.this, EditProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }else{
                    loadGroups();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadGroups(){
        final List<GroupsList> mList = new ArrayList<>();
        mList.add(new GroupsList("","","","","","","",0));
        mList.add(new GroupsList("","","","","","","",0));
        mList.add(new GroupsList("","","","","","","",0));

        // setup viewpager
        groupsPager = findViewById(R.id.groupsPager);
        groupsPagerAdapter = new GroupsPagerAdapter(FeedActivity.this, mList);
        groupsPager.setAdapter(groupsPagerAdapter);
        dots_indicator = findViewById(R.id.dots_indicator);
        dots_indicator.setViewPager(groupsPager);
    }
}
