package tech.berjis.groupgoals;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private TextView groupPurpose, groupGoal;

    public GroupFragment(Context mContext, String group) {
        this.mContext = mContext;
        this.group = group;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group, container, false);

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.keepSynced(true);

        groupPurpose = view.findViewById(R.id.groupPurpose);
        groupGoal = view.findViewById(R.id.groupGoal);

        loadGroupData();

        return view;
    }

    private void loadGroupData(){

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("Groups").child(group).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currency = snapshot.child("currency").getValue().toString();
                long goal = Long.parseLong(snapshot.child("goal").getValue().toString());
                String purpose = snapshot.child("description").getValue().toString();
                DecimalFormat formatter = new DecimalFormat("#,###,###");
                groupPurpose.setText(purpose);
                groupGoal.setText(currency+" "+formatter.format(goal));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
