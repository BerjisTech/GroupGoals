package tech.berjis.groupgoals;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class GroupFragment extends Fragment {
    private Context mContext;
    private String group;
    private DatabaseReference dbRef;
    private FirebaseAuth mAuth;
    private TextView groupPurpose, groupGoal, groupMembers;
    private ImageView membersIcon;

    public GroupFragment(Context mContext, String group) {
        this.mContext = mContext;
        this.group = group;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        initViews(view);
        loadGroupData();
        onClicks();
        return view;
    }

    private void initViews(View view) {
        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.keepSynced(true);

        groupPurpose = view.findViewById(R.id.groupPurpose);
        groupGoal = view.findViewById(R.id.groupGoal);
        membersIcon = view.findViewById(R.id.membersIcon);
        groupMembers = view.findViewById(R.id.groupMembers);
    }

    private void loadGroupData() {

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("Groups").child(group).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currency = snapshot.child("symbol").getValue().toString();
                long goal = Long.parseLong(snapshot.child("goal").getValue().toString());
                String purpose = snapshot.child("description").getValue().toString();
                DecimalFormat formatter = new DecimalFormat("#,###,###");
                groupPurpose.setText(purpose);
                groupGoal.setText(currency + " " + formatter.format(goal));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void onClicks() {
        membersIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent groupIntent = new Intent(mContext, GroupMembersActivity.class);
                Bundle groupBundle = new Bundle();
                groupBundle.putString("group_id", group);
                groupIntent.putExtras(groupBundle);
                mContext.startActivity(groupIntent);
            }
        });
        groupMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent groupIntent = new Intent(mContext, GroupMembersActivity.class);
                Bundle groupBundle = new Bundle();
                groupBundle.putString("group_id", group);
                groupIntent.putExtras(groupBundle);
                mContext.startActivity(groupIntent);
            }
        });
    }
}
