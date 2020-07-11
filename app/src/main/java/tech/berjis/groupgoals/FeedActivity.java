package tech.berjis.groupgoals;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference dbRef;
    String UID;

    private ViewPager groupsPager;
    GroupsPagerAdapter groupsPagerAdapter;
    WormDotsIndicator dots_indicator;
    List<GroupsList> listData;
    View groupsTotal, personalTotal;
    ImageView profile;
    TextView createGroupsText, allGroupsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();
        UID = mAuth.getCurrentUser().getUid();
        dbRef.keepSynced(true);

        init_vars();

        newUserState();
    }

    private void init_vars() {
        groupsTotal = findViewById(R.id.groupsTotal);
        profile = findViewById(R.id.profile);
        personalTotal = findViewById(R.id.personalTotal);
        createGroupsText = findViewById(R.id.createGroupsText);
        allGroupsText = findViewById(R.id.allGroupsText);
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
                } else {
                    loadGroups();
                    staticOnclicks();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadGroups() {
        listData = new ArrayList<>();
        listData.clear();
        dbRef.child("MyGroups").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot npsnapshot : snapshot.getChildren()) {
                        GroupsList l = npsnapshot.getValue(GroupsList.class);
                        listData.add(l);
                    }
                    // setup viewpager
                    groupsPager = findViewById(R.id.groupsPager);
                    groupsPagerAdapter = new GroupsPagerAdapter(FeedActivity.this, listData);
                    groupsPager.setAdapter(groupsPagerAdapter);
                    dots_indicator = findViewById(R.id.dots_indicator);
                    dots_indicator.setViewPager(groupsPager);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void staticOnclicks() {
        groupsTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FeedActivity.this, GroupsActivity.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FeedActivity.this, ProfileActivity.class));
            }
        });
        personalTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FeedActivity.this, PersonalActivity.class));
            }
        });
        allGroupsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FeedActivity.this, GroupsActivity.class));
            }
        });
        createGroupsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FeedActivity.this, GroupsCreateActivity.class));
            }
        });
    }
}
